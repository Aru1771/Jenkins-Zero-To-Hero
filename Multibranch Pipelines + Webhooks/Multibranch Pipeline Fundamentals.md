Today's topic: Multibranch Pipeline Fundamental
===============================================

Today's goal is to understand how Jenkins automatically creates and manages pipelines for multiple Git branches.

Part 1 — What is a Multibranch Pipeline?
-----------------------------------------

First, imagine you have a Git repository with three branches:

    empower-application/
    │
    ├── main
    ├── develop
    └── feature/login

Each branch has its own Jenkinsfile.

For example:

    - main → production pipeline
    - develop → development pipeline
    - feature/login → feature testing pipeline

* A normal Pipeline job is configured to use a particular branch or Jenkinsfile.

* A Multibranch Pipeline automatically discovers branches in a repository that contain a Jenkinsfile and creates a separate pipeline job for each discovered branch.

 <img width="1752" height="896" alt="image" src="https://github.com/user-attachments/assets/577f4d45-b0a8-464b-ac8f-e85fe2f0dcfd" />

Part 2 — How does it work?
---------------------------
Let's understand the workflow step by step.

<img width="971" height="370" alt="image" src="https://github.com/user-attachments/assets/7049a4bf-1c3c-453c-a668-4deff238f2aa" />

    Jenkins connects to your Git repository.
    Jenkins scans the repository for branches.
    It checks which branches contain a Jenkinsfile.
    It creates a separate pipeline job for each eligible branch.
    Each branch pipeline executes its own Jenkinsfile.

* Important: You don't need to create a separate Jenkins Pipeline job manually for every branch.

Part 3 — Important concepts
----------------------------
* Branch indexing

Branch indexing means Jenkins scans the Git repository to discover branches and check for changes.

For example:

    - You create a new feature/payment branch.
    - Jenkins performs branch indexing.
    - Jenkins discovers the new branch.
    - If it contains a valid Jenkinsfile, Jenkins creates its branch pipeline.

Jenkinsfile in each branch
Each branch can have its own Jenkinsfile.

Example: main branch

    pipeline {
        agent any
    
        stages {
            stage('Production Build') {
                steps {
                    echo 'Building for Production'
                }
            }
        }
    }

develop branch

    pipeline {
        agent any
    
        stages {
            stage('Development Build') {
                steps {
                    echo 'Building for Development'
                }
            }
        }
    }


You can also use the same Jenkinsfile in multiple branches and control behavior using BRANCH_NAME.

Example:

    stage('Deploy') {
        when {
            branch 'main'
        }
        steps {
            echo 'Deploying to Production'
        }
    }

This stage runs only for the main branch.


<img width="935" height="382" alt="image" src="https://github.com/user-attachments/assets/0fc26b65-2729-4a24-8c80-d0b77d904f99" />


Part 4 — Practical task: Understand the setup
---------------------------------------------

For today's lab, use your existing Jenkins environment and a Git repository.

Your task:


<img width="683" height="275" alt="image" src="https://github.com/user-attachments/assets/a191560e-4e20-4bcc-8155-031345289d46" />


<img width="1277" height="635" alt="image" src="https://github.com/user-attachments/assets/4a1503e1-58ff-4775-9c03-0abce8783555" />

Don't worry about webhooks today. We'll cover those later in Phase 3.

