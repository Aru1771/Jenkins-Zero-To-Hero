Credentials & Security:
======================

<img width="722" height="692" alt="image" src="https://github.com/user-attachments/assets/ff2cf24a-3a6c-4f07-a86c-c0968d4044b1" />

The problem with hardcoding credentials

Consider this Jenkinsfile:

    pipeline {
        agent any
    
        stages {
            stage('Docker Login') {
                steps {
                    sh '''
                        docker login \
                        -u admin \
                        -p MyPassword123
                    '''
                }
            }
        }
    }

<img width="716" height="297" alt="image" src="https://github.com/user-attachments/assets/0517edef-ec06-4dec-a2ce-65b2aab723c8" />

Part 2 — What is Jenkins Credentials Store?

Jenkins Credentials Store is a secure mechanism for storing sensitive information.

Instead of writing credentials directly in a Jenkinsfile, we store them in Jenkins and reference them using a credential ID.

<img width="991" height="715" alt="image" src="https://github.com/user-attachments/assets/56085cf2-e23a-4766-ab6e-d2bdd81bb915" />

<img width="837" height="452" alt="image" src="https://github.com/user-attachments/assets/c886f1c2-1f6d-4524-93db-37041cf193c2" />

Part 4 — Where do we store credentials?

Let's create a credential in Jenkins.

<img width="829" height="654" alt="image" src="https://github.com/user-attachments/assets/74448e26-11b6-4627-b566-e87111d4972f" />

Follow these steps:

    1. Open Jenkins Dashboard.
    2. Select Manage Jenkins.
    3. Select Credentials.
    4. Open System.
    5. Select Global credentials (unrestricted).
    6. Click Add Credentials.

You'll see fields such as:

    - Kind
    - Scope
    - Username
    - Password or Secret
    - ID
    - Description

<img width="711" height="536" alt="image" src="https://github.com/user-attachments/assets/1076224f-83ee-4467-8d4a-8f7f6ccb78ea" />

Part 5 — Use credentials inside a Jenkinsfile

Let's understand the syntax.

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerhub-credentials',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {
        sh '''
            echo "$DOCKER_PASS" | docker login \
                -u "$DOCKER_USER" \
                --password-stdin
        '''
    }

<img width="863" height="367" alt="image" src="https://github.com/user-attachments/assets/5b18223a-e0c8-42fe-9308-cf92241e87b7" />

<img width="958" height="466" alt="image" src="https://github.com/user-attachments/assets/e76025ee-26ac-4a92-8486-9fcf8ebd1f98" />

<img width="962" height="392" alt="image" src="https://github.com/user-attachments/assets/9121f9b2-e64a-46b4-8acb-d631f548ae8d" />

<img width="602" height="671" alt="image" src="https://github.com/user-attachments/assets/0e6ce593-eeb6-48e5-ac9d-667817cc1caf" />

    pipeline {
        agent any
    
        stages {
            stage('Test Credentials') {
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
                            else
                                echo "GitHub token is missing"
                                exit 1
                            fi
                        '''
                    }
                }
            }
        }
    }

Expected output:
GitHub token is available


<img width="925" height="515" alt="image" src="https://github.com/user-attachments/assets/7d186bc1-814c-4ebc-a281-bd8dac8000f7" />

<img width="1013" height="582" alt="image" src="https://github.com/user-attachments/assets/a64ed298-04bc-404b-b22c-4303e4aef893" />

<img width="1000" height="320" alt="image" src="https://github.com/user-attachments/assets/12670f0f-dd53-4cce-82da-931b29ab2510" />

Do not print the actual token.

