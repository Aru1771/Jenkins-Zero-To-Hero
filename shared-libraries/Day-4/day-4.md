Shared Libraries — Day 4
=======================

* Topic: Reduce code duplication and improve reusability
* Today, we'll learn how to write shared library functions so that multiple applications can reuse the same logic instead of duplicating code in every Jenkinsfile.

Understand the real-world problem
----------------------------------

* Imagine you deploy two applications: Empower and NuGenesis.

* Without a shared library, you might repeat the same build logic in both Jenkinsfiles:

      stage('Build Empower') {
          steps {
              echo 'Building Empower'
              echo 'Checking dependencies'
              echo 'Compiling application'
          }
      }
      
      stage('Build NuGenesis') {
          steps {
              echo 'Building NuGenesis'
              echo 'Checking dependencies'
              echo 'Compiling application'
          }
      }



* The dependency-checking and compilation steps are duplicated. If the build process changes, you must update both places.

* In a real DevOps team, we want to write the common logic once and reuse it.

Create a reusable shared library function
-----------------------------------------

File: vars/buildApp.groovy

    def call(Map config) {
    
        def appName = config.appName?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        echo "Building ${appName}"
        echo "Checking dependencies for ${appName}"
        echo "Compiling ${appName}"
    }
    


* Now, the build logic exists in one place.

Reuse it in your Jenkinsfile
----------------------------

    @Library('my-shared-library') _
    
    pipeline {
        agent any
    
        stages {
            stage('Build Empower') {
                steps {
                    buildApp(appName: 'Empower')
                }
            }
    
            stage('Build NuGenesis') {
                steps {
                    buildApp(appName: 'NuGenesis')
                }
            }
        }
    }
    
    

* The same function handles both applications. You only change the argument.

Jenkinsfile

    Controls the pipeline stages


buildApp(appName: 'Empower')

    Calls the reusable function
  

vars/buildApp.groovy

    Executes the common build logic


Why this matters in production
---------------------------------

    - Less duplication: maintain common logic in one file.
    - Consistency: applications follow the same build procedure.
    - Easier maintenance: update the shared function instead of many Jenkinsfiles.
    - Reusable design: pass different application names, versions, or environments as arguments.

* One important distinction: a shared library reduces duplicated pipeline logic, but it doesn't automatically make the applications' build processes identical if their requirements differ.

Day 4 practical task
---------------------

* Use your existing my-shared-library repository.

      Create or update vars/buildApp.groovy so it:
      1. Accepts appName and version through Map config.
      2. Validates that both values are present.
      3. Prints the application name and version.
      4. Prints Checking dependencies and Compiling application.

* Then update your Jenkinsfile to call the same buildApp() function for both Empower version 2.0 and NuGenesis version 3.0, in separate stages.

* Your turn: write the buildApp.groovy code first and send it here. I'll review it before you update the Jenkinsfile.
