package com.example.notisave.ui.More

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notisave.MainActivity
import com.example.notisave.databinding.FragmentBlockNotificationBinding
import com.example.notisave.ui.Message.adapter.ShowAppAdapter
import com.example.notisave.viewmodel.NotificationViewModel


class BlockNotificationFragment :Fragment() {
    lateinit var binding: FragmentBlockNotificationBinding
    private lateinit var appAdapter: ShowAppAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(false)
        binding = FragmentBlockNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Sử dụng binding để truy cập các phần tử trong layout
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleView()
    }

    private fun setUpRecycleView() {
        val viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        val appRecyclerView = binding.rvListApp
        appRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        appAdapter = ShowAppAdapter()
        appRecyclerView.adapter = appAdapter
        appAdapter.onClickItem ={app->
            Log.d("clickAppName", "onViewCreated: ${app.appName}")

        }
        viewModel.allApp.observe(viewLifecycleOwner, Observer { appList ->
            binding.switchCompatAll.setOnCheckedChangeListener { _, isChecked ->
                for (app in appList) {
                    app.isCheck = isChecked
                }
                viewModel.updateAllApps(isChecked)
            }
            appAdapter.submitList(appList)
        })
    }
}