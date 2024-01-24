package com.example.notisave.ui.Message

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
import com.example.notisave.R
import com.example.notisave.databinding.FragmentGroupMessageBinding
import com.example.notisave.ui.Message.adapter.IconAppAdapter
import com.example.notisave.ui.home.MessageAdapter
import com.example.notisave.viewmodel.NotificationViewModel


class GroupMessageFragment : Fragment() {
    lateinit var binding: FragmentGroupMessageBinding
    lateinit var messageAdapter :MessageAdapter
    lateinit var iconAppAdapter :IconAppAdapter
    private lateinit var viewModel: NotificationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(true)
        binding = FragmentGroupMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        handleButtonAddGroup()
        handleSearch()
        setUpRecycleView()
        setUpRecycleViewIconAppGroup()
    }

    private fun setUpRecycleViewIconAppGroup() {
        iconAppAdapter = IconAppAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                reverseLayout = true // Sắp xếp ngược từ cuối lên đầu
                stackFromEnd = true
            }
        binding.rvListApp.apply {
            adapter = iconAppAdapter
            setLayoutManager(layoutManager)
        }
        viewModel.allApp.observe(viewLifecycleOwner, Observer { appList ->
            val filteredAppList = appList.filter { it.isCheck == true }.take(10)
            iconAppAdapter.submitList(filteredAppList)

        })
    }

    private fun setUpRecycleView() {
        messageAdapter = MessageAdapter()
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                reverseLayout = true // Sắp xếp ngược từ cuối lên đầu
                stackFromEnd = true
            }
        binding.rvListMessage.apply {
            adapter = messageAdapter
            setLayoutManager(layoutManager)
        }
        viewModel.getNotificationsGroup().observe(viewLifecycleOwner, Observer { app ->
            Log.d("NotificationsGroupSize", "Size: ${app.size}")
            if(app.isNotEmpty()){
                binding.btnAddGroup.visibility=View.GONE
                binding.rvListApp.visibility=View.GONE
                binding.tvList.visibility=View.GONE
                messageAdapter.submitList(app)
            }
            else{
                binding.btnAddGroup.visibility=View.VISIBLE
                binding.rvListApp.visibility=View.VISIBLE
                binding.tvList.visibility=View.VISIBLE

            }


        })

    }
    private fun handleSearch() {
        binding.ivSearch.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val searchFragment = SearchFragment()
            fragmentTransaction.replace(R.id.fragmentView,searchFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    private fun handleButtonAddGroup() {
        binding.btnAddGroup.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val showAllFragment = ShowAllAppFragment()
            fragmentTransaction.replace(R.id.fragmentView,showAllFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }
    }





