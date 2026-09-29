Why do we need retry?
---------------------

In real CI/CD, some operations can fail temporarily.

For example:

    Jenkins
       ↓
    Docker Registry
       ↓
    Network problem
       ↓
    Push failed

Or:

    Jenkins
       ↓
    Kubernetes
       ↓
    Temporary API/network problem
       ↓
    Deployment failed

You don't necessarily want the entire pipeline to fail immediately.

You can tell Jenkins:

    "Try this operation again if it fails."

That's what retry does.

Basic retry
------------

Syntax:

    retry(3) {
        // steps
    }


Example:

    stage('Deploy') {
        steps {
            retry(3) {
                echo 'Deploying application...'
            }
        }
    }

This means Jenkins can execute the block up to 3 attempts.

Think:

    Attempt 1
       ↓
    Failed
       ↓
    Attempt 2
       ↓
    Failed
       ↓
    Attempt 3
       ↓
    Success

If all attempts fail:

    Attempt 1 → FAIL
    Attempt 2 → FAIL
    Attempt 3 → FAIL
                 ↓
            Stage FAILS

Important: retry(3) means 3 attempts
--------------------------------------

This is important.

When you write:

    retry(3)

it means:

    Maximum attempts = 3

It does not mean:

    1 initial attempt + 3 retries = 4 attempts

So:

    retry(3)

means:

    Attempt 1
    Attempt 2
    Attempt 3

Real-world example
---------------------

Suppose you're pushing a Docker image:

    stage('Docker Push') {
        steps {
            retry(3) {
                sh 'docker push Aru1771/Empower3:1.0.2'
            }
        }
    }

If the first push fails because of a temporary network issue:

    Docker Push
        ↓
    Attempt 1 → FAIL
        ↓
    Attempt 2 → SUCCESS

The pipeline continues.

This is useful for operations that can fail temporarily.


What should NOT blindly use retry?
----------------------------------

Don't assume retry fixes every failure.

For example:

    Compilation error
    Syntax error
    Invalid Dockerfile
    Wrong Terraform configuration
    Wrong Kubernetes YAML

Retrying won't fix these.

For example:

    Terraform configuration error
            ↓
    Retry
            ↓
    Same error
            ↓
    Retry
            ↓
    Same error

So retry is most useful for transient failures, such as:

    temporary network problems
    temporary service/API failures
    transient infrastructure issues

Now let's learn post
---------------------

post allows Jenkins to perform actions after a stage or pipeline completes.

Think:

    Stage
      ↓
    Result
      ↓
    post
      ↓
    What should happen afterward?

Example:

    post {
        always {
            echo 'This always runs'
        }
    }

post conditions
----------------

The most important ones are:

      always
      success
      failure
      unstable
      cleanup

We'll understand each.


always
-------

    post {
        always {
            echo 'Pipeline completed'
        }
    }

always means:

    Run this regardless of whether the pipeline succeeds or fails.

Example:

    Build → SUCCESS
       ↓
    always → RUN

or:

    Build → FAILURE
       ↓
    always → RUN

A common use:

    post {
        always {
            cleanWs()
        }
    }

This means:

    Always clean the Jenkins workspace after the pipeline.

success
-------

    post {
        success {
            echo 'Pipeline completed successfully'
        }
    }

This runs only when the relevant pipeline/stage is successful.

    SUCCESS
       ↓
    success
       ↓
    RUN

If:

    FAILURE
       ↓
    success
       ↓
    SKIP


failure
--------

    post {
        failure {
            echo 'Pipeline failed'
        }
    }

This executes when the pipeline/stage fails.

Example:

    Build
     ↓
    Test
     ↓
    FAIL
     ↓
    failure
     ↓
    Notify team

A real-world use could be:

    post {
        failure {
            echo 'Send failure notification'
        }
    }


unstable
--------

Jenkins can have a result called:

UNSTABLE

It's different from:

    SUCCESS

and:

    FAILURE

For example, a test stage might complete but have test failures or warnings depending on how the pipeline is configured.

You can handle it:

    post {
        unstable {
            echo 'Pipeline is unstable'
        }
    }

Think:

SUCCESS  → Everything good
UNSTABLE → Completed, but problems/warnings
FAILURE  → Failed

cleanup
--------

cleanup is generally used for final cleanup work.

Example:

    post {
        cleanup {
            cleanWs()
        }
    }

It runs after the other post conditions have been processed.

A common purpose:
    
    Temporary files
    Docker files
    Test artifacts
    Workspace
           ↓
    Cleanup


Pipeline-level post

You can put post at the pipeline level:

    pipeline {
        agent any
    
        stages {
            ...
        }
    
        post {
            always {
                echo 'Pipeline finished'
            }
    
            success {
                echo 'Pipeline successful'
            }
    
            failure {
                echo 'Pipeline failed'
            }
        }
    }

This is extremely common.

Think:

                 Pipeline
                    ↓
        ┌───────────┴───────────┐
        ↓                       ↓
     SUCCESS                 FAILURE
        ↓                       ↓
     success                 failure
        \                       /
         \                     /
          └────── always ─────┘

Stage-level post
-----------------
You can also put post inside a stage.

Example:
    
    stage('Test') {
        steps {
            echo 'Running tests...'
        }
    
        post {
            success {
                echo 'Tests passed'
            }
    
            failure {
                echo 'Tests failed'
            }
        }
    }

Here the post belongs specifically to the Test stage.

Pipeline-level vs Stage-level post
-----------------------------------
    
    Pipeline-level
    pipeline {
        ...
    
        post {
            always {
                ...
            }
        }
    }

It handles the overall pipeline result.

    Stage-level
    stage('Test') {
        ...
    
        post {
            always {
                ...
            }
        }
    }

It handles that particular stage.

Think:
    
    Pipeline
    │
    ├── Build
    │
    ├── Test
    │    └── stage post
    │
    ├── Deploy
    │
    └── pipeline post

Combine retry + post
-----------------------

Now we can create a realistic pipeline.

    pipeline {
        agent any
    
        stages {
    
            stage('Build') {
                steps {
                    echo 'Building Empower...'
                }
            }
    
            stage('Deploy') {
                steps {
                    retry(3) {
                        echo 'Attempting deployment...'
                        // deployment command
                    }
                }
            }
        }
    
        post {
            always {
                echo 'Pipeline completed'
            }
    
            success {
                echo 'Empower deployment successful'
            }
    
            failure {
                echo 'Empower deployment failed'
            }
    
            cleanup {
                echo 'Cleaning workspace'
            }
        }
    }

The flow becomes:

    Build
     ↓
    Deploy
     ↓
    Attempt 1
     ↓
    FAIL
     ↓
    Attempt 2
     ↓
    FAIL
     ↓
    Attempt 3
     ↓
    SUCCESS
     ↓
    success
     ↓
    always
     ↓
    cleanup

If all three attempts fail:
    
    Attempt 1 → FAIL
    Attempt 2 → FAIL
    Attempt 3 → FAIL
                  ↓
               failure
                  ↓
               always
                  ↓
               cleanup

🧠 Important difference
-----------------------------
Remember:

retry

Controls what happens when something fails:

    Failure
       ↓
    Try again
    
post

Controls what happens after the stage/pipeline finishes:

    Result
     ↓
    success / failure / always / cleanup

So:

    retry = recovery attempt
    
    post = final action
