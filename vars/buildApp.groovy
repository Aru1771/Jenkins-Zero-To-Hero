import com.company.Utils

def call(Map config) {
    def appName = config.app_name?. toString()?.trim()
    def version = config.app_version?. toString()?. trim()
    

    if (!appName) {
        error "Application should not be blank"
    }
    if (!version){
        error "App Version required"
    }

   

    def imageName = com.company.dockerImageUtils.dockerImageName(appName, version)

    echo "Docker Image Name: ${imageName}"
  

}

