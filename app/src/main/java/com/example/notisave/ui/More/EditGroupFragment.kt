package com.example.notisave.ui.More

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.notisave.MainActivity
import com.example.notisave.R
import com.example.notisave.databinding.FragmentBlockNotificationBinding
import com.example.notisave.databinding.FragmentEditGroupBinding

class EditGroupFragment :Fragment(){
    lateinit var binding: FragmentEditGroupBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).handleShowBottomNavigation(false)
        binding = FragmentEditGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleButtonAdd()
    }

    private fun handleButtonAdd() {
        binding.btnAddGroup.setOnClickListener{
            val dialog = Dialog(requireContext())
            var view= layoutInflater.inflate(R.layout.dialog_add_group,null)
            var btnCancel = view.findViewById<AppCompatButton>(R.id.btnCancel)
            var btnOk = view.findViewById<AppCompatButton>(R.id.btnOk)
            dialog.setContentView(view)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnOk.setOnClickListener {
//                setUpDelete()
                dialog.dismiss()
                requireActivity().onBackPressed()
            }
            dialog.setOnShowListener {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog.window?.setLayout(width, height)
            }
            dialog.setCancelable(true)
//           dialog.setContentView(view)
            dialog.show()
        }
    }
}