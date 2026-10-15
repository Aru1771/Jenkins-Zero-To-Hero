Yesterday, you successfully created and tested deployApp.groovy using your local Git repository. Today, we'll make that function more reliable for real-world use.

1. The problem with missing parameters
-----------------------------------------

Yesterday, you called:

        deployApp(
            appName: 'Empower',
            version: '2.0.0',
            environment: 'stage'
        )


* But what happens if someone forgets to provide the version?

        deployApp(
            appName: 'Empower',
            environment: 'stage'
        )


Your function may print:

        Application: Empower
        Version: null
        Environment: stage


* That isn't ideal for a deployment pipeline. We should either provide a sensible default where appropriate or stop with a clear error.

2. Set default values
----------------------

* For optional configuration, you can use Groovy's Elvis operator (?:).

        def call(Map config) {
            def appName = config.appName ?: 'Unknown-App'
            def version = config.version ?: '1.0.0'
            def environment = config.environment ?: 'dev'

            echo "Application: ${appName}"
            echo "Version: ${version}"
            echo "Environment: ${environment}"
        }

There are three concepts here:

        - def — declares a local variable in Groovy.
        - config.appName — reads the appName value from the config object, typically a map.
        - ?: — the Groovy Elvis operator, which uses a default value when the left side is false-like. It helps you set fallback values without writing a full if-else statement.

Let's break it down:

        - Map config — receives the map passed by the Jenkinsfile.
        - config.appName — reads the appName entry from that map.
        - config.version — reads the version entry.
        - config.environment — reads the environment entry.

How the values travel

        Jenkinsfile
        appName: 'Empower'
        version: '3.8.0'
        environment: 'prod'
        
        Shared Library
        def call(Map config)
        Receives the three entries in config
        
        Access the values
        config.appName → Empower
        config.version → 3.8.0
        config.environment → prod

What if you don't use config?

        You could write a function with individual parameters, but then the call and function definition would be different:

        def deployapp(String appName, String version) {
            echo "Deploying ${appName} version ${version}"
        }


        Call it like this:
        deployapp('Empower', '3.8.0')


* Using Map config is convenient when your deployment needs many optional settings, because you can pass named values.
* Remember: config is not a special Jenkins keyword. It's just a variable name chosen by the developer. You could call it settings instead:

        def call(Map settings) {
            echo settings.appName
        }

* For example, if the version is missing, the function uses 1.0.0.

Important: Defaults are useful for optional values, but you shouldn't silently deploy to an unintended environment just because a parameter is missing.


What if the given vale is empty spaces:

        Operator/function	Purpose
        ----------------        ---------
        ?.	                Skips the method call if the value is null.
        toString()	        Converts a non-null value to a string.
        trim()	                Removes leading and trailing whitespace.
        ?:	                Returns the default if the processed value is false-like.



3. Validate required values
-----------------------------

* For production-style pipelines, it is often safer to reject missing or invalid configuration.

        def call(Map config) {
            if (!config.appName) {
                error "Application name is required"
            }

            if (!config.version) {
                error "Application version is required"
            }

            if (!(config.environment in ['dev', 'stage', 'prod'])) {
                error "Invalid environment: ${config.environment}"
            }

            echo "Application: ${config.appName}"
            echo "Version: ${config.version}"
            echo "Environment: ${config.environment}"
        }



Here:

        - if (!config.appName) checks whether the value is missing or empty.
        - error fails the Jenkins build with a clear message.
        - in ['dev', 'stage', 'prod'] checks whether the environment is allowed.

* This prevents the function from continuing with invalid configuration.

Validation Flow:


        if (...)    --->        Checks whether a condition is true.
        !           --->	is called the logical NOT operator. It reverses a Boolean result: true becomes false, and false becomes true.
        ?.          --->  	Safely skips a method call when the value is null.
        toString()  --->	Converts the value into a string.
        trim()	    --->         Removes leading and trailing whitespace.
        error(...)   --->	Stops the Jenkins pipeline with a failure message.

Remember one thing: 

        In jenkins "Empty string"  and "null value" consider as False. so to execute the error block we change thet false value to true by using (!) 


4. Default values vs validation
----------------------------------

        Situation	                    Recommended approach
        ---------                       ----------------------
        Optional display label	        Use a default
        Optional noncritical setting	Use a sensible default
        Application name	            Validate
        Application version	Usually     validate
        Deployment environment	        Validate against allowed values
        Production approval	            Require explicit human approval; never replace it with a default


