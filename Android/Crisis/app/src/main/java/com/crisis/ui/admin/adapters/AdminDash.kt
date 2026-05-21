package com.crisis.ui.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crisis.databinding.CardPointBinding
import com.crisis.ui.admin.models.Core
import com.crisis.ui.common.spanned

class AdminDash(
    private val context: Context, private val array: ArrayList<Core>, val click: (Int) -> Unit,
) : RecyclerView.Adapter<AdminDash.ViewPort>() {
    class ViewPort(
        val item: CardPointBinding,
    ) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewPort(
        CardPointBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: ViewPort, position: Int) {
        with(holder.item) {
            array[position].let {
                imagePoint.setImageResource(
                    it.resId
                )
                if (it.name.length >= 10) {
                    textPoint.textSize = 10f
                }
                textPoint.text = spanned(it.name)
            }
            cardPoint.setOnClickListener {
                click(position)
            }
        }
    }

}