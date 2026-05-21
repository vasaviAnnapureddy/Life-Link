package com.crisis.ui.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crisis.dataLayer.responses.LoginResponse
import com.crisis.databinding.UserCardBinding
import com.crisis.ui.common.spanned

class UserListAdapter(
    private val context: Context,
    private val list: ArrayList<LoginResponse.Users>,
) : RecyclerView.Adapter<UserListAdapter.UserVH>() {

    class UserVH(val bind: UserCardBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserVH(UserCardBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        with(holder.bind) {
            val user = list[position]
            userDetails.text = spanned(
                "<b>Details</b><br>" +
                        "<b>Name :</b>${user.name ?: "—"}<br>" +
                        "<b>Mobile :</b>${user.mobile ?: "—"}<br>" +
                        "<b>Mail :</b>${user.mail ?: "—"}<br>" +
                        "<b>City :</b>${user.city ?: "—"}"
            )
        }
    }
}
