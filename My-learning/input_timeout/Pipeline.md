    pipeline {
        agent any
        
        stages {
            stage('Build') {
                steps {
                    echo "Build completed.."
                }
            }
            stage('Test') {
                steps {
                    echo "Test Completed.."
                }
            }
            stage('QA Deployment') {
                steps {
                    echo "QA Deployment was completed.."
                }
            }
            stage("Approval for Production") {
                options {
                    timeout(time: 2, unit: 'MINUTES')
                }
                input {
                    message 'Provide Approval for Production Deplyment ?'
                    ok 'Yes, Deploy to Prod'
                }
                steps {
                    echo "Production Deployment successfull..."
                }
            }
        }
    }
