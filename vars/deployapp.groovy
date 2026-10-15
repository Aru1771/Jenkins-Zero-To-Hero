def call(Map config) {

    if (!config.app_name) {
        error: "Application Name is required"
    }
    if (!config.app_version) {
        error: "Application version is required"
    }
    if (!config.env) {
        error: "Environment is nor declared"
    }


    echo "Application Name is ${app_name}"
    echo "Application Version is ${app_version}"
    echo "Environment is ${env}"
}