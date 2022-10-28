package com.dongchyeon.simplechatapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dongchyeon.simplechatapp.R
import com.dongchyeon.simplechatapp.data.model.Chat
import com.dongchyeon.simplechatapp.presentation.SimpleChatApp.Companion.userName
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val context: Context) :
    ListAdapter<Chat, RecyclerView.ViewHolder>(MessageComparator()) {

    companion object ChatType {
        const val LEFT_MESSAGE = 0
        const val CENTER_MESSAGE = 1
        const val RIGHT_MESSAGE = 2
        const val LEFT_IMAGE = 3
        const val RIGHT_IMAGE = 4
    }

    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)

        return if (chat.type == "ENTER" || chat.type == "LEFT") {
            CENTER_MESSAGE
        } else if (chat.type == "MESSAGE") {
            if (chat.from == userName) RIGHT_MESSAGE
            else LEFT_MESSAGE
        } else if (chat.type == "IMAGE") {
            if (chat.from == userName) RIGHT_IMAGE
            else LEFT_IMAGE
        } else {
            CENTER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LEFT_MESSAGE -> LeftViewHolder.create(parent)
            CENTER_MESSAGE -> CenterViewHolder.create(parent)
            RIGHT_MESSAGE -> RightViewHolder.create(parent)
            LEFT_IMAGE -> LeftImageViewHolder.create(parent)
            RIGHT_IMAGE -> RightImageViewHolder.create(parent)
            else -> CenterViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = getItem(position)
        when (getItemViewType(position)) {
            LEFT_MESSAGE -> (holder as LeftViewHolder).bind(current)
            CENTER_MESSAGE -> (holder as CenterViewHolder).bind(current)
            RIGHT_MESSAGE -> (holder as RightViewHolder).bind(current)
            LEFT_IMAGE -> (holder as LeftImageViewHolder).bind(current, context)
            RIGHT_IMAGE -> (holder as RightImageViewHolder).bind(current, context)
        }
    }

    class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentText = itemView.findViewById<TextView>(R.id.content_text)

        companion object {
            fun create(parent: ViewGroup): RecyclerView.ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_center_item, parent, false)
                return CenterViewHolder(view)
            }
        }

        fun bind(item: Chat) {
            contentText.text = item.content
        }
    }

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText = itemView.findViewById<TextView>(R.id.name_text)
        private val contentText = itemView.findViewById<TextView>(R.id.msg_text)
        private val sendTimeText = itemView.findViewById<TextView>(R.id.send_time_text)

        companion object {
            fun create(parent: ViewGroup): RecyclerView.ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_left_item, parent, false)
                return LeftViewHolder(view)
            }
        }

        fun bind(item: Chat) {
            nameText.text = item.from
            contentText.text = item.content
            sendTimeText.text =
                SimpleDateFormat("hh:mm a", Locale.KOREA).format(Date(item.sendTime))
        }
    }

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentText = itemView.findViewById<TextView>(R.id.msg_text)
        private val sendTimeText = itemView.findViewById<TextView>(R.id.send_time_text)

        companion object {
            fun create(parent: ViewGroup): RecyclerView.ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_right_item, parent, false)
                return RightViewHolder(view)
            }
        }

        fun bind(item: Chat) {
            contentText.text = item.content
            sendTimeText.text =
                SimpleDateFormat("hh:mm a", Locale.KOREA).format(Date(item.sendTime))
        }
    }

    class LeftImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText = itemView.findViewById<TextView>(R.id.name_text)
        private val image = itemView.findViewById<ImageView>(R.id.image_view)
        private val sendTimeText = itemView.findViewById<TextView>(R.id.send_time_text)

        companion object {
            fun create(parent: ViewGroup): RecyclerView.ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_left_image, parent, false)
                return LeftImageViewHolder(view)
            }
        }

        fun bind(item: Chat, context: Context?) {
            val option = MultiTransformation(CenterCrop(), RoundedCorners(8))
            Glide.with(context!!)
                .load(item.content)
                .apply(RequestOptions.bitmapTransform(option))
                .into(image)
            nameText.text = item.from
            sendTimeText.text =
                SimpleDateFormat("hh:mm a", Locale.KOREA).format(Date(item.sendTime))
        }
    }

    class RightImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.image_view)
        private val sendTimeText = itemView.findViewById<TextView>(R.id.send_time_text)

        companion object {
            fun create(parent: ViewGroup): RecyclerView.ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_right_image, parent, false)
                return RightImageViewHolder(view)
            }
        }

        fun bind(item: Chat, context: Context?) {
            val option = MultiTransformation(CenterCrop(), RoundedCorners(8))
            Glide.with(context!!)
                .load(item.content)
                .apply(RequestOptions.bitmapTransform(option))
                .into(image)
            sendTimeText.text =
                SimpleDateFormat("hh:mm a", Locale.KOREA).format(Date(item.sendTime))
        }
    }

    class MessageComparator : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }
}