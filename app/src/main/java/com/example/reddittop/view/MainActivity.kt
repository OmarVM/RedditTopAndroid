package com.example.reddittop.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.BaseApplication
import com.example.reddittop.R
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import com.example.reddittop.view.adapter.PostAdapter
import com.example.reddittop.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getAccessTokenUseCase: GetAccessTokenUseCase
    @Inject
    lateinit var getListItemsUseCase: GetListItemsUseCase
    @Inject
    lateinit var mAdapter: PostAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseApplication.getAppComponent().inject(this)

        val mRecyclerViewMain: RecyclerView = findViewById(R.id.rv_main_list)
        mRecyclerViewMain.setHasFixedSize(false)
        mRecyclerViewMain.adapter = mAdapter
        mRecyclerViewMain.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        viewModel.getInfo(getAccessTokenUseCase, getListItemsUseCase)
        viewModel.mListTop.observe(this, {
            mAdapter.updateList(it)
        })
    }
}