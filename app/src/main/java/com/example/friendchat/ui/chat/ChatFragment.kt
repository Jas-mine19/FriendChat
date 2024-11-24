package com.example.friendchat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.R
import com.example.friendchat.databinding.FragmentChatBinding
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.ui.message.MessageFragment
import com.example.friendchat.ui.user.UserBottomSheetFragment
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
        chatViewModel.loadChatsFromRoom()
        chatViewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatAdapter.updateChats(chats)
        }

//        binding.fabCreateChat.setOnClickListener {
//            showUserBottomSheet()
//        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(
            chats = emptyList(),
            getUserById = { userId -> chatViewModel.getUserById(userId) },
            onChatClick = { chatWithParticipants -> openChat(chatWithParticipants) }
        )
        binding.recChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

    private fun showUserBottomSheet() {
        val userBottomSheet = UserBottomSheetFragment { selectedUser ->
            chatViewModel.createOrGetChat(selectedUser) { chat ->
                openChat(chat)
            }
        }
        userBottomSheet.show(childFragmentManager, UserBottomSheetFragment.TAG)
    }

    private fun openChat(chatWithParticipants: ChatWithParticipants) {
        val bundle = Bundle().apply {
            putString("chatId", chatWithParticipants.chat.id)
        }
        findNavController().navigate(R.id.action_chatFragment_to_messageFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}