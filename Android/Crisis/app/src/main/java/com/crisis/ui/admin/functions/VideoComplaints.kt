package com.crisis.ui.admin.functions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.crisis.dataLayer.ApiCall
import com.crisis.dataLayer.responses.UserDetailsVideoComplaint
import com.crisis.databinding.PathLoadBinding
import com.crisis.databinding.VideoComplaintsBinding
import com.crisis.databinding.ViewCardBinding
import com.crisis.ui.common.spanned
import com.crisis.ui.common.viewModels.PointerViewModel

class VideoComplaints : Fragment() {
    private var _bind: VideoComplaintsBinding? = null
    private val bind get() = _bind
    private val models by activityViewModels<PointerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _bind = VideoComplaintsBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bind!!) {
            backBtnVideo.setOnClickListener { findNavController().navigateUp() }
            models.getVideoComplaints {
                cycle8.adapter= ViewAdapter(it){
                    dialog2.show()
                    val exo = ExoPlayer.Builder(requireActivity()).build()
                    exo.setMediaItem(MediaItem.fromUri(it))
                    exo.prepare()
                    exo.playWhenReady = true
                    bindDialogView.loadVideoFrom.player = exo
                }
            }
        }
    }
    private val bindDialogView by lazy {
        PathLoadBinding.inflate(layoutInflater)
    }
    private val dialog2 by lazy {
        Dialog(requireActivity()).apply {
            setContentView(bindDialogView.root)
            setOnDismissListener {
                bindDialogView.loadVideoFrom.player = null
            }

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
    }

    inner class ViewAdapter(
        val data: ArrayList<UserDetailsVideoComplaint.Data>,
        val point: (String) -> Unit,
    ) :
        RecyclerView.Adapter<ViewAdapter.ViewPoint>() {
        private val uri = ApiCall.BASEURL

        inner class ViewPoint(val item: ViewCardBinding) :
            RecyclerView.ViewHolder(item.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = (
                ViewPoint(
                    ViewCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
                )

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: ViewPoint, position: Int) {
            with(holder.item) {
                data[position].let {
                    statePoint.isVisible = true
                    loadVideo.setOnClickListener { _ ->
                        point(uri + it.path)
                    }
                    var num = 1

                    iamSelector.setOnClickListener {
                        num++
                        statePoint.isVisible = num % 2 == 0
                    }
                    userDetails.text = spanned(
                        "<b>User Details</b> <br>" +
                                "<b>Name : </b>${it.user?.name}<br>"
                                + "<b>Mail : </b>${it.user?.mail}<br>"
                                + "<b>Mobile : </b>${it.user?.mobile}"
                    )
                }
            }
        }

    }
}