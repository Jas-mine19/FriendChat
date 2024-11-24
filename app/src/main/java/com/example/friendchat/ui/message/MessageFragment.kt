package com.example.friendchat.ui.message

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.friendchat.databinding.FragmentMessageBinding
import com.example.friendchat.model.Message
import com.example.friendchat.ui.chat.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private val messageViewModel: MessageViewModel by viewModels()

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatId = arguments?.getString("chatId") ?: ""

        setupRecyclerView()
        setupSendButton()
        setupAttachmentHandlers()

        messageViewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.updateMessages(messages)
        }

        messageViewModel.loadMessagesForChat(chatId)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(
            messages = emptyList(),
            currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun setupSendButton() {
        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.editTextMessage.text?.clear()
            }
        }
    }

    private fun setupAttachmentHandlers() {
        binding.buttonPhoto.setOnClickListener {
            openGalleryForMedia()
        }
    }

    private fun openGalleryForMedia() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/* video/*"
        startActivityForResult(intent, REQUEST_MEDIA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let { uri ->
                uploadMediaToFirebase(uri)
            }
        }
    }

    private fun uploadMediaToFirebase(uri: Uri) {
        val storageReference = FirebaseStorage.getInstance()
            .reference.child("media/${System.currentTimeMillis()}")

        val uploadTask = storageReference.putFile(uri)
        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                val type = if (uri.toString().contains("image")) "photo" else "video"
                sendMediaMessage(downloadUri.toString(), type)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to upload media", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMediaMessage(mediaUrl: String, type: String) {
        val message = Message(
            id = System.currentTimeMillis().toString(),
            chatId = chatId,
            senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            content = mediaUrl,
            type = type,
            timestamp = System.currentTimeMillis()
        )
        messageViewModel.sendMessage(message)
    }

    private fun sendMessage(text: String) {
        val message = Message(
            id = System.currentTimeMillis().toString(),
            chatId = chatId,
            senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            content = text,
            type = "text",
            timestamp = System.currentTimeMillis()
        )
        messageViewModel.sendMessage(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_MEDIA = 1
    }
}