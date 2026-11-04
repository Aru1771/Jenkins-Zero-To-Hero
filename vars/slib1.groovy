def call(Map config) {

    def app_name = config.app_name?. toString() ?.trim()
    def app_version = config.app_version ?. to String() ?. trim()
    def environment = config.environment ?. toString() ?. trim()

    if (!app_name) {
        error 'Application name is required'
    }

    if (!app_version) {
        error 'Application version is required'
    }

    if (!environment) {
        error 'Environment is required'
    }

    echo "my application name is ${app_name} and version is ${app_version} and environment is ${environment}"
}