package edu.skku.cs.pa3

import java.net.InetAddress
import java.net.URL

// 도메인 -> 전체주소
fun getFullUrlWithProtocol(urlString: String): String {
    try {
        val url = if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
            URL(urlString)
        }
        else {
            URL("https://$urlString")
        }
        return url.toString()
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

// 전체주소 -> 도메인
fun getDomainFromUrl(urlString: String): String? {
    try{
        val url = URL(urlString)
        val host = url.host
        return host.removePrefix("www.")
    }
    catch (e: Exception){
        e.printStackTrace()
    }
    return null
}

// IP 유효성 검사
fun isIpAddress(string: String): Boolean{
    return try{
        InetAddress.getByName(string)
        true
    }
    catch(e: Exception){
        false
    }
}