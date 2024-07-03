package com.bayu.bhinneka.ui.list_jajanan

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.databinding.ItemListJajananBinding
import com.bayu.bhinneka.helper.JajananDiffCallback
import com.bayu.bhinneka.helper.JAJANAN_PARCELABLE_EXTRA
import com.bayu.bhinneka.ui.edit_jajanan.EditJajananActivity
import com.bumptech.glide.Glide

class JajananListAdapter: RecyclerView.Adapter<JajananListAdapter.JajananViewHolder>() {

    private var itemJajananList = ArrayList<Jajanan>()
    private lateinit var clicked: ItemClick

    interface ItemClick {
        fun deleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JajananViewHolder {
        val itemBinding = ItemListJajananBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return JajananViewHolder(itemBinding, clicked)
    }

    override fun getItemCount(): Int {
        return itemJajananList.size
    }

    override fun onBindViewHolder(holder: JajananViewHolder, position: Int) {
        val jajanan = itemJajananList[position]
        holder.bind(jajanan)
    }

    fun setList(listJajanan: List<Jajanan>) {
        val diffCallback = JajananDiffCallback(this.itemJajananList, listJajanan)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.itemJajananList.clear()
        this.itemJajananList.addAll(listJajanan)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setDeleteButtonOnCLickListener(clicked: ItemClick) {
        this.clicked = clicked
    }

    inner class JajananViewHolder(
        private val itemBinding: ItemListJajananBinding,
        private val itemClick: ItemClick
    ): RecyclerView.ViewHolder(itemBinding.root) {

            init {
                itemBinding.btnDelete.setOnClickListener {
                    itemClick.deleteClick(adapterPosition)
                }
            }

            fun bind(jajanan: Jajanan) {
                itemBinding.txtJajananName.text = jajanan.name
                jajanan.imagePath?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .centerCrop()
                        .into(itemBinding.imgJajananPicture)
                }

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, EditJajananActivity::class.java)
                    intent.putExtra(JAJANAN_PARCELABLE_EXTRA, jajanan)

                    itemView.context.startActivity(intent)
                }
            }

    }
}