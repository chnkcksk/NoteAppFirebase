package com.example.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.model.Note
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotesFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var popup: PopupMenu
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var adapter: NoteAdapter? = null

    val noteList: ArrayList<Note> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            floatingButtonTiklandi(it)
        }

        fireStoreVerileriAl()

        adapter = NoteAdapter(noteList)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = adapter


    }

    private fun fireStoreVerileriAl() {
        val currentUserEmail = auth.currentUser?.email
        db.collection("Notes").whereEqualTo("email", currentUserEmail)
            .orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                println(error.localizedMessage)
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        noteList.clear()

                        val documents = value.documents
                        for (document in documents) {
                            val content = document.get("content") as? String ?: "No content"
                            val title = document.get("title") as? String ?: "No title"
                            val email = document.get("email") as? String ?: "Unknown email"
                            val id = (document.get("id") as? Long)?.toInt() ?: 0


                            val note = Note(title, content, email, id)
                            noteList.add(note)
                        }
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun floatingButtonTiklandi(view: View) {
        val popup = PopupMenu(requireContext(), binding.floatingActionButton)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.my_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.addNoteItem) {
            val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment()
            Navigation.findNavController(requireView()).navigate(action)
        } else if (item?.itemId == R.id.logoutItem) {
            auth.signOut()
            val action = NotesFragmentDirections.actionNotesFragmentToKullaniciFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        return true
    }

}