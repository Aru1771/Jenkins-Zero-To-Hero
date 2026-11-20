Credentials Binding and Masking:
================================

Today we'll understand how Jenkins makes secrets available to pipeline steps, how masking works, 
and how to prevent credentials from leaking into Jenkins console logs.


What is Credentials Binding?
-----------------------------

* Suppose you have stored a Docker Hub username and password in Jenkins Credentials.

Credential ID:

    dockerhub-credentials

Your pipeline needs these credentials to authenticate with Docker Hub.
Your pipeline needs these credentials to authenticate with Docker Hub.

Credentials Binding makes the stored credentials temporarily available to your pipeline as environment variables.

    pipeline {
        agent any
    
        stages {
            stage('Docker Login') {
                steps {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'dockerhub-credentials',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )
                    ]) {
                        sh '''
                            echo "Docker username is available"
    
                            echo "$DOCKER_USER" | \
                            docker login \
                                --username "$DOCKER_USER" \
                                --password-stdin
                        '''
                    }
                }
            }
        }
    }


Here:

    - Jenkins retrieves the username and password.
    - DOCKER_USER receives the username.
    - DOCKER_PASS receives the password.
    - The credentials are available inside the withCredentials block.
    - After the block, the binding is no longer available to subsequent steps.

Important correction: The example above needs to pass the password to Docker, not the username. Use this correct version:

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerhub-credentials',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )
    ]) {
        sh '''
            echo "Docker username is available"
    
            printf '%s' "$DOCKER_PASS" | \
            docker login \
                --username "$DOCKER_USER" \
                --password-stdin
        '''
    }


This avoids putting the password directly in the command-line arguments.

What is Credentials Masking?
-----------------------------

* Imagine someone accidentally writes:

      sh 'echo "$DOCKER_PASS"'

* Jenkins tries to recognize and mask the secret in the console output.

Jenkins Console Output

    [Pipeline] withCredentials
    [Pipeline] sh
    + echo ****
    ****
    [Pipeline] }
    For example, the console may show:


The actual password is replaced with **** in the displayed output.

But remember:

Masking is not encryption, and it is not a complete security boundary.

Jenkins masking is best-effort. It can fail to protect secrets if they are transformed, encoded, split across output, exposed by another process, or written to files.

<img width="985" height="682" alt="image" src="https://github.com/user-attachments/assets/aeb8ad52-99de-45f0-a8eb-e802a6963cd7" />

<img width="1002" height="656" alt="image" src="https://github.com/user-attachments/assets/882770d1-e3d9-4579-95f0-223fd86cd14e" />

<img width="982" height="423" alt="image" src="https://github.com/user-attachments/assets/6edc14cd-95ab-4cad-a33d-06d39d5f1dd7" />

<img width="983" height="405" alt="image" src="https://github.com/user-attachments/assets/f64f7fee-bb3c-4a3c-aac1-085c3ac14cce" />

<img width="831" height="370" alt="image" src="https://github.com/user-attachments/assets/246a12db-202a-48a9-883f-e93c2c03f90c" />

    stage('Deploy') {
        steps {
            withCredentials([
                string(
                    credentialsId: 'github-token',
                    variable: 'GITHUB_TOKEN'
                )
            ]) {
                sh '''
                    echo "Performing authenticated operation"
                '''
            }
        }
    }

This is preferable to exposing the token to every stage.

<img width="975" height="822" alt="image" src="https://github.com/user-attachments/assets/9a4561ff-ae8a-429d-96e3-9e48d1ad0af6" />

