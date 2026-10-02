1. What is matrix?

        matrix is used when you want to run the same set of stages/steps across multiple combinations of values.

Think of it like a testing grid.

For example, suppose you want to test Empower in:

          dev
          stage
          prod
          
          and against:
          
          Java 17
          Java 21

A matrix can create all combinations:

                 Java 17       Java 21
               ┌───────────┬───────────┐
    dev        │   Test    │   Test    │
               ├───────────┼───────────┤
    stage      │   Test    │   Test    │
               ├───────────┼───────────┤
    prod       │   Test    │   Test    │
               └───────────┴───────────┘

That's 6 combinations.

parallel vs matrix
-------------------

This is very important.

parallel

You manually define different branches:

    parallel {
        stage('Unit Test') { ... }
        stage('SonarQube') { ... }
        stage('Trivy') { ... }
    }

The tasks are different:

    Unit Test
    SonarQube
    Trivy
    
matrix
------

You define dimensions, and Jenkins generates combinations.

    matrix {
        axes {
            axis {
                name 'ENVIRONMENT'
                values 'dev', 'stage'
            }
    
            axis {
                name 'VERSION'
                values 'v1', 'v2'
            }
        }
    }

Jenkins generates:

    dev   + v1
    dev   + v2
    stage + v1
    stage + v2

So:

Parallel = different tasks running at the same time

Matrix = same logic running across different combinations

Basic matrix syntax
---------------------

Here is a simple example:

    stage('Testing') {
    
        matrix {
    
            axes {
    
                axis {
                    name 'ENVIRONMENT'
                    values 'dev', 'stage'
                }
    
                axis {
                    name 'VERSION'
                    values 'v1', 'v2'
                }
            }
    
            stages {
    
                stage('Test') {
                    steps {
                        echo "Testing ${ENVIRONMENT} - ${VERSION}"
                    }
                }
            }
        }
    }

Jenkins can generate:

    Testing dev-v1
    Testing dev-v2
    Testing stage-v1
    Testing stage-v2

The important pieces are:

    matrix
      │
      ├── axes
      │     ├── ENVIRONMENT
      │     └── VERSION
      │
      └── stages
            └── Test

What is an Axis?
----------------

An axis is simply a dimension/variable containing multiple values.

For example:

    axis {
        name 'ENVIRONMENT'
        values 'dev', 'stage', 'prod'
    }

This gives:

    ENVIRONMENT
        ├── dev
        ├── stage
        └── prod

Another axis:

    axis {
        name 'JAVA_VERSION'
        values '17', '21'
    }

gives:

    JAVA_VERSION
        ├── 17
        └── 21

Together Jenkins creates the combinations.

Real-world DevOps example
---------------------------

Imagine your application needs testing across:

    Environment:
    dev
    stage
    
    Application version:
    1.0
    2.0

Matrix:

                     Version
                  1.0       2.0
               ┌─────────┬─────────┐
    dev        │ dev-1.0 │ dev-2.0 │
               ├─────────┼─────────┤
    stage      │stage-1.0│stage-2.0│
               └─────────┴─────────┘

That's 4 combinations.

And because matrix cells can execute concurrently, this is very useful for CI testing.

Your first practical task 🔥
-----------------------------

Let's keep it simple first.

Create an Empower testing matrix with two axes.

    Axis 1
    ENVIRONMENT
    dev
    stage
    Axis 2
    VERSION
    1.0
    2.0

Your matrix should produce:

    Testing Empower - dev - 1.0
    Testing Empower - dev - 2.0
    Testing Empower - stage - 1.0
    Testing Empower - stage - 2.0
Pipeline structure

Build:

    Build
      ↓
    Matrix Testing
      ├── dev + 1.0
      ├── dev + 2.0
      ├── stage + 1.0
      └── stage + 2.0
      ↓
    Deployment

Skeleton
----------

Write the missing parts yourself:

        pipeline {
            agent any
        
            stages {
        
                stage('Build') {
                    steps {
                        echo 'Building Empower...'
                    }
                }
        
                stage('Matrix Testing') {
        
                    matrix {
        
                        axes {
        
                            // Axis 1
        
                            // Axis 2
                        }
        
                        stages {
        
                            stage('Test') {
                                steps {
                                    // Print environment and version
                                }
                            }
                        }
                    }
                }
        
                stage('Deployment') {
                    steps {
                        echo 'Deploying Empower...'
                    }
                }
            }
        }
