parameters
===========

First understand the goal
-------------------------
In a basic Jenkins pipeline, you might have:

    pipeline {
        agent any
    
        stages {
            stage('Build') {
                steps {
                    echo 'Building application...'
                }
            }
        }
    }


The problem is that the pipeline is fixed.

For example, suppose you want to deploy the same application to:

    DEV
    QA
    PROD


Instead of modifying the Jenkinsfile every time, we can allow the person starting the build to select the environment.

That's where parameters come in.

What are Jenkins Parameters?
----------------------------

A parameter is an input given when starting a Jenkins build.

For example:

Build with Parameters
    
    ENVIRONMENT: [DEV ▼]
    VERSION:     1.0.5
    DEPLOY:      ☑

The user provides these values, and the pipeline can use them.

Think of it like a function:

    pipeline(environment, version, deploy)

Jenkins receives those values when the build starts.

Basic parameter syntax
-----------------------

Example:

    pipeline {
        agent any
    
        parameters {
            string(
                name: 'APP_VERSION',
                defaultValue: '1.0.0',
                description: 'Application version'
            )
    
            choice(
                name: 'ENVIRONMENT',
                choices: ['DEV', 'QA', 'PROD'],
                description: 'Select deployment environment'
            )
    
            booleanParam(
                name: 'DEPLOY',
                defaultValue: false,
                description: 'Deploy the application?'
            )
        }
    
        stages {
            stage('Build') {
                steps {
                    echo "Building version ${params.APP_VERSION}"
                }
            }
        }
    }

There are three parameters here.

string

    string(
        name: 'APP_VERSION',
        defaultValue: '1.0.0'
    )

User enters:

    1.0.5

Access it using:

    params.APP_VERSION


choice

    choice(
        name: 'ENVIRONMENT',
        choices: ['DEV', 'QA', 'PROD']
    )

Jenkins displays a dropdown:

    ENVIRONMENT
       ↓
    [ DEV ▼ ]

Access it using:

    params.ENVIRONMENT


booleanParam

    booleanParam(
        name: 'DEPLOY',
        defaultValue: false
    )

Jenkins displays a checkbox:

    ☐ Deploy

Access it using:

    params.DEPLOY

The value will be:

    true

or:

    false




Very important: params
-----------------------
This is something you should remember.

When you define:

    parameters {
        string(name: 'APP_VERSION', defaultValue: '1.0.0')
    }

you access the value using:

    params.APP_VERSION

Not:

    APP_VERSION

So:

    echo "Version is ${params.APP_VERSION}"

Parameters vs Environment
--------------------------
This distinction is very important.

    | Parameters               | Environment                        |
    | ------------------------ | ---------------------------------- |
    | User provides input      | Pipeline provides/configures value |
    | Used to customize builds | Used to make values available      |
    | `params.NAME`            | `NAME`                             |
    | Example: DEV/QA/PROD     | Example: application name          |
    | Example: version         | Example: registry URL              |
    
Think:

      PARAMETER
          ↓
      User input
          ↓
      Pipeline
      
      ENVIRONMENT
          ↓
      Pipeline configuration
          ↓
      Stages/steps
Combine both

Now let's create a realistic example.

pipeline {
    agent any

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['DEV', 'QA', 'PROD'],
            description: 'Select deployment environment'
        )

        string(
            name: 'APP_VERSION',
            defaultValue: '1.0.0',
            description: 'Application version'
        )

        booleanParam(
            name: 'DEPLOY',
            defaultValue: false,
            description: 'Deploy the application?'
        )
    }

    environment {
        APP_NAME = 'my-application'
        DOCKER_REGISTRY = 'docker.io/mycompany'
    }

    stages {

        stage('Build') {
            steps {
                echo "Application: ${APP_NAME}"
                echo "Version: ${params.APP_VERSION}"
                echo "Environment: ${params.ENVIRONMENT}"
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying ${APP_NAME}"
                echo "Version: ${params.APP_VERSION}"
                echo "Environment: ${params.ENVIRONMENT}"
            }
        }
    }
}


Combine both
------------
Now let's create a realistic example.

      pipeline {
          agent any
      
          parameters {
              choice(
                  name: 'ENVIRONMENT',
                  choices: ['DEV', 'QA', 'PROD'],
                  description: 'Select deployment environment'
              )
      
              string(
                  name: 'APP_VERSION',
                  defaultValue: '1.0.0',
                  description: 'Application version'
              )
      
              booleanParam(
                  name: 'DEPLOY',
                  defaultValue: false,
                  description: 'Deploy the application?'
              )
          }
      
          environment {
              APP_NAME = 'my-application'
              DOCKER_REGISTRY = 'docker.io/mycompany'
          }
      
          stages {
      
              stage('Build') {
                  steps {
                      echo "Application: ${APP_NAME}"
                      echo "Version: ${params.APP_VERSION}"
                      echo "Environment: ${params.ENVIRONMENT}"
                  }
              }
      
              stage('Deploy') {
                  steps {
                      echo "Deploying ${APP_NAME}"
                      echo "Version: ${params.APP_VERSION}"
                      echo "Environment: ${params.ENVIRONMENT}"
                  }
              }
          }
      }

What happens when you click Build with Parameters?
--------------------------------------------------
Jenkins will show something like:

    ENVIRONMENT:  [DEV ▼]
    
    APP_VERSION:  1.0.0
    
    DEPLOY:       ☐

Suppose you select:

    ENVIRONMENT = QA
    APP_VERSION = 2.5.0
    DEPLOY      = true

Then Jenkins receives:

    params.ENVIRONMENT = QA
    params.APP_VERSION = 2.5.0
    params.DEPLOY      = true

And the environment contains:

    APP_NAME = my-application
    DOCKER_REGISTRY = docker.io/mycompany

One important Jenkins concept
------------------------------
There are two different ways of using environment variables.

Pipeline-level

    pipeline {
        environment {
            APP_NAME = 'my-app'
        }
    }

Available throughout the pipeline.

    Build
     ↓
    Test
     ↓
    Deploy

All stages can use:

    APP_NAME


Stage-level

You can also define:

    stage('Build') {
    
        environment {
            BUILD_TYPE = 'release'
        }
    
        steps {
            echo "${BUILD_TYPE}"
        }
    }

Here BUILD_TYPE is specific to that stage.

Think:

    Pipeline environment
            ↓
    Available everywhere
    
    Stage environment
            ↓
    Available only in that stage

🧠 Your first practical task
------------------------------
Don't copy my complete example.

Create your own Jenkinsfile with:

Parameters

Create:

ENVIRONMENT
APP_VERSION
DEPLOY

Requirements:

ENVIRONMENT → DEV / QA / PROD
APP_VERSION  → default 1.0.0
DEPLOY       → default false
Environment

Create:

APP_NAME
TEAM_NAME

Use your own values.

Stage

Create:

Build

and print:

Application name
Environment
Version
Deploy value
Team name

For example, the output should look approximately like:

Application: my-app
Environment: QA
Version: 2.0.0
Deploy: true
Team: DevOps
