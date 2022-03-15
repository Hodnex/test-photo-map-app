package com.example.testbalinasoft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testbalinasoft.data.Comment
import com.example.testbalinasoft.databinding.ItemCommentBinding
import java.text.SimpleDateFormat

class CommentAdapter(val listener: OnItemClickListener) :
    ListAdapter<Comment, CommentAdapter.CommentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                listener.onItemLongClick(getItem(position))
                true
            }
        }

        fun bind(comment: Comment) {
            binding.apply {
                textViewComment.text = comment.text
                textViewCommentTime.text =
                    SimpleDateFormat("dd.MM.yyyy hh:mm").format(comment.date * 1000L)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemLongClick(comment: Comment)
    }

    class DiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment) =
            oldItem == newItem
    }
}