package com.example.notisave.ui.home


import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notisave.databinding.ItemNotificationAppNameBinding
import com.example.notisave.model.NotificationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeAdapter: androidx.recyclerview.widget.ListAdapter<NotificationEntity, HomeAdapter.NotificationViewHolder>(ITEM_COMPARATOR) {
    var onItemClick: ((NotificationEntity) -> Unit)? = null
//    var titleAdapter = TitleAdapter()


    inner class NotificationViewHolder(val binding: ItemNotificationAppNameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationEntity) {
            val bitmap = BitmapFactory.decodeByteArray(data.icon, 0, data.icon.size)
            binding.tvNameApplication.text=data.appName
            binding.tvTime.text = data.day
            Glide.with(binding.ivIcon)
                .load(bitmap)
                .into(binding.ivIcon)
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(ItemNotificationAppNameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
