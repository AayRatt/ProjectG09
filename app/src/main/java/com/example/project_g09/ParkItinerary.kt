package com.example.project_g09

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.project_g09.databinding.FragmentItineraryBinding
import com.example.project_g09.databinding.FragmentParkItineraryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParkItinerary : Fragment(R.layout.fragment_park_itinerary) {

    private var _binding: FragmentParkItineraryBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    private val args:ParkItineraryArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doStuff()

    }

    private fun doStuff() {
        val preferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val name = args.fullName
        val address = args.address
        binding.tvFullName.text = name
        binding.tvAddress.text = address
        binding.etDate.setText(preferences.getString("date", ""))
        binding.etNotes.setText(preferences.getString("notes", ""))

        binding.btnSave.setOnClickListener {
            val date = binding.etDate.text.toString()
            val notes = binding.etNotes.text.toString()

            preferences.edit().apply {
                putString("date", date)
                putString("notes", notes)
                apply()
            }
        }

        db.collection("parks")
            .whereEqualTo("fullName", args.fullName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val parkDocRef = db.collection("parks").document(document.id)
                    val date = binding.etDate.text.toString()
                    parkDocRef.update("currentDate", date)
                        .addOnSuccessListener {
                            // Date field updated successfully
                        }
                        .addOnFailureListener { e ->
                            // Handle the error here
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error here
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
