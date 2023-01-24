node(){
  try{
    def buildNum = env.BUILD_NUMBER 
    def branchName= env.BRANCH_NAME
    
    print buildNum
    print branchName

   stage('SERVICE - Git checkout'){
      git branch: branchName, url: "http://gitlab.example.com/mypipeline/myapp1.git"
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
