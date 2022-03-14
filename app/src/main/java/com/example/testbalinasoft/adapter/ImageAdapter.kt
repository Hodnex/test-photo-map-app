package com.example.testbalinasoft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testbalinasoft.data.Image
import com.example.testbalinasoft.databinding.GridItemPhotoBinding
import java.text.SimpleDateFormat

class ImageAdapter(private val listener: OnItemClickListener, private val context: Context) :
    ListAdapter<Image, ImageAdapter.ImageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            GridItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageViewHolder(private val binding: GridItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnLongClickListener {
                    val position = adapterPosition
                    listener.onItemLongClick(getItem(position))
                    true
                }
                root.setOnClickListener {
                    val position = adapterPosition
                    listener.onItemClick(getItem(position))
                }
            }
        }

        fun bind(image: Image) {
            binding.apply {
                Glide.with(context).load(image.url).into(imageViewSmallPhoto)
                textViewDate.text = SimpleDateFormat("dd.MM.yyyy").format(image.date * 1000L)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemLongClick(image: Image)
        fun onItemClick(image: Image)
    }

    class DiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Image, newItem: Image) =
            oldItem == newItem
    }
}