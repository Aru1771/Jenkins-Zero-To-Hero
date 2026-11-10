def call(Map config) {
    
    def app_name = config.app_name ?. toString() ?. trim() 
    def version = config.version ?. toString() ?. trim() ?: '1.0'
    def environment = config.environment ?. toString() ?. trim()

    if (!app_name) {
        error ('Name should not be blank')
    }

    if (!environment) {
        error ('environment should not be Blank')
    }

    echo "${app_name} version ${version} is deployed in ${environment}"
}