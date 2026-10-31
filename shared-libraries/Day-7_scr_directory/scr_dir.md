Topic: Using src/ directory in Shared Libraries
=================================================

* So far, you've mainly used the vars/ directory. Today, we'll learn about another important Shared Library directory: src/.

Understand the difference between vars/ and src/:
--------------------------------------------------

A Jenkins Shared Library commonly uses this structure:

        my-shared-library/
        │
        ├── vars/
        │   ├── buildApp.groovy
        │   ├── runTests.groovy
        │   └── deployApp.groovy
        │
        └── src/
            └── com/
                └── company/
                    └── Utils.groovy

Directory	    Purpose
vars/	        Defines reusable pipeline steps that you call directly from Jenkinsfile.
src/	        Contains Groovy classes and reusable helper logic.

Simple example:

- vars/buildApp.groovy → You call buildApp() directly.
- src/com/company/Utils.groovy → You use the Utils class to perform reusable operations.

Create a class inside src/:
---------------------------

Create this file:

src/com/company/Utils.groovy

    package com.company

    class Utils {
    
        static String getAppInfo(String appName, String version) {
            return "Application: ${appName}, Version: ${version}"
        }
    }

Here:

    - package defines the class package.
    - class Utils defines a reusable Groovy class.
    - static allows you to call the method without creating an object.
    - getAppInfo() returns a string.

Use this class inside vars/
---------------------------

Now create or update:

vars/buildApp.groovy

    import com.company.Utils
    
    def call(Map config) {
    
        def appName = config.app_name?.toString()?.trim()
        def version = config.app_version?.toString()?.trim()
    
        if (!appName) {
            error 'Application name is required'
        }
    
        if (!version) {
            error 'Application version is required'
        }
    
        def appInfo = Utils.getAppInfo(appName, version)
    
        echo appInfo
    }


Notice that buildApp.groovy is calling a method from the Utils class.


Call it from Jenkinsfile:
-------------------------

    @Library('my-shared-library') _
    
    pipeline {
        agent any
    
        stages {
            stage('Build Application') {
                steps {
                    buildApp(
                        app_name: 'Empower',
                        app_version: '2.0'
                    )
                }
            }
        }
    }


Expected output: Application: Empower, Version: 2.0

<img width="946" height="420" alt="image" src="https://github.com/user-attachments/assets/84b81d92-4f79-45da-b4fa-dc90b8566d59" />

Why do real DevOps teams use src/?
-----------------------------------

Imagine your organization has 20 applications.

You may need common helper logic for:

    - Docker image naming.
    - Version formatting.
    - Configuration processing.
    - Validation utilities.
    - Reusable data transformations.

* Instead of writing this logic repeatedly in multiple vars/ files, you can organize it into reusable Groovy classes under src/.

Important: src/ classes are not Jenkins pipeline steps by themselves. 
Keep Jenkins pipeline-specific operations such as echo, sh, and error in the pipeline context (vars/ or an appropriately passed pipeline script object). 
The example above keeps the helper class independent of Jenkins pipeline steps.
