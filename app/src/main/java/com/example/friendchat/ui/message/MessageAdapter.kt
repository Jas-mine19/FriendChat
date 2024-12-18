package com.example.friendchat.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.friendchat.databinding.ItemMessageReceiveBinding
import com.example.friendchat.databinding.ItemMessageSentBinding
import com.example.friendchat.model.Message

class MessageAdapter(
    private var messages: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageReceiveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    inner class SentMessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            when (message.type) {
                "text" -> {
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.ivMedia.visibility = View.GONE
                    binding.tvMessage.text = message.content
                }

                "photo", "video" -> {
                    binding.tvMessage.visibility = View.GONE
                    binding.ivMedia.visibility = View.VISIBLE
                    Glide.with(binding.root.context)
                        .load(message.content)
                        .into(binding.ivMedia)
                }
            }

            binding.tvTimestamp.text = android.text.format.DateFormat.format(
                "hh:mm a",
                message.timestamp
            )
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            when (message.type) {
                "text" -> {
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.ivMedia.visibility = View.GONE
                    binding.tvMessage.text = message.content
                }

                "photo", "video" -> {
                    binding.tvMessage.visibility = View.GONE
                    binding.ivMedia.visibility = View.VISIBLE
                    Glide.with(binding.root.context)
                        .load(message.content)
                        .into(binding.ivMedia)
                }
            }

            binding.tvTimestamp.text = android.text.format.DateFormat.format(
                "hh:mm a",
                message.timestamp
            )
        }
    }
}