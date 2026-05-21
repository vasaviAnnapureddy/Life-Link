package com.crisis.ui.user.functions

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.crisis.dataLayer.ApiCall
import com.crisis.dataLayer.responses.VideoComplaintResponse
import com.crisis.databinding.AddDialogBinding
import com.crisis.databinding.AddVideoBinding
import com.crisis.databinding.PathLoadBinding
import com.crisis.databinding.ViewCardBinding
import com.crisis.ui.common.backPoint
import com.crisis.ui.common.viewModels.PointerViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddVideoComplaint : Fragment() {
    private var _bind: AddVideoBinding? = null
    private val bind get() = _bind
    private val models by activityViewModels<PointerViewModel>()

    private val dialogBind by lazy {
        AddDialogBinding.inflate(layoutInflater)
    }

    private val dialog by lazy {
        Dialog(requireActivity()).apply {
            setContentView(dialogBind.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _bind = AddVideoBinding.inflate(layoutInflater)
        return bind?.root
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var uriVideo: Uri? = null
        backPoint()
        val register =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                activityResult.data?.data?.let {
                    val expo = ExoPlayer.Builder(requireActivity()).build()
                    dialogBind.playerPoint.player = expo
                    val uri = androidx.media3.common.MediaItem.fromUri(it)
                    expo.prepare()
                    expo.setMediaItem(uri)
                    expo.playWhenReady = true
                    dialogBind.pointerLess.isVisible = true
                    uriVideo = it
                }
            }

        with(bind!!) {

            addView.setOnClickListener {
                dialogBind.pointerLess.isVisible = false
                dialog.show()
            }
        }
        getComplaints()
        with(dialogBind) {
            pickThat.setOnClickListener {
                register.launch(
                    Intent(Intent.ACTION_GET_CONTENT).setType("video/*")
                )
            }
            sendVideo.setOnClickListener {
                val desc = dialogBind.desc.text.toString()
                if (uriVideo == null) {
                    models.toast.value = "Please select a video from gallery"
                } else if (desc.isEmpty())
                    models.toast.value = "Please Give a Description About Situation"
                else {

                    val resolver = requireActivity().contentResolver.openInputStream(uriVideo!!)
                    models.addVideo(
                        userid = this@AddVideoComplaint.id.toRequestBody(),
                        desc = desc.toRequestBody(),
                        multi = MultipartBody.Part.createFormData(
                            "file",
                            "${System.currentTimeMillis()}",
                            resolver?.readBytes()?.toRequestBody()!!
                        )
                    ) {
                        if ("Success".contains(it)) {
                            getComplaints()
                            dialog.dismiss()
                            dialog2.dismiss()
                        }
                    }
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

    private fun getComplaints() {
        models.getVideos(userid = id,
            data = { data ->
                bind?.recyclerView2?.adapter = ViewAdapter(data) {
                    dialog2.show()
                    val exo = ExoPlayer.Builder(requireActivity()).build()
                    exo.setMediaItem(MediaItem.fromUri(it))
                    exo.prepare()
                    exo.playWhenReady = true
                    bindDialogView.loadVideoFrom.player = exo
                }
            })
    }

    val id by lazy {
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("id", "")
            ?: ""
    }


    inner class ViewAdapter(
        val data: ArrayList<VideoComplaintResponse.Data>,
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
                    loadVideo.setOnClickListener { _ ->
                        point(uri + it.path)
                    }
                }
            }
        }

    }

}