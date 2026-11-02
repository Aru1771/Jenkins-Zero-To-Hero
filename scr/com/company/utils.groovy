package com.company

class Utils {
    static string getAppInfo(string app_name, string app_version, string environment) {
        return """
        Application Name: ${app_name}
        Version: ${app_version} 
        Environment: ${environment}
        """
    }
}

