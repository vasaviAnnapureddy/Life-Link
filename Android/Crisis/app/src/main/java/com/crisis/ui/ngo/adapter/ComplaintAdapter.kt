package com.crisis.ui.ngo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.crisis.dataLayer.ApiCall
import com.crisis.dataLayer.responses.ComplaintResponse
import com.crisis.databinding.ComplaintCardBinding
import com.crisis.ui.common.spanned

class ComplaintAdapter(
    val context: Context,
    val array: ArrayList<ComplaintResponse.Data>,
    val updateOfPoint: (id: ComplaintResponse.Data) -> Unit,
) : RecyclerView.Adapter<ComplaintAdapter.Complaints>() {
    class Complaints(
        val bind: ComplaintCardBinding,
    ) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Complaints(
        bind = ComplaintCardBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
    )

    private val url get() = ApiCall.BASEURL
    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: Complaints, position: Int) {
        with(holder.bind) {
            array[position].let {
                headImage.load(url + it.imagePath)
                complaintDetails.text = spanned("<b>${it.title}</b><br>${it.description}")
                root.setOnClickListener { _ ->
                    updateOfPoint(it)
                }
            }
        }
    }
}