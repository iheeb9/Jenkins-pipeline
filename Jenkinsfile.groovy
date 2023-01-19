node{
    cleanWs()
	try{
	def buildNum = env.BUILD_NUMBER
        def branchName= env.BRANCH_NAME
		print buildNum
		print branchName
		stage('PremiereEtape'){
		  sh "echo 'Hello world !! '"
 		}
		stage('DexiemeEtape'){
		  sh "echo 'Hello world !!'"
		}
	    }
	finally{
		cleanWs()
		}
	    }
