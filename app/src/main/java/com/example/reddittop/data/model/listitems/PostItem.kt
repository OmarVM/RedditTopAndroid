package com.example.reddittop.data.model.listitems

data class PostItem(
        val author_fullname: String?,
        val title : String,
        var clicked: Boolean,
        val num_comments: Int,
        val media_metadata: HashMap<String,DataImages>?,
        val gallery_data: ItemsGallery,
        var thumbnail: String,
        val created: Long,
        val created_utc: Long
)
