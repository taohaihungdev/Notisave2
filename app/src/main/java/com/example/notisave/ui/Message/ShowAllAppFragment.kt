package com.example.notisave.ui.Message



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notisave.MainActivity
import com.example.notisave.databinding.FragmentShowAllAppBinding
import com.example.notisave.ui.Message.adapter.ShowAppAdapter
import com.example.notisave.viewmodel.NotificationViewModel


class ShowAllAppFragment  :Fragment(){
    lateinit var binding : FragmentShowAllAppBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: ShowAppAdapter
    private lateinit var viewModel: NotificationViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShowAllAppBinding.inflate(layoutInflater)
        (activity as MainActivity).handleShowBottomNavigation(false)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListen()
        setUpRecycleView()
        // Khởi tạo ViewModel
        val viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        viewModel.allApp.observe(viewLifecycleOwner, Observer { appList ->
            binding.switchCompatAll.setOnCheckedChangeListener { _, isChecked ->
                for (app in appList) {
                    app.isCheck = isChecked
                }
                viewModel.updateAllApps(isChecked)
            }
            appAdapter.submitList(appList)
        })
        viewModel.searchResults.observe(viewLifecycleOwner, Observer { searchResults ->
            appAdapter.submitList(searchResults)
        })
        binding.edtSearch.addTextChangedListener { editable ->
            val query = editable.toString()
            viewModel.searchApps(query)
        }
    }

    private fun setUpRecycleView() {
        appAdapter = ShowAppAdapter()
        binding.rvListApp.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appAdapter
        }
        appAdapter.onSwitchCheckedChangeListener = { app ->
            viewModel.updateApp(app)
        }
    }

    private fun setUpListen() {
        binding.ivSearch.setOnClickListener{
            binding.tvTitle.visibility = View.GONE
            binding.edtSearch.visibility= View.VISIBLE
            binding.edtSearch.requestFocus()
            // Xử lý sự kiện khi EditText thay đổi để thực hiện tìm kiếm


        }
    }


}