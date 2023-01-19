node{
    cleanWs()
	try{
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
