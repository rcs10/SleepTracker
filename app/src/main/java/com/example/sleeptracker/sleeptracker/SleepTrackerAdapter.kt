package com.example.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleeptracker.R
import com.example.sleeptracker.database.OneNight
import com.example.sleeptracker.databinding.ItemViewHolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val Item_Sleep = 1
private const val Item_Header = 2

class SleepTrackerAdapter(val sleepNightClickListener: SleepNightClickListener) :
    ListAdapter<DataItems, RecyclerView.ViewHolder>(OneNightDiffCallBack()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSleepData(listOfData: List<OneNight>?) {
        adapterScope.launch {
            val list = when (listOfData) {
                null -> listOf(DataItems.HeaderClass)
                else -> listOf(DataItems.HeaderClass) + listOfData.map { DataItems.SleepDataClass(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    class SleepViewItemHolder private constructor(private val binding: ItemViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            sleepData: OneNight,
            sleepNightClickListener: SleepNightClickListener
        ) {
            binding.sleepNight = sleepData
            binding.sleepNightClickListener = sleepNightClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): SleepViewItemHolder {
                val inflator = LayoutInflater.from(parent.context)
                val binding: ItemViewHolderBinding =
                    DataBindingUtil.inflate(inflator, R.layout.item_view_holder, parent, false)
                return SleepViewItemHolder(binding)
            }
        }

    }

    class HeaderItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        companion object {
            fun create(parent: ViewGroup): HeaderItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false) as View
                return HeaderItemViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItems.SleepDataClass -> Item_Sleep
            is DataItems.HeaderClass -> Item_Header
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SleepViewItemHolder -> {
                val sleepData = getItem(position) as DataItems.SleepDataClass
                holder.bind(sleepData.oneNight, sleepNightClickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Item_Sleep -> SleepViewItemHolder.create(parent)
            Item_Header -> HeaderItemViewHolder.create(parent)
            else -> throw IllegalArgumentException("ViewHolder not Found")
        }
    }
}

class OneNightDiffCallBack : DiffUtil.ItemCallback<DataItems>() {
    override fun areItemsTheSame(oldItem: DataItems, newItem: DataItems) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DataItems, newItem: DataItems) = oldItem == newItem
}

class SleepNightClickListener(val clickListener: (nightId: Long) -> Unit) {
    fun onClick(oneNight: OneNight) = clickListener(oneNight.nightId)
}

sealed class DataItems {
    abstract val id: Long

    data class SleepDataClass(val oneNight: OneNight) : DataItems() {
        override val id = oneNight.nightId
    }

    object HeaderClass : DataItems() {
        override val id = Long.MIN_VALUE
    }
}