package com.example.reddittop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reddittop.R
import com.squareup.picasso.Picasso

class PostDetailFragment : Fragment() {

    private var title: String? = null
    private var author: String? = null
    private var img: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_POST_TITLE).toString()
            author = it.getString(ARG_POST_AUTHOR) ?: "Anonymous"
            img = it.getString(ARG_POST_IMG).toString()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_detail_post, container, false)

        title?.let {
            rootView.findViewById<TextView>(R.id.title_post).text = it
        }
        author.let {
            rootView.findViewById<TextView>(R.id.author_post).text = it
        }
        img.let {
            if (img.contains("https")){
                val im = rootView.findViewById<ImageView>(R.id.img_post)
                Picasso.get().load(it).into(im)
            }
        }
        return rootView
    }

    companion object {
        const val ARG_POST_TITLE = "post_title"
        const val ARG_POST_AUTHOR = "post_author"
        const val ARG_POST_IMG = "post_img"
    }
}