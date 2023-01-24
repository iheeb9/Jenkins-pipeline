node(){
  try{
    def buildNum = env.BUILD_NUMBER 
    def branchName= env.BRANCH_NAME
    
    print buildNum
    print branchName

   stage('SERVICE - Git checkout'){
      git branch: branchName, url: "https://github.com/iheeb9/Jenkins-pipeline.git"
    }


      stage('Env - run postgres'){
      sh "chmod +x -R ${env.WORKSPACE}"
      sh "./generator.sh -p"
      sh "docker ps -a"
      
    }
    
    
    /* déterminer l'extension */
    if (branchName == "dev" ){
      extension = "-SNAPSHOT"
    }
    if (branchName == "stage" ){
      extension = "-RC"
    }
    if (branchName == "master" ){
      extension = ""
    }

    /* Récupération du commitID long */
    def commitIdLong = sh returnStdout: true, script: 'git rev-parse HEAD'

    /* Récupération du commitID court */
    def commitId = commitIdLong.take(7)

    /* Modification de la version dans le pom.xml */
    sh "sed -i s/'-XXX'/${extension}/g spring-boot-server/pom.xml"

    /* Récupération de la version du pom.xml après modification */
    def version = sh returnStdout: true, script: "cat spring-boot-server/pom.xml | grep -A1 '<artifactId>myapp1' | tail -1 |perl -nle 'm{.*<version>(.*)</version>.*};print \$1' | tr -d '\n'"

     print """
     #################################################
        BanchName: $branchName
        CommitID: $commitId
        AppVersion: $version
        JobNumber: $buildNum
     #################################################
        """

    /* Maven - tests */
    stage('SERVICE - Tests unitaires'){
      sh ' cd spring-boot-server && docker run --rm --name maven-${commitIdLong} -v /var/lib/jenkins/maven/:/root/.m2 -v "$(pwd)":/usr/src/mymaven --network generator_generator -w /usr/src/mymaven maven:3.3-jdk-8 mvn -B clean test'
    }

    /* Maven - build */
    stage('SERVICE - Jar'){
      sh ' cd spring-boot-server && docker run --rm --name maven${commitIdLong} -v /var/lib/jenkins/maven/:/root/.m2 -v "$(pwd)":/usr/src/mymaven --network generator_generator -w /usr/src/mymaven maven:3.3-jdk-8  mvn -B clean install'
    }


    
    
    
     /* Docker - build & push */
    /* Attention Credentials */
    def imageName='192.168.116.133:5000/myapp'

    stage('DOCKER - Build/Push registry'){
      docker.withRegistry('http://192.168.5.5:5000', 'myregistry_login') {
         def customImage = docker.build("$imageName:${version}-${commitId}")
         customImage.push()
      }
      sh "docker rmi $imageName:${version}-${commitId}"
    }

    /* Docker - test */
    stage('DOCKER - check registry'){
      withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'myregistry_login',usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
      sh 'curl -sk --user $USERNAME:$PASSWORD https://192.168.5.5:5000/v2/myapp/tags/list'
      }
    }


    
    
    
    

  } finally {
    sh 'docker rm -f postgres'
    cleanWs()
  }
}
