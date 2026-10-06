Matrix + when
==============

Now let's combine what you learned earlier about when.

Suppose we have:

    dev
    stage
    prod

but we don't want to run this particular test in production.

    matrix {
    
        axes {
    
            axis {
                name 'ENVIRONMENT'
                values 'dev', 'stage', 'prod'
            }
    
            axis {
                name 'VERSION'
                values '1.2', '1.3'
            }
        }
    
        stages {
    
            stage('Test') {
    
                when {
                    not {
                        environment name: 'ENVIRONMENT', value: 'prod'
                    }
                }
    
                steps {
                    echo "Testing ${ENVIRONMENT} - ${VERSION}"
                }
            }
        }
    }

Conceptually:

    dev   1.2 → Test ✅
    dev   1.3 → Test ✅
    
    stage 1.2 → Test ✅
    stage 1.3 → Test ✅
    
    prod  1.2 → Test skipped
    prod  1.3 → Test skipped

Why use when?

matrix says:

    "Create these combinations."

when says:

    "For this particular combination, should this stage execute?"

That's a very important distinction.







