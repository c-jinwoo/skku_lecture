package edu.skku.cs.pa3

data class DataBlockUrl(
    var url: String?= null,
    var engineList: List<String>
)
data class LastAnalysisResult(
    val category: String,
    val result: String,
    val method: String,
    val engine_name: String
)
data class Attributes(
    val last_analysis_results: Map<String, LastAnalysisResult>
)
data class Data(
    val attributes: Attributes
)
data class ApiResponse(
    val data: Data
)

data class Db_homepage(
    val homepage: String
)

const val KAKAO_APPKEY = ""/////////
const val VIRUSTOTAL_APIKEY = ""/////////
const val VIRUSTOTAL_IPREQUEST_URLPREFIX = "https://www.virustotal.com/api/v3/ip_addresses/"
const val VIRUSTOTAL_DOMATINREQUEST_URLPREFIX = "https://www.virustotal.com/api/v3/domains/"
const val AWS_URL = ""/////////
const val DEFAULT_HOMEPAGE_URL = "https://www.google.com"
var homePageUrl = ""
var session_userId = ""
val blockUrlList = ArrayList<DataBlockUrl>()