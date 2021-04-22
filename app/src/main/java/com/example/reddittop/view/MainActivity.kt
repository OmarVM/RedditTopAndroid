package com.example.reddittop.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.BaseApplication
import com.example.reddittop.R
import com.example.reddittop.data.model.listitems.ChildrenRequest
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import com.example.reddittop.view.adapter.IOnClickListener
import com.example.reddittop.view.adapter.PostAdapter
import com.example.reddittop.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IOnClickListener {

    @Inject
    lateinit var getAccessTokenUseCase: GetAccessTokenUseCase
    @Inject
    lateinit var getListItemsUseCase: GetListItemsUseCase
    @Inject
    lateinit var mAdapter: PostAdapter

    private val viewModel: MainViewModel by viewModels()

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseApplication.getAppComponent().inject(this)

        if(findViewById<NestedScrollView>(R.id.post_detail_container) != null){
            twoPane = true
        }

        val mRecyclerViewMain: RecyclerView = findViewById(R.id.rv_main_list)
        mRecyclerViewMain.setHasFixedSize(false)
        mAdapter.setOnClickListener(this)
        mRecyclerViewMain.adapter = mAdapter
        mRecyclerViewMain.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        viewModel.getInfo(getAccessTokenUseCase, getListItemsUseCase)
        viewModel.mListTop.observe(this, {
            mAdapter.updateList(it)
        })
    }

    override fun onClick(item: ChildrenRequest) {

        if (twoPane){
            val fragment = PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PostDetailFragment.ARG_POST_TITLE, item.data.title)
                    putString(PostDetailFragment.ARG_POST_AUTHOR, item.data.author_fullname)
                    putString(PostDetailFragment.ARG_POST_IMG, "")
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.post_detail_container, fragment)
                .commit()
        }else{
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra(PostDetailFragment.ARG_POST_TITLE, item.data.title)
                putExtra(PostDetailFragment.ARG_POST_AUTHOR, item.data.author_fullname)
                putExtra(PostDetailFragment.ARG_POST_IMG, "")
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when(item.itemId){
            android.R.id.home -> {
                navigateUpTo(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}