package com.experiments.dineatmytime.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.experiments.dineatmytime.databinding.LayoutChipsBinding
import com.experiments.dineatmytime.model.Menu


class ChipAdapter :
        ListAdapter<Menu, ChipAdapter.CustomerViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding =
                LayoutChipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class CustomerViewHolder(private val binding: LayoutChipsBinding) :
            RecyclerView.ViewHolder(binding.root) {



        init {

            binding.chip.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    val item = getItem(position)

                    item.isSelected = isChecked

                }
            }

        }


        fun bind(data: Menu) {

            binding.apply {
                chip.text = data.menuName
                chip.isChecked = data.isSelected

            }
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(old: Menu, aNew: Menu) =
                old.menuId == aNew.menuId

        override fun areContentsTheSame(old: Menu, aNew: Menu) =
                old == aNew
    }


}