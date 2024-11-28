package com.example.friendchat.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.databinding.BottomSheetUserBinding
import com.example.friendchat.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserBottomSheetFragment(
    private val onUserSelected: (User) -> Unit // Callback when a user is selected
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetUserBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    companion object {
        const val TAG = "UserBottomSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Bottom sheet onCreateView called")
        setupRecyclerView()

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            Log.d(TAG, "Users fetched for bottom sheet: $users")
            if (users.isNotEmpty()) {
                userAdapter.updateUsers(users)
            } else {
                Log.d(TAG, "No users available to display in the bottom sheet")
            }
        }

        userViewModel.syncUsersFromFirebase()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            context = requireContext(),
            users = emptyList(),
            onUserClick = { user ->
                onUserSelected(user)
                dismiss()
            }
        )

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}