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

  } finally {
    sh 'docker rm -f postgres'
    cleanWs()
  }
}
