package com.company

class dockerImageUtils {

    static String dockerImageName(string appName, string version) {
        return "${appName.toLowerCase()}:${version}"

    }
}

