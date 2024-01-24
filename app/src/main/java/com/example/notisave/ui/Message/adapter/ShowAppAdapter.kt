package com.example.notisave.ui.Message.adapter




import android.graphics.BitmapFactory
import android.util.Log


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notisave.databinding.ItemShowAllAppBinding
import com.example.notisave.model.AppNameEntity

class ShowAppAdapter : ListAdapter<AppNameEntity, ShowAppAdapter.ViewHolder>(AppNameEntityDiffCallback()) {
    var onClickItem: ((AppNameEntity) -> Unit)? = null
    var onSwitchCheckedChangeListener: ((AppNameEntity) -> Unit)? = null
    inner class ViewHolder(val binding: ItemShowAllAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppNameEntity) {
            binding.tvNameApp.text = app.appName
            val bitmap = BitmapFactory.decodeByteArray(app.icon, 0, app.icon.size)
            Glide.with(binding.ivIcon)
                .load(bitmap)
                .into(binding.ivIcon)

            binding.switchCompat01.isChecked= app.isCheck
            binding.switchCompat01.setOnClickListener{
                // Cập nhật giá trị isCheck trong data model
                app.isCheck =!app.isCheck
                onSwitchCheckedChangeListener?.invoke(app )
            }

            binding.root.setOnClickListener {
                onClickItem?.invoke(app)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemShowAllAppBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = getItem(position)
        holder.bind(app)
    }
}

class AppNameEntityDiffCallback : DiffUtil.ItemCallback<AppNameEntity>() {
    override fun areItemsTheSame(oldItem: AppNameEntity, newItem: AppNameEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppNameEntity, newItem: AppNameEntity): Boolean {
        // You might need to implement a more detailed equality check based on your data structure
        return oldItem == newItem
    }
}


