Reusable functions and organizing pipeline logic
================================================

Yesterday, you completed default values, null handling, and input validation. 
Today, we'll learn how to split common CI/CD operations into reusable Shared Library functions, like a DevOps engineer would in a real project.

Why do we need multiple reusable functions?
--------------------------------------------

* Imagine your Empower application pipeline has these operations:

      Checkout
         ↓
      Build
         ↓
      Test
         ↓
      Docker Build
         ↓
      Deploy

* If you put all the logic inside one large deployApp.groovy file, it becomes harder to maintain and troubleshoot.

* Instead, separate the operations into reusable functions.

      Application Jenkinsfile
      
      Jenkins Shared Library
      vars/ directory
      
      buildApp()
      Build logic
      
      
      runTests()
      Testing logic
      
      
      buildDockerImage()
      Image build logic

        deployApp()
      Deployment logic

* under the vars folder i have created diff stages functions so we can access them separately when ever we required in the pipeline.

* Each function has a clear responsibility. You can reuse it across multiple application pipelines.

Create multiple files under vars/
----------------------------------

* Your existing local repository can now have this structure:

      jenkins-shared-library/
      └── vars/
          ├── buildApp.groovy
          ├── runTests.groovy
          ├── buildDockerImage.groovy
          └── deployApp.groovy

#Remember the rule from Day 1:

    * The filename determines the step name.
    * For example, vars/buildApp.groovy exposes buildApp().

Example: buildApp.groovy
-------------------------

    def call(Map config) {
        def appName = config.appName?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        echo "Building application: ${appName}"
    }

* This uses the validation techniques you learned on Day 2.

Calling it:
buildApp(appName: 'Empower')


Output:
Building application: Empower


Example: runTests.groovy
-------------------------
Create vars/runTests.groovy:

    def call(Map config) {
        def appName = config.appName?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        echo "Running unit tests for ${appName}"
    }
    

Calling it:
runTests(appName: 'Empower')


Output:
Running unit tests for Empower


These examples simulate the operations using echo. In a real pipeline, the functions could execute Maven tests, npm tests, or other build commands.

Call multiple library functions from one Jenkinsfile
------------------------------------------------------

* Once both files exist in your configured Shared Library, your Jenkinsfile can call them in sequence:

      @Library('my-shared-library') _
      
      pipeline {
          agent any
      
          stages {
              stage('Build') {
                  steps {
                      buildApp(appName: 'Empower')
                  }
              }
      
              stage('Test') {
                  steps {
                      runTests(appName: 'Empower')
                  }
              }
          }
      }


Execution flow:

    Jenkinsfile
        ↓
    buildApp(appName: 'Empower')
        ↓
    runTests(appName: 'Empower')


Notice that the Jenkinsfile controls the stage order, while the Shared Library contains the reusable operation logic.


Important real-world distinction
---------------------------------

* A Shared Library function is not automatically a Jenkins stage.

For example:
buildApp(appName: 'Empower')
runTests(appName: 'Empower')


* These are function calls. They run wherever you call them.

* The Jenkinsfile defines the stages:

      stage('Build') {
          steps {
              buildApp(appName: 'Empower')
          }
      }


* This separation makes your pipelines easier to understand and maintain.

Today's practice
-----------------
Create one additional function in your existing local repository: vars/runTests.groovy.
Your function must:
1. Accept a Map config.
2. Validate appName using the techniques from Day 2.
3. Accept an optional testType, defaulting to unit.
4. Print the application name and test type.
5. Fail with a clear error if the application name is missing or empty.
Then update your test Jenkinsfile to call both buildApp() and runTests() in separate stages.
Send me your runTests.groovy code and the updated Jenkinsfile. I'll review them before we move forward.
Daily practice reminder: Your separate Advanced Pipeline Syntax real-world task is still ongoing. Once Shared Libraries is completed, I'll give you one final task using only the previously completed topics, excluding Shared Libraries.

deployApp()
Deployment logic
