node(){
  try{
    def buildNum = env.BUILD_NUMBER 
    def branchName= env.BRANCH_NAME
    
    print buildNum
    print branchName

    stage('Env - clone generator'){
      git "http://gitlab.example.com/mypipeline/generator.git"
    }

    stage('Env - run postgres'){
      sh "./generator.sh -p"
      sh "docker ps -a"
      
    }

  } finally {
    sh 'docker rm -f postgres'
    cleanWs()
  }
}
