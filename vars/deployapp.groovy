def call(Map config) {

    if (!config.app_name?.toString()?.trim()) {
        error: "Application Name is required"
    }
  

    
    def app_version = config.app_version ?.toString() ?.trim() ?: '1.0.0'


    echo "Application Name is ${app_name}"
    echo "Application Version is ${app_version}"
    echo "Environment is ${env}"
}