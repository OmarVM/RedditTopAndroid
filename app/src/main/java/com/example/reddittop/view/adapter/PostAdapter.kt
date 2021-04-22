package com.example.reddittop.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.R
import com.example.reddittop.data.model.listitems.ChildrenRequest

class PostAdapter(var mList: ArrayList<ChildrenRequest>) : RecyclerView.Adapter<PostAdapter.PostHolder>(){

    private lateinit var mOnClickListener: IOnClickListener

    fun setOnClickListener(mIOnClickListener: IOnClickListener){
        mOnClickListener = mIOnClickListener
    }

    fun updateList(newList: List<ChildrenRequest>){
        mList.clear()
        mList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title_post)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val comments: TextView = itemView.findViewById(R.id.comments_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_row, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val mPost = mList[position]
        holder.title.text = mPost.data.title
        val name = mPost.data.author_fullname ?: "Anonymous"
        holder.userName.text = name
        val commentString = holder.itemView.context.getString(R.string.comments_post_full, mPost.data.num_comments.toString())
        holder.comments.text = commentString

        with(holder.itemView){
            this.setOnClickListener {
                mOnClickListener.onClick(mPost)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}