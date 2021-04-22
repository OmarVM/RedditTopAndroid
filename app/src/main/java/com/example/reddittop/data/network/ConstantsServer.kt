package com.example.reddittop.data.network

object ConstantsServer {

    const val URL_BASE = "https://www.reddit.com/"
    const val URL_BASE_OAUTH = "https://oauth.reddit.com/"

    const val CLIENT_ID = ""
    const val CLIENT_SECRET = ""

    const val KEY_GRANT_TYPE = "grant_type"
    const val VALUE_GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
    const val KEY_DEVICE_ID = "device_id"
    const val KEY_SCOPE = "scope"
    const val VALUE_SCOPE_READ = "read"

    //ITEMS
    const val KEY_TIME = "t"
    const val VALUE_TIME_YEAR = "year"
    const val KEY_AFTER = "after"
    const val KEY_BEFORE = "before"
    const val KEY_LIMIT = "limit"
    const val VALUE_LIMIT = 10

    const val VALUE_HEADER_USER_AGENT = ""
}