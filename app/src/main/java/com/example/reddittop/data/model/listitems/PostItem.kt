package com.example.reddittop.data.model.listitems

data class PostItem(
        val author_fullname: String?,
        val title : String,
        val clicked: Boolean,
        val num_comments: Int
)
