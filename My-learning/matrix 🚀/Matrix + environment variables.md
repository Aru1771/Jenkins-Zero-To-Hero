You can also combine normal Jenkins environment variables with matrix variables.

For example:

      pipeline {
      
          agent any
      
          environment {
              APP_NAME = 'Empower'
              TEAM_NAME = 'Empower Team'
          }
      
          stages {
      
              stage('Matrix Testing') {
      
                  matrix {
      
                      axes {
      
                          axis {
                              name 'ENVIRONMENT'
                              values 'dev', 'stage'
                          }
      
                          axis {
                              name 'VERSION'
                              values '1.2', '1.3'
                          }
                      }
      
                      stages {
      
                          stage('Test') {
                              steps {
      
                                  echo "Application: ${APP_NAME}"
                                  echo "Team: ${TEAM_NAME}"
                                  echo "Environment: ${ENVIRONMENT}"
                                  echo "Version: ${VERSION}"
                              }
                          }
                      }
                  }
              }
          }
      }

Here you have two kinds of variables:

Normal pipeline environment variables

    APP_NAME
    TEAM_NAME
    Matrix variables
    ENVIRONMENT
    VERSION

So one matrix cell might print:

    Application: Empower
    Team: Empower Team
    Environment: dev
    Version: 1.2


      
