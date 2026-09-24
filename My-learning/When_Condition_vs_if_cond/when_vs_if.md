Session 2: when
================

Now we'll learn one of the most important Declarative Pipeline features: when.

Why do we need when?
--------------------

Suppose you have this pipeline:

    Build
      ↓
    Test
      ↓
    Deploy to DEV
      ↓
    Deploy to PROD

You don't want every stage to run every time.

For example:

    develop branch → Deploy DEV
    main branch    → Deploy PROD
    feature branch → Don't deploy

We use when to tell Jenkins:

    "Run this stage only when this condition is true."

Basic syntax
-------------
The structure is:

    stage('Deploy') {
        when {
            condition
        }
    
        steps {
            ...
        }
    }

Example:

    stage('Deploy') {
        when {
            branch 'main'
        }
    
        steps {
            echo 'Deploying to production'
        }
    }

This means:

    Current branch = main
            ↓
        Run Deploy
    
    Current branch ≠ main
            ↓
       Skip Deploy

when vs if
----------
This is very important.

You previously used something like:

    script {
        if (params.DEPLOY) {
            echo 'Deploying'
        }
    }

That's a Groovy if statement.

Jenkins Declarative Pipeline provides:

    when {
        ...
    }

which controls whether an entire stage executes.

if

Generally used inside steps:

    steps {
        script {
            if (...) {
                ...
            }
        }
    }
when

Used at the stage level:

    stage('Deploy') {
        when {
            ...
        }
    
        steps {
            ...
        }
    }

Think:

    if
     ↓
    Decision inside the stage

    when
     ↓
    Should this stage run at all?

Most important when conditions
------------------------------
We'll learn these:

    branch
    environment
    expression
    allOf
    anyOf
    not

We'll start with the simplest one.

branch
-------
Suppose your Git repository has:

main
develop
feature/login
feature/payment

You want production deployment only from main.

Use:

stage('Production Deploy') {

    when {
        branch 'main'
    }

    steps {
        echo 'Deploying to Production'
    }

    If branch = main
    
    Production Deploy
           ↓
         RUN
         
    If branch = develop
    Production Deploy
           ↓
        SKIPPED

Practical example
------------------
Let's create a simple pipeline:

      pipeline {
          agent any
      
          stages {
      
              stage('Build') {
                  steps {
                      echo 'Building application...'
                  }
              }
      
              stage('Test') {
                  steps {
                      echo 'Running tests...'
                  }
              }
      
              stage('Production Deploy') {
      
                  when {
                      branch 'main'
                  }
      
                  steps {
                      echo 'Deploying to Production...'
                  }
              }
          }
      }

Imagine Jenkins is building:

develop

Output:

    Build
      ↓
    RUN
    
    Test
      ↓
    RUN
    
    Production Deploy
      ↓
    SKIPPED

If Jenkins builds:

main

then:

    Build
      ↓
    RUN
    
    Test
      ↓
    RUN
    
    Production Deploy
      ↓
    RUN
    
  environment
---------------
Now let's connect this with what you learned in Session 1.

Suppose:

    environment {
        DEPLOY_ENV = 'prod'
    }

You can conditionally run a stage:

    stage('Production Deploy') {
    
        when {
            environment name: 'DEPLOY_ENV', value: 'prod'
        }
    
        steps {
            echo 'Deploying to production'
        }
    }

Meaning:

    DEPLOY_ENV = prod
           ↓
       RUN stage

Anything else:

    DEPLOY_ENV = dev
           ↓
      SKIP stage

expression
-----------
This is where when becomes more powerful.

You can evaluate a Groovy expression.

For example, you already have:

booleanParam(
    name: 'DEPLOY',
    defaultValue: false
)

You can write:

    stage('Deploy') {
    
        when {
            expression {
                params.DEPLOY
            }
        }
    
        steps {
            echo 'Deploying application...'
        }
    }

Now:

    DEPLOY = true
         ↓
    Deploy stage runs

and:

    DEPLOY = false
         ↓
    Deploy stage skipped

Notice how this is cleaner than the if logic you used in Task 1.

Combining conditions — allOf
----------------------------
Suppose production deployment should happen only when:

Branch = main
AND
DEPLOY = true

Use:

    when {
        allOf {
            branch 'main'
    
            expression {
                params.DEPLOY
            }
        }
    }

Think:

             Branch = main?
                    ↓
                   YES
                    ↓
             DEPLOY = true?
                    ↓
                   YES
                    ↓
              DEPLOY STAGE

If either condition is false:

    SKIP

anyOf
------
anyOf means OR.

Example:

when {
    anyOf {
        branch 'main'
        branch 'develop'
    }
}

Meaning:

    main       → RUN
    develop    → RUN
    feature/*  → SKIP

not
----
not reverses the condition.

Example:

    when {
        not {
            branch 'main'
        }
    }

Meaning:

    main
     ↓
    SKIP

Everything else:

    develop
    feature
    release
     ↓
    RUN


Important mental model
----------------------
Remember these four:

    allOf → AND
    anyOf → OR
    not   → NOT
expression → evaluate a condition

For example:

when {
    allOf {
        branch 'main'

        expression {
            params.DEPLOY
        }
    }
}

means:

    main AND DEPLOY=true
    
🧪 Your Task 1 — when + DEPLOY
-------------------------------
Now it's your turn.

You already created:

booleanParam(
    name: 'DEPLOY',
    defaultValue: false
)

Create a Deploy stage using when, not if.

Requirement:

DEPLOY = true
     ↓
Deploy stage runs
DEPLOY = false
     ↓
Deploy stage skipped

Your stage should contain:

when {
    ...
}

and then:

steps {
    echo ...
}
🎯 Expected result

If:

DEPLOY = false

Jenkins should show something like:

Deploy
SKIPPED

If:

DEPLOY = true

then:

Deploy
SUCCESS

Write this part yourself and send it to me. I'll check it before we move to Task 2 — branch-based deployment using branch.
