package com.crisis.ui.user.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.crisis.dataLayer.responses.SosResponse
import com.crisis.databinding.CardSosBinding

class MessageAdapter(
    val context: Context,
    private val messageList: List<SosResponse.Data>,
    val shared: SharedPreferences? = null,
) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(val view: CardSosBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = CardSosBinding.inflate(LayoutInflater.from(context), parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        with(holder.view) {
            when (shared?.getString("typeOf", "")) {
                "admin", "NGO" -> {
                    viewButton.isVisible = true
                    details.text = message.message
                    var num = 1
                    viewDetails.isVisible = false
                    viewButton.setOnClickListener {
                        viewDetails.isVisible = num % 2 == 0
                        num++
                    }
                    viewDetails.text = HtmlCompat.fromHtml(
                        "<big>${message.userDetails?.name}</big><br>" +
                                "${message.userDetails?.mail}<br>" +
                                "${message.userDetails?.mobile}<br>",
                        HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS
                    )
                }


                else -> {
                    details.text = message.message

                }
            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
