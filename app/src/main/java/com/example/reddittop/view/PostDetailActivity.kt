package com.example.reddittop.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reddittop.R

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null){
            val fragment = PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PostDetailFragment.ARG_POST_TITLE, intent.getStringExtra(PostDetailFragment.ARG_POST_TITLE))
                    putString(PostDetailFragment.ARG_POST_AUTHOR, intent.getStringExtra(PostDetailFragment.ARG_POST_AUTHOR))
                    putString(PostDetailFragment.ARG_POST_IMG, intent.getStringExtra(PostDetailFragment.ARG_POST_IMG))
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.post_detail_container, fragment)
                .commit()
        }
    }
}