package com.example.friendchat.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.databinding.FragmentUserBinding
import com.example.friendchat.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels() // Use Hilt for ViewModel injection
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

        setupRecyclerView()

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }
        userViewModel.syncUsersFromFirebase()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            users = emptyList(),
            onUserClick = { user -> openUserProfile(user) }
        )

        binding.recUser.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun openUserProfile(user: User) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}