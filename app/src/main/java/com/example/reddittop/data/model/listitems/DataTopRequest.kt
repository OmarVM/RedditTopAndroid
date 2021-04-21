package com.example.reddittop.data.model.listitems

data class DataTopRequest(
        val dist : Int,
        val children : List<ChildrenRequest>,
        val after : String?,
        val before : String?
)
