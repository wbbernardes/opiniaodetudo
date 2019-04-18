package com.example.opiniaodetudo.View

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.opiniaodetudo.R

class OnlineFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.layout_reviews_online, null)
        val reviewAdapter = configureList(rootView)
        configureRefresh(rootView, reviewAdapter)
        return rootView
    }
    private fun configureRefresh(
        rootView: View,
        reviewAdapter: OnlineReviewAdapter
    ) {
        val refreshLayout = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        refreshLayout.setOnRefreshListener {
            reviewAdapter.loadList {
                refreshLayout.isRefreshing = false
            }
            true
        }
    }
    private fun configureList(rootView: View): OnlineReviewAdapter {
        val viewManager = LinearLayoutManager(activity!!)
        val viewAdapter = OnlineReviewAdapter()
        val listView = rootView.findViewById<RecyclerView>(R.id.online_review_list)
        listView.layoutManager = viewManager
        listView.adapter = viewAdapter
        return viewAdapter
    }
}