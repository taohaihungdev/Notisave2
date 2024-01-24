package com.example.notisave.ui.Message.adapter


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notisave.databinding.ItemAppIconBinding
import com.example.notisave.model.AppNameEntity
import com.example.notisave.model.NotificationEntity



class IconAppAdapter: ListAdapter<AppNameEntity, IconAppAdapter.NotificationViewHolder>(ITEM_COMPARATOR) {
    var onItemClick: ((AppNameEntity) -> Unit)? = null
    inner class NotificationViewHolder(val binding: ItemAppIconBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AppNameEntity) {
            val bitmap = BitmapFactory.decodeByteArray(data.icon, 0, data.icon.size)
            Glide.with(binding.ivIcon)
                .load(bitmap)
                .into(binding.ivIcon)

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(ItemAppIconBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object{
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<AppNameEntity>() {
            override fun areItemsTheSame(oldItem: AppNameEntity, newItem: AppNameEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AppNameEntity, newItem: AppNameEntity): Boolean {
                return oldItem == newItem
            }

        }
    }
}