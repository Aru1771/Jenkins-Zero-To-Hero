<img width="972" height="526" alt="image" src="https://github.com/user-attachments/assets/2da6e5ac-8266-4f32-aee4-859353652365" />Pull Request (PR) Pipeline Integration:
=======================================

We'll learn:

    - What is a Pull Request (PR)?
    - How Jenkins discovers pull requests.
    - How PR validation pipelines work.
    - Testing code before merging into main.
    - Practical GitHub + Jenkins integration.

Part 1 — What is a Pull Request?
---------------------------------

Imagine your team is developing the Empower application.

Your Git repository has these branches:

      main
        |
        |---- develop
                 |
                 |---- feature/login

* A developer creates a new feature in feature/login.
* After completing the code, the developer wants to merge it into develop.
* Before merging, the developer creates a Pull Request (PR).
* A Pull Request is a request to review and merge changes from one branch into another.

<img width="975" height="356" alt="image" src="https://github.com/user-attachments/assets/a03c2299-6d81-43ad-86a0-d03fa4262533" />


Part 2 — Why do we need PR pipelines?
-------------------------------------

Consider this scenario:

    1. Developer A writes login functionality.
    2. Developer A creates a Pull Request.
    3. Jenkins automatically starts a pipeline.
    4. Jenkins builds the application.
    5. Jenkins runs unit tests.
    6. Jenkins performs SonarQube analysis.
    7. Jenkins reports whether validation passed or failed.

<img width="936" height="662" alt="image" src="https://github.com/user-attachments/assets/8401a31b-6061-496b-929e-e1392978e1e1" />

Part 3 — How does Jenkins discover Pull Requests?
-------------------------------------------------

* For this, Jenkins uses a Git provider integration.
* For example, with GitHub, Jenkins commonly uses the **GitHub Branch Source plugin**.

In your Multibranch Pipeline configuration, you configure:

      - Repository owner.
      - Repository name.
      - GitHub credentials (if required).
      - Branch discovery.
      - Pull Request discovery.
* The plugin allows Jenkins to discover both normal branches and Pull Requests.

<img width="1200" height="727" alt="image" src="https://github.com/user-attachments/assets/c42c7fdd-284d-4e4e-9ecc-cbd7c6fb692b" />

Example repository

Suppose your GitHub repository contains:

    empower-application/
    │
    ├── main
    ├── develop
    ├── feature/login
    │
    └── Pull Requests
        ├── PR-10: feature/login → develop
        └── PR-11: feature/payment → main


* Jenkins can discover the branches and the Pull Requests.

* It creates separate jobs for discovered PRs, such as:

      empower-application/
      │
      ├── main
      ├── develop
      ├── feature-login
      │
      ├── PR-10
      └── PR-11

The exact job names and structure depend on the Git provider plugin.

Part 4 — How does Jenkins build a Pull Request?
-----------------------------------------------

This is an important concept.

Jenkins can build a PR in two common ways.

<img width="915" height="472" alt="image" src="https://github.com/user-attachments/assets/a0cb67e7-3f7a-47f6-af08-524a63abfbd4" />

<img width="1043" height="542" alt="image" src="https://github.com/user-attachments/assets/377a6e0d-6ee8-4b7a-bd8d-f049cfb97a50" />

<img width="992" height="671" alt="image" src="https://github.com/user-attachments/assets/6f01cd4a-c8c6-4fdd-be78-22416bc0d678" />

You can use these variables in your Jenkinsfile:

    pipeline {
        agent any
    
        stages {
            stage('PR Information') {
                steps {
                    echo "PR ID: ${env.CHANGE_ID}"
                    echo "Source: ${env.CHANGE_BRANCH}"
                    echo "Target: ${env.CHANGE_TARGET}"
                }
            }
        }
    }


Remember the difference:

    - BRANCH_NAME identifies the current Multibranch job's branch or PR.
    - CHANGE_ID identifies the Pull Request.
    - CHANGE_BRANCH identifies the PR source branch.
    - CHANGE_TARGET identifies the PR target branch.

Part 6 — PR-specific pipeline execution
----------------------------------------
Suppose you want a stage to run only for Pull Requests.

Use:

    stage('PR Validation') {
        when {
            changeRequest()
        }
        steps {
            echo 'Running Pull Request validation'
        }
    }


Suppose you want to validate only PRs targeting develop:

    stage('Develop PR Validation') {
        when {
            changeRequest target: 'develop'
        }
        steps {
            echo 'Validating PR for develop'
        }
    }

You can combine this with your previously learned when, expression, parallel, and post concepts.

<img width="972" height="526" alt="image" src="https://github.com/user-attachments/assets/cb806624-ecbc-4bbb-832c-071723b85b2e" />


<img width="1013" height="760" alt="image" src="https://github.com/user-attachments/assets/073f5882-5447-41c7-8248-7c1ba6d65e3d" />

<img width="992" height="677" alt="image" src="https://github.com/user-attachments/assets/de52f1b5-4584-4ba5-8415-a19d5e46e859" />

Part 9 — Daily revision tasks
As agreed, we continue revising Phase 1 and Phase 2 every day.

Phase 1 — Advanced Pipeline Syntax
Revision task 1

Build a Jenkins pipeline with:
- String parameter APP_VERSION, default 1.0.
- Boolean parameter RUN_TESTS, default true.
- Build stage.
- Test stage that runs only when RUN_TESTS == true.
- Parallel execution of Unit Tests and Security Scan.
- post section to print the final build status.


Phase 2 — Shared Libraries
Revision task 2

Create a reusable function named runTests in:

vars/runTests.groovy

Requirements:
- Accept Map config.
- Accept app_name and test_type.
- Default test_type to unit.
- Validate app_name.
- Support unit and integration test types.
- Print which application's tests are running.
- Call it from your Jenkinsfile for Empower and NuGenesis.
