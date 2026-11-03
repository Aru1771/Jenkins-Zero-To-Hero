import com.company.dockerImageUtils

def call(Map test) {
    def app_name = test.app_name?.toString()?.trim()
    def app_version = test.app_version?.toString()?.trim() ?: '1.0'

    if (!app_name) {
        error 'Application name is required'
    }

    if (!app_version) {
        error 'Tets type is required'
    }

    def imageName = dockerImageUtils.dockerImageName(app_name, app_version)

    echo "Docker Image Name: ${imageName}"

    
}

