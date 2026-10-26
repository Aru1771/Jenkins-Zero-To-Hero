Passing configuration between Jenkinsfile and Shared Libraries:
===============================================================

1. Real-world scenario

* Imagine your team deploys two applications:

      - Empower — version 2.0 to dev
      - NuGenesis — version 3.0 to stage
* Instead of hardcoding these values inside the library, pass them from the Jenkinsfile.

2. Shared library code

File: vars/deployApp.groovy

    def call(Map config) {
    
        def appName = config.appName?.toString()?.trim()
        def version = config.version?.toString()?.trim()
        def environment = config.environment?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        if (!version) {
            error 'Application version is required'
        }
    
        if (!environment) {
            error 'Deployment environment is required'
        }
    
        echo "Application: ${appName}"
        echo "Version: ${version}"
        echo "Environment: ${environment}"
    
        echo "Deploying ${appName} version ${version} to ${environment}"
    }



* Notice that config receives all the named arguments as a map. The library validates the required values before printing the deployment messages.

3. Jenkinsfile

        @Library('my-shared-library') _
        
        pipeline {
            agent any
        
            stages {
                stage('Deploy Empower') {
                    steps {
                        deployApp(
                            appName: 'Empower',
                            version: '2.0',
                            environment: 'dev'
                        )
                    }
                }
        
                stage('Deploy NuGenesis') {
                    steps {
                        deployApp(
                            appName: 'NuGenesis',
                            version: '3.0',
                            environment: 'stage'
                        )
                    }
                }
            }
        }



4. Expected console output

        Application: Empower
        Version: 2.0
        Environment: dev
        Deploying Empower version 2.0 to dev
        
        Application: NuGenesis
        Version: 3.0
        Environment: stage
        Deploying NuGenesis version 3.0 to stage


* These messages simulate deployment; they do not deploy anything to a real environment.
 Update the Jenkinsfile to pass region: 'us-east-1' to both applications.
