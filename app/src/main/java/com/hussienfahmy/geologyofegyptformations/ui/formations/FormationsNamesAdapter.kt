package com.hussienfahmy.geologyofegyptformations.ui.formations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hussienfahmy.geologyofegyptformations.databinding.RowItemFormationBinding
import com.hussienfahmy.geologyofegyptformations.model.Formation
import com.hussienfahmy.geologyofegyptformations.utils.searchFormationByName
import com.hussienfahmy.geologyofegyptformations.utils.toastNotAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormationsNamesAdapter(
    private val clickListener: OnFormationItemClickListener,
) : ListAdapter<String, FormationsNamesAdapter.MyViewHolder>(StringDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = currentList[position]
        holder.bind(currentItem, clickListener)
    }

    override fun getItemId(position: Int): Long {
        return currentList[position].hashCode().toLong()
    }

    class MyViewHolder private constructor(
        private val binding: RowItemFormationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            formationName: String,
            onFormationItemClickListener: OnFormationItemClickListener
        ) {
            binding.formation = formationName
            binding.listener = onFormationItemClickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) = MyViewHolder(
                RowItemFormationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

class StringDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}

class OnFormationItemClickListener(private val listener: (formationName: String) -> Unit) {
    fun onClick(formationName: String) = listener(formationName)
}