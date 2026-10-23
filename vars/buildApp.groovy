def call(Map config) {
    def appName = config.appName?. toString()?.trim()
    def version = config.version?. toString()?. trim()

    if (!appName) {
        error "Application should not be blank"
    }
    if (!version){
        error "App Version required"
    }



    echo "application: ${appName}"
    echo "version: ${version}"

}

