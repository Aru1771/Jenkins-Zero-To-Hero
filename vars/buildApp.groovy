def call(Map config) {
    def appName = config.appName?. tostring()?.trim()
    def version = config.version?. tostring()?. trim()

    if (!appName) {
        error "Application should not be blank"
    }
    if (!version){
        error "App Version required"
    }

    

    echo "application: ${appName}"
    echo "version: ${version}"

}

