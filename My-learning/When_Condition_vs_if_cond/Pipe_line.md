    pipeline {
        agent any
        
        parameters {
            string(
            name: 'app_version',
            defaultValue: '1.30',
            description: 'application version'
            )
            booleanParam(
                name: 'Deployment_Confirmation',
                defaultValue: false,
                description: 'application deployment confirmation'
                )
            choice(
                name: 'branch_selection',
                choices: ['future', 'pre_prod', 'dev'],
                description: 'select the branch from hear'
                )
        }
        
        environment {
            App_name = 'Aru1771/Empower3'
            image_tag = '${build_number}'
            image = '${App_name}:${image_tag}'
            environment = 'prod'
            branch_selection_var = "${params.branch_selection}"
        }
        
        
        stages {
            
            stage('Build') {
                steps {
                    echo "Build successful.."
                }
            }
            stage('Test') {
                steps {
                    echo "Test was successful.."
                }
            }
            stage('brach_based'){
                when {
                    branch 'main'
                }
                steps {
                    echo "this is test"
                }
            }
            stage('envronment_based'){
                when {
                    environment name: 'environment', value: 'prod'
                }
                
                steps{
                    echo "env_based_deployment"
                }
            }
            stage('expression_with_parameter_based') {
                when {
                    expression {
                        params.branch_selection == "${branch_selection_var}"
                    }
                }
                steps{
                    echo "this a selected brach ${branch_selection_var}"
                }
                
            }
            stage('all_of') {
                when{
                    allOf {
                        environment name: 'environment', value: 'prod'
                        expression {
                            params.branch_selection == 'dev'
                        }
                    }
                }
                steps {
                    echo "all the conditions are ture"
                }
            }
            stage('any_of'){
                when {
                    anyOf{
                        environment name:'environment', value: 'dev'
                        expression {
                            params.branch_selection == 'dev'
                        }
                    }
                }
                steps{
                    echo "in both one condition is true"
                }
            }
            stage('not') {
                when {
                    not {
                        environment name:'environment', value: 'future'
                    }
                }
                steps{
                    echo "env value is not match with ${environment} "
                }
            }
            stage('Deployment') {
                when {
                    expression {
                        params.Deployment_Confirmation == true
                    }
                }
    
                steps{
                    echo "deployment_completed_successful...."
                }
                    
            }
        }
        
    }
