package com.example.friendchat.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.friendchat.databinding.ItemChatBinding
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.model.User
import kotlinx.coroutines.*

class ChatAdapter(
    private var chats: List<ChatWithParticipants>,
    private val getUserById: suspend (String) -> User?,
    private val onChatClick: (ChatWithParticipants) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

        fun bind(chatWithParticipants: ChatWithParticipants) {
            val chat = chatWithParticipants.chat
            val participants = chatWithParticipants.participants

            binding.tvUserName.text = "Loading..."
            binding.tvUserMessage.text = "No messages yet"
            binding.tvTime.text = ""

            coroutineScope.launch {
                val participantId = participants.firstOrNull()?.userId ?: return@launch
                val user = getUserById(participantId)
                binding.tvUserName.text = user?.name ?: "Unknown User"
            }

            binding.tvUserMessage.text = chat.lastMessageContent ?: "No messages yet"
            binding.tvTime.text = android.text.format.DateFormat.format(
                "hh:mm a",
                chat.lastMessageTimestamp ?: System.currentTimeMillis()
            ).toString()

            binding.root.setOnClickListener {
                onChatClick(chatWithParticipants)
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

    fun updateChats(newChats: List<ChatWithParticipants>) {
        val diffCallback = ChatDiffCallback(chats, newChats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.chats = newChats
        diffResult.dispatchUpdatesTo(this)
    }

    class ChatDiffCallback(
        private val oldList: List<ChatWithParticipants>,
        private val newList: List<ChatWithParticipants>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].chat.id == newList[newItemPosition].chat.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}