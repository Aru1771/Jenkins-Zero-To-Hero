def call(Map config) {

    def app_name = config.app_name ?. toString() ?. trim() 
    def test_type = config.test_type ?. toString() ?. trim() ?: "unit"

    if (!app_name) {
        error ("Name is required")
    }

    echo "${app_name} is running the ${test_type}"
    



}