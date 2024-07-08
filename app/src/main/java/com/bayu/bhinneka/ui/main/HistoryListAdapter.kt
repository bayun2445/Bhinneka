package com.bayu.bhinneka.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.databinding.ItemListHistoryBinding
import com.bayu.bhinneka.helper.HISTORY_EXTRA
import com.bayu.bhinneka.helper.HistoryDiffCallback
import com.bayu.bhinneka.ui.history.HistoryActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryListAdapter: RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private var itemHistoryList = ArrayList<History>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemBinding = ItemListHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HistoryViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemHistoryList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = itemHistoryList[position]

        holder.bind(history)
    }

    fun setList(listHistory: List<History>) {
        val diffCallback = HistoryDiffCallback(this.itemHistoryList, listHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.itemHistoryList.clear()
        this.itemHistoryList.addAll(listHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class HistoryViewHolder(
        private val itemBinding: ItemListHistoryBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(history: History) {
            itemBinding.txtJajananName.text = history.resultJajananName
            itemBinding.txtDate.text = SimpleDateFormat(
                "dd/MMM/yyyy",
                Locale.US
            ).format(history.timeStamp)

            Glide.with(itemView.context)
                .load(history.imgPath)
                .centerCrop()
                .into(itemBinding.imgJajananPicture)

            itemView.setOnClickListener {
                itemView.context.startActivity(
                    Intent(itemView.context, HistoryActivity::class.java)
                        .putExtra(HISTORY_EXTRA, history)
                )
            }
        }
    }
}