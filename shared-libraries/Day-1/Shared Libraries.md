What problem does Shared Library solve?
----------------------------------------
Imagine your company has 20 applications.

Every Jenkinsfile contains:

    Checkout
    ↓
    Build
    ↓
    Unit Test
    ↓
    SonarQube
    ↓
    Docker Build
    ↓
    Trivy
    ↓
    Docker Push
    ↓
    Deployment

Without a Shared Library:

    App A → huge Jenkinsfile
    App B → huge Jenkinsfile
    App C → huge Jenkinsfile
    ...
    App T → huge Jenkinsfile

If you change the Docker or SonarQube logic, you may need to modify 20 Jenkinsfiles.

With a Shared Library:

                  Shared Library
                       │
          ┌────────────┼────────────┐
          ↓            ↓            ↓
       App A         App B         App C
     Jenkinsfile   Jenkinsfile   Jenkinsfile

The common logic is maintained once.



What is a Shared Library?
-------------------------

A Jenkins Shared Library is basically a Git repository containing reusable Jenkins/Groovy code.

For example:

    jenkins-shared-library/
    │
    ├── vars/
    │   ├── buildApp.groovy
    │   ├── dockerBuild.groovy
    │   └── deployApp.groovy
    │
    ├── src/
    │   └── ...
    │
    └── resources/
        └── ...

Your application Jenkinsfile can then reuse those functions.


The three important directories
-------------------------------

Initially, focus on these:

    vars/
    src/
    resources/

vars/

    Used for reusable pipeline steps/functions.

Example:

    vars/
    └── buildApp.groovy

You could call:

    buildApp()


src/

    Used for more structured/reusable Groovy classes and logic.

We'll learn this later.

For now:

    vars = simple reusable pipeline functions.

resources/

    Used for supporting files/resources that your shared library needs.

We'll cover this later.



Basic Shared Library example
-----------------------------

Suppose your Git repository contains:

    jenkins-shared-library/
    └── vars/
        └── sayHello.groovy

vars/sayHello.groovy:

    def call() {
        echo "Hello from Jenkins Shared Library"
    }

Then your application Jenkinsfile can use:

    @Library('my-shared-library') _
    
    pipeline {
        agent any
    
        stages {
            stage('Test') {
                steps {
                    sayHello()
                }
            }
        }
    }

The important part is:

    @Library('my-shared-library') _

This tells Jenkins:

    "Load this Shared Library."

Then:

    sayHello()

calls the function from:

    vars/sayHello.groovy



Why is the filename important?
------------------------------

This is an important rule.

If you have:

    vars/buildApp.groovy

you normally call:

    buildApp()

If you have:

    vars/deployApp.groovy

you call:

    deployApp()

So:

    vars/buildApp.groovy
            ↓
        buildApp()

Think of vars as:

    Filename = reusable pipeline step name

Passing parameters
------------------

We don't want this:

    def call() {
        echo "Building Empower"
    }

because it is specific to Empower.

Instead:

    def call(String appName) {
        echo "Building ${appName}"
    }

Now:

    buildApp('Empower')

produces:

    Building Empower

And:

    buildApp('NuGenesis')

produces:

    Building NuGenesis

Now the same function can support multiple applications.



Multiple parameters
-------------------

You can also use a map:

    def call(Map config) {
        echo "Application: ${config.appName}"
        echo "Version: ${config.version}"
        echo "Environment: ${config.environment}"
    }

Then:

    buildApp(
        appName: 'Empower',
        version: '1.2.0',
        environment: 'dev'
    )

This is very common in real Jenkins Shared Libraries because it is easier to extend later.



Real-world architecture
------------------------

Eventually your company might have:

    GitHub
    │
    ├── jenkins-shared-library
    │   ├── vars/
    │   │   ├── buildApp.groovy
    │   │   ├── dockerBuild.groovy
    │   │   ├── securityScan.groovy
    │   │   └── deployApp.groovy
    │   │
    │   ├── src/
    │   └── resources/
    │
    ├── empower
    │   └── Jenkinsfile
    │
    ├── nugenesis
    │   └── Jenkinsfile
    │
    └── labmonitor
        └── Jenkinsfile

The application Jenkinsfile becomes much smaller:

@Library('company-shared-library') _

    pipeline {
        agent any
    
        stages {
            stage('Build') {
                steps {
                    buildApp(...)
                }
            }
    
            stage('Docker') {
                steps {
                    dockerBuild(...)
                }
            }
    
            stage('Deploy') {
                steps {
                    deployApp(...)
                }
            }
        }
    }

This is one of the major reasons companies use Shared Libraries.


Today's learning path
--------------------

We will learn Shared Libraries in this order:

    1. Shared Library concept          ← TODAY
           ↓
    2. vars/ directory
           ↓
    3. call() method
           ↓
    4. Parameters
           ↓
    5. Using Shared Library in Jenkinsfile
           ↓
    6. Global Library configuration
           ↓
    7. Git-based Shared Library
           ↓
    8. src/ directory
           ↓
    9. resources/
           ↓
    10. Production-style Shared Library

For now, don't worry about src, resources, or advanced library design.

Your first small practice

Create mentally/locally:

    jenkins-shared-library/
    └── vars/
        └── hello.groovy

Make hello.groovy print:

    Hello from Empower Team Shared Library

Then create a Jenkinsfile that loads the library and calls:

    hello()


1. Understand the execution flow
---------------------------------
Imagine your team has a reusable function called buildApp.
        
        Application Jenkinsfile
        
        Calls buildApp('Empower')
                 |
                 
        Jenkins Shared Library
        
        vars/buildApp.groovy
                 |
                 
        Reusable pipeline logic executes
        
        Prints the application name, builds, or performs other configured steps.

The Jenkinsfile calls the function. Jenkins loads the library, finds the matching file under vars/, and executes its code.


What is call()?
---------------

Consider this file:

 vars/buildApp.groovy

        def call(String appName) {
        echo "Building application: ${appName}"
    }

The call() method makes the library step callable by its filename.


Because the filename is buildApp.groovy, you can call it like this:

    buildApp('Empower')

Output:

    Building application: Empower

You don't need to write buildApp.call('Empower') in your Jenkinsfile. Jenkins/Groovy lets you invoke the step using the shorter syntax.


Passing multiple values
------------------------

In real projects, you usually need the application name, version, and target environment.

vars/buildApp.groovy

    def call(Map config) {
        echo "Application: ${config.appName}"
        echo "Version: ${config.version}"
        echo "Environment: ${config.environment}"
    }


Call it from the Jenkinsfile:

    buildApp(
        appName: 'Empower',
        version: '1.2.0',
        environment: 'dev'
    )

Expected output:

    Application: Empower
    Version: 1.2.0
    Environment: dev

Why use Map config? You can pass multiple named values without creating a separate method parameter for every value. It also makes the function easier to extend later.

One important distinction: this example explains the library function itself. To execute it in Jenkins, the Shared Library must also be stored in a repository and configured or loaded by Jenkins.

Today's practice — Shared Libraries only
-----------------------------------------

Create a file named vars/deployApp.groovy.

Your function must:

Accept a Map config.

Print the application name.

Print the version.

Print the target environment.

Print a deployment message using all three values.

Then show me how you would call it with:

Application: Empower

Version: 2.0.0

Environment: stage
