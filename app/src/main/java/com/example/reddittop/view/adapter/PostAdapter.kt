package com.example.reddittop.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.R
import com.example.reddittop.data.model.listitems.ChildrenRequest
import com.example.reddittop.view.adapter.swipeaction.ItemTouchHelperSwipe
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs

class PostAdapter(var mList: ArrayList<ChildrenRequest>) : RecyclerView.Adapter<PostAdapter.PostHolder>(), ItemTouchHelperSwipe.CallbackOnSwipeAction{

    private lateinit var mIUInteractionsListener: IUInteractionsListener

    fun setOnClickListener(mIUInteractionsListener: IUInteractionsListener){
        this.mIUInteractionsListener = mIUInteractionsListener
    }

    fun updateList(newList: ArrayList<ChildrenRequest>, paginationStatus: Boolean){
        if (paginationStatus){
            mList.addAll(newList)
            mIUInteractionsListener.completedPaginationOp()
            mIUInteractionsListener.dataSetChanged(mList)
            notifyDataSetChanged()
        }else{
            val auxList = newList.clone() as ArrayList<ChildrenRequest>
            mList.clear()
            mList.addAll(auxList)
            notifyDataSetChanged()
        }
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title_post)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val comments: TextView = itemView.findViewById(R.id.comments_post)
        val imgPost: ImageView = itemView.findViewById(R.id.image_row_post)
        val imgReadIndicator: ImageView = itemView.findViewById(R.id.img_read_indicator)
        val timeAgoTxt: TextView = itemView.findViewById(R.id.txt_time_ago)
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

        val timeFromService = mPost.data.created_utc * 1000
        holder.timeAgoTxt.text = holder.itemView.context.getString(R.string.str_time_ago_day, calculateTimeCreated(timeFromService))

        if (mPost.data.clicked){
            holder.imgReadIndicator.visibility = View.INVISIBLE
        }

        if (mPost.data.thumbnail.contains("https")){
            Picasso.get().load(mPost.data.thumbnail).into(holder.imgPost)

            with(holder.imgPost){
                this.setOnClickListener {
                    mIUInteractionsListener.onClickImg(mPost.data.thumbnail)
                }
            }
        }

        with(holder.itemView){
            this.setOnClickListener {
                mIUInteractionsListener.onClick(mPost)

                mPost.data.clicked = true
                mIUInteractionsListener.dataSetChanged(mList)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onSwipedItem(position: Int) {
        mList.remove(mList[position])
        mIUInteractionsListener.dataSetChanged(mList)
    }

    private fun calculateTimeCreated(timePost: Long): String{
        val current = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val diffInMill = abs(timePost - current)
        val timeAgo = TimeUnit.DAYS.convert(diffInMill, TimeUnit.MILLISECONDS)
        return timeAgo.toString()
    }
}