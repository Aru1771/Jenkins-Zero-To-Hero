    
    pipeline {
        agent any
    
        stages {
            stage('Build') {
                steps {
                    echo 'Building Empower...'
                }
            }
    
            stage('Matrix Testing') {
                matrix {
                    axes {
                        axis {
                            
                            name 'ENVIRONMENT'
                            values 'dev', 'stage'
                        }
                        
                        axis {
                            
                            name 'version'
                            values '1.2', '1.3'
                        }
                        
                        axis {
                            
                            name 'JAVA_VERSION'
                            values '17', '21'
                        }
                    }
    
                    stages {
                        stage('Test') {
                            steps {
                                echo "Testing Empower - ${ENVIRONMENT} - ${version} - ${JAVA_VERSION}"
                            }
                        }
                    }
                }
            }
    
            stage('Deployment') {
                steps {
                    echo 'Deploying Empower...'
                }
            }
        }
    }
