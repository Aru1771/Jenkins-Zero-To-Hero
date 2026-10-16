def call(Map test) {
    def app_name = test.app_name?.toString()?.trim()
    def test_type = test.test_type?.toString()?.trim() ?: 'unit'

    if (!app_name) {
        error 'Application name is required'
    }

    echo "Application is testing: ${app_name}"
    echo "test type is: ${test_type}"
}

