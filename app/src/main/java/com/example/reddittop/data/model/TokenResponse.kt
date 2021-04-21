package com.example.reddittop.data.model

data class TokenResponse(
    val access_token: String?,
    val token_type: String,
    val device_id: String,
    val expires_in: Long,
    val scope: String)
