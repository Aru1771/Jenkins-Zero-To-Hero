def call(Map config) {
    def appName = config.app_name?. toString()?.trim()
    def version = config.app_version?. toString()?. trim()
    def environment = config.environment?. toString()?. trim()

    if (!appName) {
        error "Application should not be blank"
    }
    if (!version){
        error "App Version required"
    }

    if (!environment) {
        error "Environment is required"
    }


    echo "application: ${appName}"
    echo "version: ${version}"
    echo "Environment ${environment}"

}

