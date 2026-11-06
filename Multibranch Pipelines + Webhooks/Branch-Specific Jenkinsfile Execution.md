Branch-Specific Jenkinsfile Execution:
=====================================

We'll learn:

    1. How Jenkins executes different Jenkinsfiles for different branches.
    2. How to use the BRANCH_NAME environment variable.
    3. How to configure branch-specific build and deployment logic.
    4. How to handle development, staging, and production pipelines.
    5. Practical multibranch pipeline implementation.

Why do we need branch-specific execution?:
------------------------------------------

Imagine your Empower application has three Git branches:

    
    empower-application/
    │
    ├── main
    ├── develop
    └── feature/login

<img width="841" height="422" alt="image" src="https://github.com/user-attachments/assets/c9647f66-e9b3-42f8-885d-6df9219387b0" />

What is BRANCH_NAME?:
---------------------

* In a Jenkins Multibranch Pipeline, Jenkins automatically provides the BRANCH_NAME environment variable.

* It identifies the branch associated with the current pipeline run.

<img width="977" height="722" alt="image" src="https://github.com/user-attachments/assets/139a1eda-80ad-4b88-89ad-d742acc6b19e" />

Control execution using when:
-----------------------------

Now let's say you want the deployment stage to run only for the main branch.

You can use the Declarative Pipeline when condition.

    pipeline {
        agent any
    
        stages {
            stage('Build') {
                steps {
                    echo "Building ${env.BRANCH_NAME}"
                }
            }
    
            stage('Test') {
                steps {
                    echo "Running tests"
                }
            }
    
            stage('Production Deployment') {
                when {
                    branch 'main'
                }
                steps {
                    echo "Deploying to Production"
                }
            }
        }
    }

<img width="980" height="310" alt="image" src="https://github.com/user-attachments/assets/42a400ae-1292-4aa6-af0d-151421818a9b" />

Different deployment environments:
----------------------------------

* Let's make this more realistic.
* Suppose your organization follows this deployment strategy:

      - main → Production
      - develop → Development
      - release/* → Staging
      - Other branches → Build and test only

You can use multiple when conditions:
    
    pipeline {
        agent any
    
        stages {
            stage('Build') {
                steps {
                    echo "Building ${env.BRANCH_NAME}"
                }
            }
    
            stage('Development Deployment') {
                when {
                    branch 'develop'
                }
                steps {
                    echo 'Deploying to Development'
                }
            }
    
            stage('Staging Deployment') {
                when {
                    branch pattern: 'release/*', comparator: 'GLOB'
                }
                steps {
                    echo 'Deploying to Staging'
                }
            }
    
            stage('Production Deployment') {
                when {
                    branch 'main'
                }
                steps {
                    echo 'Deploying to Production'
                }
            }
        }
    }

* Here, release/* matches branch names such as:

      - release/1.0
      - release/2.0
      - release/empower-v3
* The GLOB comparator uses wildcard matching.


BRANCH_NAME vs when { branch }:
------------------------------------

Both are useful, but they serve different purposes.

<img width="820" height="182" alt="image" src="https://github.com/user-attachments/assets/b74d6935-d9d4-4158-987b-4d715f1a611b" />

You can also use BRANCH_NAME with an expression:

    stage('Production Deployment') {
        when {
            expression {
                env.BRANCH_NAME == 'main'
            }
        }
        steps {
            echo 'Deploying to Production'
        }
    }

For a simple branch check, prefer when { branch 'main' }. Use expression when you need more complex logic.

Real-world tip: Branch-based deployment conditions are not a replacement for deployment approvals or access controls. 
Protect production deployments with appropriate Jenkins permissions and credential restrictions.

Important: Same Jenkinsfile or different Jenkinsfiles?:
-------------------------------------------------------
<img width="923" height="587" alt="image" src="https://github.com/user-attachments/assets/23fdbb34-41f4-4588-a34b-be4c6ca80222" />

<img width="977" height="802" alt="image" src="https://github.com/user-attachments/assets/82931867-0a82-42c3-827e-1308128100c8" />

<img width="652" height="313" alt="image" src="https://github.com/user-attachments/assets/3311da6d-d4ec-461f-b243-c448bd98d496" />


Daily revision tasks:

Phase 1 — Advanced Pipeline Syntax
Revision task 1

Create a pipeline with:
- String parameter APP_VERSION, default 1.0.
- Boolean parameter DEPLOY, default false.
- Build stage.
- Test stage.
- Deployment stage that runs only when both conditions are true:
  - DEPLOY == true
  - Current branch is main.
- Use post to print the pipeline result.



Phase 2 — Shared Libraries
Revision task 2

Create a reusable deployApp function in vars/deployApp.groovy.

Requirements:
- Accept app_name, version, and environment through Map config.
- Validate all three values.
- Set default version to 1.0 if not supplied.
- Print the deployment details.
- Call it twice from Jenkinsfile:
  - Empower — Production
  - NuGenesis — Development
