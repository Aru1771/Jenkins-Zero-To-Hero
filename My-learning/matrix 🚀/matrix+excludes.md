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
    
        excludes {
            exclude {
                axis {
                    name 'ENVIRONMENT'
                    values 'prod'
                }
    
                axis {
                    name 'VERSION'
                    values '1.2'
                }
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

Now Jenkins generates:

    dev   1.2
    dev   1.3
    stage 1.2
    stage 1.3
    prod  1.3

prod + 1.2 is removed.

Real-world meaning

You can think:

    "Generate everything, except these combinations."

For example:

Browser = Chrome, Firefox
OS      = Windows, Linux

Maybe Firefox isn't supported on a particular platform.

You can exclude that combination.
