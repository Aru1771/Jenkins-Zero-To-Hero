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

* For example, if the version is missing, the function uses 1.0.0.

Important: Defaults are useful for optional values, but you shouldn't silently deploy to an unintended environment just because a parameter is missing.

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

