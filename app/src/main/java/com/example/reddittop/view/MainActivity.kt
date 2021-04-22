package com.example.reddittop.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittop.BaseApplication
import com.example.reddittop.R
import com.example.reddittop.data.model.listitems.ChildrenRequest
import com.example.reddittop.usecase.GetAccessTokenUseCase
import com.example.reddittop.usecase.GetListItemsUseCase
import com.example.reddittop.view.adapter.IUInteractionsListener
import com.example.reddittop.view.adapter.PostAdapter
import com.example.reddittop.view.adapter.swipeaction.ItemTouchHelperSwipe
import com.example.reddittop.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IUInteractionsListener {

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
        viewModel.setDependencies(getAccessTokenUseCase, getListItemsUseCase)

        val emptyView: LinearLayout = findViewById(R.id.ll_empty_view)
        val mRecyclerViewMain: RecyclerView = findViewById(R.id.rv_main_list)
        val loading: ProgressBar = findViewById(R.id.m_progress_circular)

        mRecyclerViewMain.setHasFixedSize(false)
        mAdapter.setOnClickListener(this)
        mRecyclerViewMain.adapter = mAdapter
        mRecyclerViewMain.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val mItemTouchHelper = ItemTouchHelper(ItemTouchHelperSwipe(mAdapter, applicationContext))
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewMain)

        viewModel.getInfo()
        viewModel.mListTop.observe(this, {
            if (it.isNotEmpty()){
                mRecyclerViewMain.visibility = View.VISIBLE
                mAdapter.updateList(it)
                loading.visibility = View.GONE
            }else{
                mRecyclerViewMain.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                loading.visibility = View.GONE
            }
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

    override fun dataSetChanged(newList: List<ChildrenRequest>) {
        viewModel.updateLiveDataList(newList)
    }

    private fun dismissAllContent(){
        viewModel.updateLiveDataList(arrayListOf())
        if (twoPane){
            val fragments = supportFragmentManager.fragments
            if (fragments.size > 0){
                for (fragment in fragments){
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater: MenuInflater = menuInflater
        mInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when(item.itemId){
            android.R.id.home -> {
                navigateUpTo(Intent(this, MainActivity::class.java))
                true
            }
            R.id.op_dismiss_all -> {
                dismissAllContent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}