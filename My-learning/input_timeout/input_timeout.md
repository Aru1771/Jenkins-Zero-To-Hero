Session 3: input + timeout
=============================
We are now moving to the next locked topic in your Jenkins syllabus.

The goal is to understand how Jenkins can pause a pipeline and wait for human approval, while also preventing the pipeline from waiting forever.

Why do we need input?
-----------------------

Consider a real CI/CD pipeline:

    Git
     ↓
    Build
     ↓
    Test
     ↓
    Deploy to DEV
     ↓
    Deploy to QA
     ↓
            ⏸️ WAIT FOR APPROVAL
     ↓
    Deploy to PROD

You usually don't want production deployment to happen automatically after every successful QA deployment.

You may want someone to approve it.

That's what input does.

Basic input
------------
The simplest example:

    stage('Approval') {
        input {
            message 'Deploy to production?'
            ok 'Yes, Deploy'
        }
    
        steps {
            echo 'Production deployment approved'
        }
    }

Jenkins pauses at this stage and displays an approval prompt.

Conceptually:

    Pipeline
       ↓
    Approval Stage
       ↓
    ⏸️ Waiting for input
       ↓
    User clicks "Yes, Deploy"
       ↓
    Pipeline continues

What does message do?
---------------------
message 'Deploy to production?'

This is the message displayed to the person approving the pipeline.

For example:

Deploy to production?

You can make it more descriptive:

message 'QA testing is complete. Do you want to deploy Empower to PROD?'


What does ok do?
----------------
ok 'Yes, Deploy'

This controls the text of the approval button.

Instead of a generic:

Proceed

you can have:

Yes, Deploy

So the Jenkins UI will essentially show:

    ┌──────────────────────────────────────────┐
    │ QA testing is complete.                  │
    │ Do you want to deploy Empower to PROD?   │
    │                                          │
    │             [ Yes, Deploy ]              │
    └──────────────────────────────────────────┘


Real-world example
------------------
Let's create:

    Build
      ↓
    Test
      ↓
    Deploy QA
      ↓
    Manual Approval
      ↓
    Deploy PROD

Example:

        pipeline {
            agent any
        
            stages {
        
                stage('Build') {
                    steps {
                        echo 'Building Empower...'
                    }
                }
        
                stage('Test') {
                    steps {
                        echo 'Testing Empower...'
                    }
                }
        
                stage('Deploy QA') {
                    steps {
                        echo 'Deploying Empower to QA...'
                    }
                }
        
                stage('Approval') {
                    input {
                        message 'QA testing completed. Deploy Empower to PROD?'
                        ok 'Deploy to PROD'
                    }
        
                    steps {
                        echo 'Production deployment approved'
                    }
                }
        
                stage('Deploy PROD') {
                    steps {
                        echo 'Deploying Empower to PROD...'
                    }
                }
            }
        }

The important part is:

    stage('Approval') {
        input {
            message 'QA testing completed. Deploy Empower to PROD?'
            ok 'Deploy to PROD'
        }
    
        steps {
            echo 'Production deployment approved'
        }
    }

What happens if nobody approves?
---------------------------------
This is where timeout becomes important.

Imagine:

    10:00 AM
    Pipeline reaches approval
    
    10:30 AM
    Nobody approves
    
    11:00 AM
    Nobody approves
    
    12:00 PM
    Still waiting...

You don't want Jenkins to keep this build waiting indefinitely.

So we use:

    timeout

Basic timeout
---------------
Example:

    stage('Approval') {
    
        options {
            timeout(time: 10, unit: 'MINUTES')
        }
    
        input {
            message 'Deploy to PROD?'
            ok 'Deploy'
        }
    
        steps {
            echo 'Approved'
        }
    }

This means:

    Approval starts
          ↓
    Wait maximum 10 minutes
          ↓
    User approves?
       ↙       ↘
     YES       NO
     ↓          ↓
    Continue   Timeout

timeout units
---------------
You can use:

    unit: 'SECONDS'
    unit: 'MINUTES'
    unit: 'HOURS'

Examples:

    timeout(time: 30, unit: 'SECONDS')
    timeout(time: 15, unit: 'MINUTES')
    timeout(time: 2, unit: 'HOURS')

For production approval, something like:

    timeout(time: 30, unit: 'MINUTES')

is common conceptually.

input + timeout together
--------------------------
This is the important combination.

    stage('Production Approval') {
    
        options {
            timeout(time: 15, unit: 'MINUTES')
        }
    
        input {
            message 'Deploy Empower to PROD?'
            ok 'Approve Deployment'
        }
    
        steps {
            echo 'Production deployment approved'
        }
    }

Think:

              Pipeline
                 ↓
             Build/Test
                 ↓
              QA Deploy
                 ↓
        ┌─────────────────┐
        │    APPROVAL     │
        │                 │
        │ Wait 15 minutes │
        │                 │
        │ [Approve]       │
        └─────────────────┘
             ↓       ↓
          Approve   Timeout
             ↓       ↓
          PROD     Failure
Important distinction: input vs timeout
--------------------------------------------
Remember this:

input

Answers:

    Should the pipeline wait for human approval?

    input {
        message 'Deploy?'
    }
timeout

Answers:

    How long should Jenkins wait?

    options {
        timeout(time: 15, unit: 'MINUTES')
    }

Together:

    input
     ↓
    Wait for human
    
    timeout
     ↓
    Don't wait forever

timeout can also apply to an entire stage
-----------------------------------------
For example:

    stage('Deploy') {
    
        options {
            timeout(time: 10, unit: 'MINUTES')
        }
    
        steps {
            echo 'Deploying...'
        }
    }

Here the timeout applies to the stage.

It doesn't necessarily mean you're waiting for input.

It simply means:

If this stage takes longer than 10 minutes, terminate it.

So timeout has a broader purpose than just approvals.

Pipeline-level timeout

You can also put it at the pipeline level:
    
    pipeline {
    
        agent any
    
        options {
            timeout(time: 1, unit: 'HOURS')
        }
    
        stages {
            ...
        }
    }

Now the entire pipeline has a maximum duration.

Conceptually:
    
    Pipeline
    ├── Build
    ├── Test
    ├── QA
    ├── Approval
    └── PROD
           ↑
           |
    Maximum 1 hour

Where does input normally go?
-------------------------------
For a production pipeline, a common design is:

    Build
     ↓
    Unit Test
     ↓
    Security Scan
     ↓
    Docker Build
     ↓
    Deploy DEV
     ↓
    Deploy QA
     ↓
    Manual Approval
     ↓
    Deploy PROD

The approval is placed after validation/testing and before the production deployment.

🧪 Your Practical Task
--------------------------
Now let's use your Empower application.

Create this pipeline flow:

    Build
      ↓
    Test
      ↓
    Deploy QA
      ↓
    Production Approval
      ↓
    Deploy PROD

Requirements

    1. Build
    
    Print:
    
    Building Empower...
    2. Test
    
    Print:
    
    Testing Empower...
    3. Deploy QA
    
    Print:
    
    Deploying Empower to QA...
    4. Production Approval
    
    Use:
    
    input {
        message '...'
        ok '...'
    }

Your message should clearly ask whether to deploy Empower to production.

5. Add a timeout

Set the approval timeout to:

    2 minutes
    
    Use:
    
    options {
        timeout(time: 2, unit: 'MINUTES')
    }
6. Deploy PROD

Only after approval, print:

Deploying Empower to PROD...
🎯 Expected flow

If you approve:

Build
 ↓
Test
 ↓
QA
 ↓
⏸️ Approval
 ↓
Approve
 ↓
PROD

If you don't approve within 2 minutes:

Build
 ↓
Test
 ↓
QA
 ↓
⏸️ Approval
 ↓
2 minutes
 ↓
TIMEOUT
Important

For this task, don't add when yet. Focus only on understanding:

input
+
timeout

Write the Jenkinsfile yourself and run it. Then send it here, and I'll review it line-by-line.
