package com.example.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.databinding.FragmentNoteDetailBinding
import com.example.noteapp.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NoteDetailFragment : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!
    val noteList: ArrayList<Note> = arrayListOf()

    private var adapter: NoteAdapter? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fireStoreVerileriAl()

        binding.editNoteSaveButton.setOnClickListener {
            fireStoreVerileriGuncelle()

        }
        binding.editNoteDeleteButton.setOnClickListener {
            fireStoreVeriSil()
        }


    }

    private fun fireStoreVerileriGuncelle() {
        val noteId = arguments?.getString("noteId")

        if (noteId != null && auth.currentUser != null) {
            val noteTitle = binding.editNoteTitleEditText.text.toString()
            val noteContent = binding.editNoteContentEditText.text.toString()

            val notePostMap = hashMapOf<String, Any>(
                "title" to noteTitle,
                "content" to noteContent
            )

            db.collection("Notes").document(noteId).update(notePostMap)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Note updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error updating note: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(requireContext(), "Note ID or user is null", Toast.LENGTH_SHORT).show()
        }
        val action = NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun fireStoreVerileriAl() {
        val noteId = arguments?.getString("noteId")

        if (noteId != null) {
            db.collection("Notes").document(noteId)
                .addSnapshotListener { documentSnapshot, error ->
                    if (error != null) {
                        Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    } else if (documentSnapshot != null && documentSnapshot.exists()) {
                        val title = documentSnapshot.getString("title") ?: "No title"
                        val content = documentSnapshot.getString("content") ?: "No content"

                        // Null kontrolü yaparak binding'e erişin
                        if (_binding != null) {
                            binding.editNoteTitleEditText.setText(title)
                            binding.editNoteContentEditText.setText(content)
                        }
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Note ID is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fireStoreVeriSil() {
        val noteId = arguments?.getString("noteId")
        if (noteId != null) {
            db.collection("Notes").document(noteId).delete()
        }
        val action = NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}