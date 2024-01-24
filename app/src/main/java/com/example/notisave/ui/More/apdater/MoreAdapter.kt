package com.example.notisave.ui.More.apdater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.notisave.databinding.ItemMoreBinding
import com.example.notisave.ui.More.model.MoreModel

class MoreAdapter(private val listMore: List<MoreModel>?) :RecyclerView.Adapter<MoreAdapter.MoreViewHolder>() {


    var onClickItem : ((MoreModel) -> Unit)?=null
    inner class MoreViewHolder(val binding: ItemMoreBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data :MoreModel){
            binding.ivIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivIcon.context,
                    data.iconMore
                )
            )
            binding.tvName.text= data.nameMore
            binding.root.setOnClickListener{
                onClickItem?.invoke(data)
            }
        }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        val view =ItemMoreBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MoreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMore!!.size
    }

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        holder.bind(listMore!![position])

    }
}