package com.bayu.bhinneka.helper

import androidx.recyclerview.widget.DiffUtil
import com.bayu.bhinneka.data.model.Jajanan


class DiffCallback(private val mOldList: List<Jajanan>, private val mNewList: List<Jajanan>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldList.size
    }

    override fun getNewListSize(): Int {
        return mNewList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition] == mNewList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]

        return oldItem.name == newItem.name
    }
}