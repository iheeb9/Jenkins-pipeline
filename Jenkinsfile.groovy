node(){
  try{
    def buildNum = env.BUILD_NUMBER 
    def branchName= env.BRANCH_NAME
    
    print buildNum
    print branchName

    stage('Env - clone generator'){
      git "https://github.com/iheeb9/Jenkins-pipeline.git"
    }



  } finally {
    sh 'docker rm -f postgres'
    cleanWs()
  }
}
