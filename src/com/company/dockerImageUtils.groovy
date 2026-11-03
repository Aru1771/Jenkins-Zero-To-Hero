package com.company

class dockerImageUtils {

    static sreing dockerImageName(string appName, string version) {
        return "${appName.toLowerCase()}:${version}"

    }
}

