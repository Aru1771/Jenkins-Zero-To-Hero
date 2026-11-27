Practice playground: https://onecompiler.com/groovy?utm_source=chatgpt.com

Day 1:
======

What is Groovy?

* Groovy is a programming language that runs on the Java Virtual Machine (JVM). It is designed to work closely with Java, but its syntax is generally shorter and more flexible.

Let's write a basic Groovy program:

    println "Welcome to Groovy"
    println "Learning Jenkins Pipeline Coding"

println prints a message and moves to the next line.

<img width="550" height="216" alt="image" src="https://github.com/user-attachments/assets/8588acae-8d7a-43c6-b7d7-15a14232e3e1" />

Day-2:
======

Groovy Variables, Data Types and Strings

<img width="991" height="537" alt="image" src="https://github.com/user-attachments/assets/6b6e07bb-2f10-4667-98c9-fe0a8bc7b966" />

<img width="1010" height="747" alt="image" src="https://github.com/user-attachments/assets/9bde7b98-696d-4b6c-b41c-fcf1f234f947" />

<img width="857" height="445" alt="image" src="https://github.com/user-attachments/assets/30207f50-bc0c-4075-89ef-2dd6a168e66b" />

Remember this Jenkins distinction:

    - Groovy double-quoted strings can interpolate variables.
    - Groovy single-quoted strings don't interpolate variables.
    - Jenkins sh is a pipeline step that executes shell commands.

def appName = 'empower'

Single quotes: println 'Application name is ${appName}' 

Double quotes: println "Application name is ${appName}"

Double-quoted strings support interpolation and produce a GString when interpolation is used.

What is script {}?

* In Declarative Jenkins Pipeline, script {} allows us to write imperative Groovy code inside a stage.

* Remember the distinction:

        - def – declares a Groovy variable.
        - environment – defines environment variables for Jenkins pipeline execution.
        - env.APP_NAME – accesses a Jenkins environment variable from Groovy code.


Day-3:
======

Today we'll learn three important Groovy data structures:
1. List – Store multiple values.
2. Map – Store ddef services = ['user-service', 'payment-service', 'order-service']

services.each { service ->
    echo "Building ${service}"
}ata as key-value pairs.
3. Set – Store unique values without duplicates.

List:

Remember: Groovy Lists use zero-based indexing.

    def services = ['user-service', 'payment-service', 'order-service']

    println services[0]
    println services[1]
    println services[2]


def services = ['user-service', 'payment-service', 'order-service']

services.each { service ->
    echo "Building ${service}"
}

    each iterates through every List element.
    service represents the current element.
    separates the closure parameter from its body.
    echo is a Jenkins Pipeline step.

Important: each {} is a Groovy collection operation. echo is supplied by Jenkins Pipeline.

<img width="1002" height="743" alt="image" src="https://github.com/user-attachments/assets/8486d404-3c66-4002-8614-d47d8482dd39" />

<img width="982" height="851" alt="image" src="https://github.com/user-attachments/assets/69a46ec7-f91f-429c-b9cd-e4f91eb3444d" />

<img width="983" height="887" alt="image" src="https://github.com/user-attachments/assets/d9e707eb-406b-4efe-af4f-4b6a8c8a5edd" />

This is called a nested Map: a Map containing other Maps.

This pattern is especially useful when building reusable deployment functions in vars/deployApp.groovy.

<img width="996" height="767" alt="image" src="https://github.com/user-attachments/assets/30f0272f-241e-41f7-b9ed-bd176a8d61df" />

<img width="883" height="505" alt="image" src="https://github.com/user-attachments/assets/29c3e813-b644-48b4-9cdd-4587a6134133" />

5. Combining List and Map in a Jenkins pipeline
Now let's put everything together.

Imagine an enterprise CI pipeline that builds three microservices. Each service has its own version and Docker image name.

        pipeline {
            agent any
        
            stages {
                stage('Build Microservices') {
                    steps {
                        script {
                            def services = [
                                [name: 'user-service', version: '1.0'],
                                [name: 'payment-service', version: '2.0'],
                                [name: 'order-service', version: '1.5']
                            ]
        
                            services.each { service ->
        
                                def imageName =
                                    "${service.name}:${service.version}"
        
                                echo "Building ${service.name}"
                                echo "Docker Image: ${imageName}"
                            }
                        }
                    }
                }
            }
        }

<img width="971" height="650" alt="image" src="https://github.com/user-attachments/assets/87b22301-bf5e-4697-b29f-fc9587ce8719" />
