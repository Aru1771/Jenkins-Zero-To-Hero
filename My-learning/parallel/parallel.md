What is parallel?
-----------------

Normally Jenkins executes stages one after another:

    Build
      ↓
    Unit Test
      ↓
    Security Scan
      ↓
    Deploy

With parallel, independent tasks can run at the same time:

                 ┌── Unit Test ──────┐
                 │                   │
    Build ───────┼── Security Scan ──┼──→ Deploy
                 │                   │
                 └── Integration ───┘

This can reduce pipeline execution time when the tasks don't depend on each other.

Basic syntax
--------------

Declarative Pipeline:

    stage('Testing') {
        parallel {
    
            stage('Unit Test') {
                steps {
                    echo 'Running Unit Tests...'
                }
            }
    
            stage('Security Scan') {
                steps {
                    echo 'Running Security Scan...'
                }
            }
        }
    }

Jenkins starts both branches:

    Testing
       │
       ├── Unit Test
       │
       └── Security Scan

They execute independently.

Real-world DevOps example
--------------------------

Suppose your Empower application has:

    Build
      ↓
    Testing
      ├── Unit Tests
      ├── SonarQube
      └── Trivy Scan
      ↓
    Deployment

These three checks may be independent, so instead of:

    Unit Test       5 min
          ↓
    SonarQube       3 min
          ↓
    Trivy           2 min
    ---------------------
    Total           10 min

you can run them in parallel:

                 ┌── Unit Test ── 5 min ──┐
                 │                         │
    Testing ─────┼── SonarQube ─ 3 min ───┼──→ Deployment
                 │                         │
                 └── Trivy ───── 2 min ───┘

The parallel section finishes when the required branches finish, so the elapsed time can be closer to the longest branch rather than the sum.

Important rule
---------------

Only put tasks in parallel when they don't depend on each other's output.

    Good candidates
    Unit Test
    SonarQube
    Trivy

because they can independently inspect the application.

    Bad example
    Build
      ↓
    Deploy

You shouldn't run these in parallel because deployment depends on the build artifact.

Your first practical task 🔥

Create this pipeline yourself:

    Build
      ↓
    Parallel Testing
       ├── Unit Test
       ├── Security Scan
       └── Integration Test
      ↓
    Deployment

Expected messages:

    Build
    Building Empower...
    Unit Test
    Running Empower Unit Tests...
    Security Scan
    Running Empower Security Scan...
    Integration Test
    Running Empower Integration Tests...
    Deployment
    Deploying Empower...
    Skeleton

Start with:

      pipeline {
          agent any
      
          stages {
      
              stage('Build') {
                  steps {
                      // your code
                  }
              }
      
              stage('Testing') {
                  parallel {
      
                      stage('Unit Test') {
                          steps {
                              // your code
                          }
                      }
      
                      stage('Security Scan') {
                          steps {
                              // your code
                          }
                      }
      
                      stage('Integration Test') {
                          steps {
                              // your code
                          }
                      }
                  }
              }
      
              stage('Deployment') {
                  steps {
                      // your code
                  }
              }
          }
      }
      
One thing to observe

Use echo messages first. Don't add sleep yet.

Once you run it, look at the Jenkins Stage View and verify that:

    Unit Test
    Security Scan
    Integration Test

appear as parallel branches under Testing.

Write the complete Jenkinsfile yourself and send it to me. I'll review it before we move to the next parallel exercise.


Parallel failure
----------------

Change only the Trivy branch:
        
        stage('Trivy scan') {
            steps {
                echo 'Trivy scanning....'
                sleep 5
                error 'Trivy found a critical vulnerability'
            }
        }

Keep Unit Test and SonarQube successful.

So we'll have:

        Unit Test       ✅
        SonarQube       ✅
        Trivy           ❌
                        ↓
                  Parallel block ❌
                        ↓
                  Deployment ❌
                        ↓
                  Pipeline ❌


Fail happen
------------

        stage('Testing') {
                    parallel {
                        stage('unit test') {
                            steps {
                                echo "Unit Testing...."
                                sleep 10
                                echo "Unit Test Completed"
                            }
                        }
                        stage('SonarQubeAnalisys') {
                            steps {
                                echo "SonarQubeAnalisys....."
                                sleep 10
                                echo "SQ Analisys Completed"
                            }
                        }
                        stage('Trivy scan') {
                            steps{
                                echo "Trivy scanning...."
                                sleep 10
                                error "Trivy Scan failed"
                            }
                        }
                    }
                }
