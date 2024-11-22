package com.example.friendchat.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.friendchat.databinding.ItemChatBinding
import com.example.friendchat.model.Chat
import com.example.friendchat.model.User
import kotlinx.coroutines.*

class ChatAdapter(
    private var chats: List<Chat>,
    private val getUserById: suspend (String) -> User?,
    private val onChatClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

        fun bind(chat: Chat) {
            // Reset data
            binding.tvUserName.text = "Loading..."
            binding.tvUserMessage.text = "No messages yet"
            binding.tvTime.text = ""

            coroutineScope.launch {
                val user = getUserById(chat.participants)
                binding.tvUserName.text = user?.name ?: "Unknown User"
            }

            binding.tvUserMessage.text = chat.lastMessageId ?: "No messages yet"
            binding.tvTime.text = android.text.format.DateFormat.format(
                "hh:mm a",
                chat.timestamp
            ).toString()

            // Handle click
            binding.root.setOnClickListener {
                onChatClick(chat)
            }
        }

        fun clear() {
            coroutineScope.cancel()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun onViewRecycled(holder: ChatViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    override fun getItemCount(): Int = chats.size

    fun updateChats(newChats: List<Chat>) {
        this.chats = newChats
        notifyDataSetChanged()
    }
}