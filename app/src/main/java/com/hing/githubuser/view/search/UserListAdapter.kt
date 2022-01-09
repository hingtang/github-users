package com.hing.githubuser.view.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hing.githubuser.databinding.SearchUserItemBinding
import com.hing.githubuser.domain.search.UserData
import com.hing.githubuser.view.extensions.loadImage

/**
 * [RecyclerView.Adapter] that can display a [UserData].
 * TODO: Replace the implementation with code for your data type.
 */
class UserListAdapter(
    private val users: MutableList<UserData> = mutableListOf(),
    private val onItemClick: (pos: Int) -> Unit
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    var isLoadMore = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(users[position])
    }

    override fun getItemCount(): Int = users.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(data: List<UserData>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<UserData>) {
        val posStart = users.size
        users.addAll(data)
        notifyItemRangeInserted(posStart, data.size)
    }

    fun updateData(data: List<UserData>) {
        if (isLoadMore) {
            addData(data)
        } else {
            refreshData(data)
        }
    }

    inner class ViewHolder(
        private val binding: SearchUserItemBinding,
        onItemClick: (pos: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bindData(user: UserData) {
            binding.imgvAvatar.loadImage(user.avatarUrl)
            binding.tvName.text = user.name
            binding.tvUrl.text = user.url
        }
    }

}
