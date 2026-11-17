Day 2: Username/Password, Secret Text and SSH Credentials
=========================================================

Part 1 — Username with Password credentials:
--------------------------------------------

* Imagine your Jenkins pipeline needs to push a Docker image to Docker Hub.

To authenticate, Docker requires:

    - Username
    - Password or access token
Instead of hardcoding these values, create a Jenkins credential.


<img width="1934" height="1160" alt="image" src="https://github.com/user-attachments/assets/d280cfd3-0cd6-45a5-8a9a-9921f273b5b0" />

<img width="631" height="357" alt="image" src="https://github.com/user-attachments/assets/635c7002-c57e-449f-936f-20940085963c" />

How to use it in Jenkinsfile

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
                                  echo "$DOCKER_PASS" |
                                  docker login \
                                      -u "$DOCKER_USER" \
                                      --password-stdin
                              '''
                          }
                      }
                  }
              }
      }


What happens?

    1. Jenkins finds dockerhub-credentials.
    2. It retrieves the username and password.
    3. It exposes them as temporary environment variables inside the binding block.
    4. Docker uses those variables to authenticate.
    5. The credential binding ends after the block finishes.


<img width="738" height="547" alt="image" src="https://github.com/user-attachments/assets/01c19c9e-c7d0-42e4-b56d-0d067c4f19ba" />

How to use it

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

<img width="690" height="362" alt="image" src="https://github.com/user-attachments/assets/95250bd2-245a-47a3-8fb4-22695a65ed98" />

<img width="1012" height="513" alt="image" src="https://github.com/user-attachments/assets/0f03c858-5329-4a8c-8536-0a3cb74258ad" />

<img width="1038" height="718" alt="image" src="https://github.com/user-attachments/assets/e1d26213-c483-4b4b-a586-19b1e48bad57" />

<img width="661" height="343" alt="image" src="https://github.com/user-attachments/assets/5fc04fdb-8328-4366-bb0c-3f804837f8a2" />

How to use it in Jenkinsfile

* One common method is the sshagent step, provided by the SSH Agent plugin.
    
      pipeline {
          agent any
      
          stages {
              stage('Connect to EC2') {
                  steps {
                      sshagent(credentials: ['ec2-ssh-key']) {
                          sh '''
                              ssh -o StrictHostKeyChecking=yes \
                                  ec2-user@10.0.1.20 \
                                  "hostname"
                          '''
                      }
                  }
              }
          }
      }
      

<img width="1057" height="762" alt="image" src="https://github.com/user-attachments/assets/e922c64c-6582-495c-b186-9b40e92ee019" />

<img width="988" height="702" alt="image" src="https://github.com/user-attachments/assets/85e82111-6be9-49b7-891e-55735af8b05e" />

<img width="830" height="633" alt="image" src="https://github.com/user-attachments/assets/ac5d03b3-6d78-4522-a916-a14880d2a109" />

<img width="1028" height="747" alt="image" src="https://github.com/user-attachments/assets/0f038cd7-5ec7-49b1-b423-cd0f7f9961f5" />

<img width="833" height="307" alt="image" src="https://github.com/user-attachments/assets/3987559a-88bd-4ee2-83e0-2e446061b0da" />





