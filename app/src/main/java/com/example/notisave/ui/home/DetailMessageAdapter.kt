package com.example.notisave.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notisave.databinding.ItemDetailMessageBinding
import com.example.notisave.model.NotificationEntity


class DetailMessageAdapter : ListAdapter<NotificationEntity, DetailMessageAdapter.DetailMessageViewHolder>(ITEM_COMPARATOR) {
    var onItemClick: ((NotificationEntity) -> Unit)? = null

    inner class DetailMessageViewHolder(val binding: ItemDetailMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationEntity, showDay: Boolean) {
            if (showDay) {
                binding.tvDay.text = data.day
                binding.tvDay.visibility = View.VISIBLE
            } else {
                binding.tvDay.visibility = View.GONE
            }
            binding.tvHour.text = data.hour
            binding.tvTitle.text = data.title
            binding.tvText.text = data.text
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMessageViewHolder {
        return DetailMessageViewHolder(ItemDetailMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DetailMessageViewHolder, position: Int) {
        val currentNotification = getItem(position)
        val showDay = position == 0 || currentNotification.day != getItem(position - 1).day
        holder.bind(currentNotification, showDay)
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

