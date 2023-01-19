node{
    cleanWs()
	try{
	def buildNum = env.BUILD_NUMBER
        def branchName= env.BRANCH_NAME
	
		stage('PremiereEtape'){
		  sh "echo 'Hello world !! '"
			print buildNum
			print branchName	
 		}
		stage('DexiemeEtape'){
		  sh "echo 'Hello world !!'"
		}
	    }
	finally{
		cleanWs()
		}
	    }
