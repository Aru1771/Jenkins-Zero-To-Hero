Topic: Using shared libraries with pipeline parameters
======================================================

* So far, you've learned how to create reusable functions, validate inputs, and pass configuration using Map config.
*  Today, we'll connect those concepts with Jenkins build parameters.

# 1. Real-world scenario

* Suppose your team deploys Empower to different environments.
* Instead of editing the Jenkinsfile every time, you want to select the application version and environment when starting a Jenkins build.

For example:

    - APP_VERSION → 2.0
    - DEPLOY_ENV → stage
* The Jenkinsfile passes these values to the shared library, and the library uses them.

# 2. Shared library: vars/deployApp.groovy

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
          echo "Simulating deployment..."
      }
      
      

* Notice that the library doesn't decide which version to deploy. It receives the configuration from the Jenkinsfile.

# 3. Jenkinsfile: pass parameters to the library

    @Library('my-shared-library') _
    
    pipeline {
        agent any
    
        parameters {
            string(
                name: 'APP_VERSION',
                defaultValue: '2.0',
                description: 'Application version to deploy'
            )
    
            choice(
                name: 'DEPLOY_ENV',
                choices: ['dev', 'stage', 'prod'],
                description: 'Target environment'
            )
        }
    
        stages {
            stage('Deploy Application') {
                steps {
                    deployApp(
                        appName: 'Empower',
                        version: params.APP_VERSION,
                        environment: params.DEPLOY_ENV
                    )
                }
            }
        }
    }



# 4. Understand the flow

Jenkins build parameters

    APP_VERSION = 2.0 · DEPLOY_ENV = stage

Jenkinsfile

    Reads values using params.APP_VERSION and params.DEPLOY_ENV

    deployApp(Map config)

* Validates the values and simulates deployment


Important: Because prod is an available choice, this example is for learning only. A real production pipeline should add appropriate approval and deployment safeguards before allowing production deployment.

Day 6 practical task
---------------------
Modify the Jenkinsfile yourself:

    1. Add a string parameter named APP_NAME, with default value Empower.
    2. Keep the APP_VERSION and DEPLOY_ENV parameters.
    3. Pass params.APP_NAME to deployApp() instead of hardcoding 'Empower'.
    4. Keep the validation inside deployApp.groovy.
    5. Use echo only; do not perform a real deployment.
