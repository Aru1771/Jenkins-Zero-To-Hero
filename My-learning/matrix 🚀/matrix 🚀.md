1. What is matrix?

        matrix is used when you want to run the same set of stages/steps across multiple combinations of values.

* Imagine you have an application called Empower. You need to test it on:
        
        - Two Java versions: Java 17 and Java 21
        - Two environments: Dev and Stage
* Without a matrix, you might write four separate Jenkins stages or jobs.
* With a matrix, you define the combinations once, and Jenkins runs a separate execution for each combination.

Think of a matrix like an Excel table:

<img width="748" height="162" alt="image" src="https://github.com/user-attachments/assets/bd619247-679c-4406-a431-383a82578b13" />

*  gives you four combinations.

* The memory trick: Matrix = Dimensions × Combinations × Repeated execution.
        
          Dimensions are the things you want to vary, such as Java version or environment.
          Values are the choices within each dimension.
          Combinations are the executions Jenkins creates from those choices.

<img width="962" height="551" alt="image" src="https://github.com/user-attachments/assets/13c91232-8e2e-4ad4-871d-04be6d89ab5b" />


 * One important detail: JAVA_VERSION and ENVIRONMENT become environment variables within each matrix execution. You can reference them as ${JAVA_VERSION} and ${ENVIRONMENT} in a Groovy double-quoted string.

<img width="931" height="481" alt="image" src="https://github.com/user-attachments/assets/612001d1-14c2-4e30-8f2e-dc643bd0b63a" />

* If you had three axes with 2, 2, and 3 values, Jenkins would generate \(2 \times 2 \times 3 = 12\) combinations, unless you excluded combinations.

* Important: Jenkins can execute matrix combinations in parallel when its execution capacity and configuration permit it. Matrix does not guarantee that all combinations run simultaneously.
  
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
