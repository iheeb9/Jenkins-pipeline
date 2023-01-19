node{
    cleanWs()
	try{
	def buildNum = env.BUILD_NUMBER
        def branchName= env.BRANCH_NAME
		print buildNum
		print branchName
		
	stage('POSTGRESQL - Container run')
		{
        	sh "./generator.sh -p"
        	sh "docker ps -a"         
		}
	

	   }
	finally{
		sh 'docker rm -f postgres'
        	cleanWs()

  	}
