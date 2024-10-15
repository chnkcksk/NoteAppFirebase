package com.example.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveNoteButton.setOnClickListener {

            notKayitIslemi(it)

        }

    }

    fun notKayitIslemi(view: View) {

        val uuid = UUID.randomUUID()

        val noteTitle = binding.addNoteTitleEditText.text.toString()
        val noteContent = binding.addNoteContentEditText.text.toString()

        if (auth.currentUser != null) {
            val notePostMap = hashMapOf<String, Any>()
            notePostMap.put("title", noteTitle)
            notePostMap.put("content", noteContent)
            notePostMap.put("email", auth.currentUser!!.email.toString())
            notePostMap.put("date", Timestamp.now())
            notePostMap.put("id", uuid)

            db.collection("Notes").add(notePostMap).addOnSuccessListener { documentReference ->
                //veri database e yuklendi
                Toast.makeText(requireContext(), "Not database e yuklendi", Toast.LENGTH_LONG)
                    .show()
                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}