package com.example.friendchat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.R
import com.example.friendchat.databinding.FragmentChatBinding
import com.example.friendchat.model.Chat
import com.example.friendchat.ui.message.MessageFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            chatViewModel.loadChatsForUser(currentUserUid)
        }

        chatViewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatAdapter.updateChats(chats.distinctBy { it.participants }) // Ensure unique chats
        }


        binding.fabCreateChat.setOnClickListener {

        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(
            chats = emptyList(),
            getUserById = { userId -> chatViewModel.getUserById(userId) },
            onChatClick = { chat -> openChat(chat) }
        )

        binding.recChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

    private fun openChat(chat: Chat) {
        val bundle = Bundle().apply {
            putString("chatId", chat.id)
            putString("receiverId", chat.participants) // Adjust as per your model
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MessageFragment::class.java, bundle)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}