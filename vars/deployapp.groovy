def call(Map config) {
    def app_name = config.app_name ?: 'unkown-application'
    def app_version = config.app_version ?: '1.0.0'
    def env = config.env ?: 'prod'

    echo "Application Name is ${app_name}"
    echo "Application Version is ${app_version}"
    echo "Environment is ${env}"
}