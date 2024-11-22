package com.example.friendchat.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.data.AppDatabase
import com.example.friendchat.data.repository.MessageRepository
import com.example.friendchat.databinding.FragmentMessageBinding
import com.example.friendchat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val messageViewModel: MessageViewModel by viewModels()
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var chatId: String
    private lateinit var receiverId: String
    private lateinit var receiverName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatId = requireArguments().getString("chatId") ?: ""
        receiverId = requireArguments().getString("receiverId") ?: ""
        receiverName = requireArguments().getString("receiverName") ?: ""



        setupRecyclerView()
        observeMessages()

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText, "text")
            }
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(emptyList(), "currentUserId") // Replace with actual current user ID
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun observeMessages() {
        messageViewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.updateMessages(messages)
            binding.recyclerViewMessages.scrollToPosition(messages.size - 1)
        }
    }

    private fun sendMessage(content: String, type: String) {
        val message = Message(
            id = System.currentTimeMillis().toString(),
            chatId = chatId,
            senderId = "currentUserId",
            content = content,
            timestamp = System.currentTimeMillis(),
            type = type
        )
        messageViewModel.sendMessage(message)
        binding.editTextMessage.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}