package com.example.noteapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentKullaniciBinding
import com.example.noteapp.databinding.FragmentKullaniciKayitBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class KullaniciKayitFragment : Fragment() {

    private var _binding: FragmentKullaniciKayitBinding? = null
    private val binding get() = _binding!!

    //Firebase auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciKayitBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            kayitOl(it)

        }

    }


    fun kayitOl(view: View) {

        val email = binding.saveEmailEditText.text.toString()
        val password = binding.savePasswordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //kullanici olusturuldu
                    Toast.makeText(requireContext(), "New User Added", Toast.LENGTH_LONG).show()
                    val action =
                        KullaniciKayitFragmentDirections.actionKullaniciKayitFragmentToNotesFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }

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