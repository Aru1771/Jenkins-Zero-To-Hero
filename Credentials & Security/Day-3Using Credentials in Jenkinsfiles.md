Using Credentials in Jenkinsfiles
=================================

Today we'll learn how to use Jenkins credentials inside real pipeline stages without exposing secrets.

Why do we use credentials in Jenkinsfiles?
-------------------------------------------

Imagine your Jenkins pipeline needs to:

    1. Pull source code from a private GitHub repository.
    2. Push a Docker image to Docker Hub.
    3. Deploy an application to an EC2 server.
These operations may require tokens, usernames, passwords or SSH keys.

We should never hardcode them:

    environment {
        DOCKER_PASSWORD = 'my-secret-password'
    }


Instead, store secrets in Jenkins Credentials and reference them using their credential IDs.

Two ways to access credentials:
-------------------------------

Method 1: withCredentials

* This binds credentials only inside a specific block.

Example: Secret Text
Assume you created a Jenkins credential:
    
    - Kind: Secret text
    - ID: github-token

    pipeline {
        agent any
    
        stages {
            stage('GitHub Authentication') {
                steps {
                    withCredentials([
                        string(
                            credentialsId: 'github-token',
                            variable: 'GITHUB_TOKEN'
                        )
                    ]) {
                        sh '''
                            if [ -n "$GITHUB_TOKEN" ]; then
                                echo "GitHub token is available"
                            fi
                        '''
                    }
                }
            }
        }
    }

What happens?

    1. Jenkins retrieves the credential using github-token.
    2. Jenkins makes it available through GITHUB_TOKEN.
    3. The shell command can use that variable.
    4. After the block finishes, the binding is no longer available to subsequent pipeline steps.
    Notice that we check whether the token exists without printing its value.

Notice that we check whether the token exists without printing its value.

Method 2: Declarative credentials() helper:
-------------------------------------------

You can also bind credentials inside the pipeline's environment block.

    pipeline {
        agent any
    
        environment {
            GITHUB_TOKEN = credentials('github-token')
        }
    
        stages {
            stage('GitHub Authentication') {
                steps {
                    sh '''
                        if [ -n "$GITHUB_TOKEN" ]; then
                            echo "GitHub token is available"
                        fi
                    '''
                }
            }
        }
    }


Here, Jenkins makes the credential available through the pipeline environment.

<img width="976" height="753" alt="image" src="https://github.com/user-attachments/assets/38131580-b65d-4888-a249-9a15b06fa1d2" />

<img width="1058" height="416" alt="image" src="https://github.com/user-attachments/assets/7ac7823c-3569-4e59-8b67-5912cdd9207b" />

<img width="1030" height="698" alt="image" src="https://github.com/user-attachments/assets/095fdc9d-73a5-4824-b511-132aa717eb88" />

Also:

    - Never use echo "$GITHUB_TOKEN".
    - Avoid shell tracing (set -x) around secrets.
    - Masking is not a guarantee against every way secrets can leak.
    - Never expose credentials to untrusted pull-request builds.


      pipeline {
          agent any
      
          stages {
              stage('Clone Repository') {
                  steps {
                      withCredentials([
                          usernamePassword(
                              credentialsId: 'github-token',
                              usernameVariable: 'GIT_USERNAME',
                              passwordVariable: 'GIT_TOKEN'
                          )
                      ]) {
                          bat '''
                              git clone https://%GIT_USERNAME%:%GIT_TOKEN%@github.com/Aru1771/Terraform-Full-Course-Aws.git
                          '''
                      }
                  }
              }
          }
      }




<img width="993" height="552" alt="image" src="https://github.com/user-attachments/assets/bc6cf9e8-1563-40e1-9825-8fa04b04ef17" />




            pipeline {
                agent any
                
                stages {
                    stage('Git_Cred') {
                        steps{
                            withCredentials([
                                string(
                                    credentialsId: 'github-token',
                                    variable: 'GITHUB_TOKEN'
                                    )
                                ])
                                {
                                    bat '''
                                    if not "%GITHUB_TOKEN%"=="" (
                                        echo GitHub token is available
                                    ) else (
                                        echo GitHub token is missing
                                        exit /b 1
                                    )
                                '''
                                }
                        }
                    }
                    stage('Docker-hub') {
                        steps{
                            withCredentials([
                                usernamePassword(
                                    credentialsId: 'dockerhub-credentials',
                                    usernameVariable: 'DOCKER_USER',
                                    passwordVariable: 'DOCKER_PASS'
                                    )
                                ])
                                {
                                    echo "Docker user name: ${DOCKER_USER}"
                                }
                                
                        }
                    }
                    stage('SSH-login'){
                        steps{
                            sshagent(credentials: ['ec2-ssh-key']){
                                
                                bat '''
                            ssh -o StrictHostKeyChecking=yes ^
                                ec2-user@172.31.32.87 ^
                                "hostname"
                        '''
                            }
                        }
                    }
                }
            }
