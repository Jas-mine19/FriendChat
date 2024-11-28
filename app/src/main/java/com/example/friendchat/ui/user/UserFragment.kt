package com.example.friendchat.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.R
import com.example.friendchat.databinding.FragmentUserBinding
import com.example.friendchat.model.ChatWithParticipants
import com.example.friendchat.model.User
import com.example.friendchat.ui.chat.ChatViewModel
import com.example.friendchat.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
            true
        }
        setupRecyclerView()

        userViewModel.users.observe(viewLifecycleOwner) { users ->

            users.forEach { item->
                Log.d("UserFragment", "Observed users: $item")
            }
            userAdapter.updateUsers(users)
        }

        userViewModel.loadUsersFromRoomList()
        userViewModel.syncUsersFromFirebase()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            context= requireContext(),
            users = emptyList(),
            onUserClick = { user -> openOrCreateChat(user) }
        )

        binding.recUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun openOrCreateChat(user: User) {
        Log.d("UserFragment", "Clicked user: $user")
        chatViewModel.createOrGetChat(user) { chatWithParticipants ->
            navigateToMessageFragment(chatWithParticipants)
        }
    }

    private fun navigateToMessageFragment(chatWithParticipants: ChatWithParticipants) {
        val bundle = Bundle().apply {
            putString("chatId", chatWithParticipants.chat.id)
        }
        findNavController().navigate(R.id.messageFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}