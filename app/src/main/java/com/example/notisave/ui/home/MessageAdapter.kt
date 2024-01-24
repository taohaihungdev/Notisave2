package com.example.notisave.ui.home


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notisave.databinding.ItemMessageBinding
import com.example.notisave.model.NotificationEntity



class MessageAdapter: ListAdapter<NotificationEntity, MessageAdapter.NotificationViewHolder>(ITEM_COMPARATOR) {
        var onItemClick: ((NotificationEntity) -> Unit)? = null
        inner class NotificationViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: NotificationEntity) {
                val bitmap = BitmapFactory.decodeByteArray(data.icon, 0, data.icon.size)
                binding.titleTextView.text = data.title
                binding.textTextView.text = data.text
                Glide.with(binding.ivIcon)
                    .load(bitmap)
                    .into(binding.ivIcon)
                binding.root.setOnClickListener {
                    onItemClick?.invoke(data)
                }
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
            return NotificationViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


        companion object{
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
