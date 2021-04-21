package com.example.reddittop.data.network.items

import com.example.reddittop.data.model.listitems.RedditTopRequest
import com.example.reddittop.data.network.ConstantsServer
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestListItemsService {

    @GET("/r/subreddit/top")
    suspend fun requestListItems(
        @Query(ConstantsServer.KEY_TIME) time: String,
        @Query(ConstantsServer.KEY_AFTER) after: String,
        @Query(ConstantsServer.KEY_BEFORE) before: String,
        @Query(ConstantsServer.KEY_LIMIT) limitItems: Int
    ) : RedditTopRequest
}