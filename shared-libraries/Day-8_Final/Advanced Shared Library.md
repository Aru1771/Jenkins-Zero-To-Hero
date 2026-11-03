Topic: Advanced Shared Library Design and Production Best Practices
===================================================================

So far, you've learned vars/, src/, Groovy classes, reusable functions, parameters, and input validation.

Today, we'll learn how to organize a Shared Library like a DevOps engineer working with multiple applications in a production environment.

Understand a production Shared Library structure
-------------------------------------------------

Imagine your organization supports three applications:

    - Empower
    - NuGenesis
    - Laboratory Automation
Instead of maintaining separate Jenkins pipeline logic for every application, we create one common Shared Library.

A recommended structure:
-------------------------

    my-shared-library/
    │
    ├── vars/
    │   ├── buildApp.groovy
    │   ├── runTests.groovy
    │   ├── buildDockerImage.groovy
    │   └── deployApp.groovy
    │
    ├── src/
    │   └── com/
    │       └── company/
    │           ├── DockerUtils.groovy
    │           ├── VersionUtils.groovy
    │           └── ValidationUtils.groovy
    │
    └── README.md

<img width="817" height="343" alt="image" src="https://github.com/user-attachments/assets/be9d181f-d740-4220-b22d-4dad50eee2fc" />

Separate pipeline logic from business logic:
-------------------------------------------

This is an important production practice.

Suppose you want to build a Docker image.

In vars/buildDockerImage.groovy:

    def call(Map config) {
    
        def appName = config.app_name?.toString()?.trim()
        def version = config.version?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        if (!version) {
            error 'Version is required'
        }
    
        def imageName = com.company.DockerUtils.getImageName(
            appName,
            version
        )
    
        echo "Docker image: ${imageName}"
    }
    
In src/com/company/DockerUtils.groovy:

    package com.company
    
    class DockerUtils {
    
        static String getImageName(String appName, String version) {
            return "${appName.toLowerCase()}:${version}"
        }
    }

Here is the important difference:

    buildDockerImage.groovy handles Jenkins pipeline interaction.
    DockerUtils.groovy handles reusable image-name formatting.

This separation makes the library easier to test and maintain.

Reuse the same function for multiple applications:

Your Jenkinsfile:

    @Library('my-shared-library') _
    
    pipeline {
        agent any
    
        stages {
    
            stage('Build Empower') {
                steps {
                    buildDockerImage(
                        app_name: 'Empower',
                        version: '2.0'
                    )
                }
            }
    
            stage('Build NuGenesis') {
                steps {
                    buildDockerImage(
                        app_name: 'NuGenesis',
                        version: '3.0'
                    )
                }
            }
        }
    }


Expected output:

    Docker image: empower:2.0
    Docker image: nugenesis:3.0

Both applications reuse the same library function.

<img width="1130" height="712" alt="image" src="https://github.com/user-attachments/assets/5608c65b-87d5-4ae6-9e7c-a2099426d360" />

Important: Shared Library versioning:

You can load a specific version of your library:

    @Library('my-shared-library@main') _


For production, using an immutable Git tag or commit is safer than relying on a branch that can change:

    @Library('my-shared-library@v1.0.0') _


This makes pipeline behavior more predictable.

<img width="923" height="742" alt="image" src="https://github.com/user-attachments/assets/3657d852-4225-433c-a6ff-0235abac9dd0" />
