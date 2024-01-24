//package com.example.notisave.ui.Message.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.ListAdapter
//import com.example.notisave.database.dao.GroupAddDao
//import com.example.notisave.databinding.ItemListGroupBinding
//import com.example.notisave.model.GroupAppEntity
//
//// GroupAdapter.kt
//class GroupAdapter(private val onItemClick: (GroupAppEntity) -> Unit) :
//    ListAdapter<GroupAddDao, GroupAdapter.GroupViewHolder>(GroupDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
//        val binding = ItemListGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return GroupViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
//        val currentGroup = getItem(position)
//        holder.bind(currentGroup, onItemClick)
//    }
//
//    inner class GroupViewHolder(private val binding: ListItemGroupBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(group: Group, onItemClick: (Group) -> Unit) {
//            binding.groupName.text = group.groupName
//
//            // Thêm logic xử lý sự kiện cho các nút sửa và xóa ở đây (nếu cần)
//
//            binding.root.setOnClickListener { onItemClick(group) }
//        }
//    }
//
//    private class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
//        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
//            return oldItem.groupId == newItem.groupId
//        }
//
//        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
