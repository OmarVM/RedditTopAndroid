package com.example.reddittop.view.adapter

import com.example.reddittop.data.model.listitems.ChildrenRequest

interface IUInteractionsListener {
    fun onClick(item: ChildrenRequest)
    fun dataSetChanged(newList: List<ChildrenRequest>)
}