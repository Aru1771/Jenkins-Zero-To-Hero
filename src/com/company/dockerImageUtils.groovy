package com.company

class dockerImageUtils {

    static String dockerImageName(String appName, String version) {
        return "${appName.toLowerCase()}:${version}"

    }
}

