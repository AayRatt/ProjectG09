package com.example.project_g09

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_g09.databinding.FragmentItineraryBinding
import com.example.project_g09.databinding.FragmentViewParkDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parksList = mutableListOf<HashMap<String, String>>()
        val adapter = SimpleAdapter(
            requireContext(),
            parksList,
            R.layout.list_item,
            arrayOf("fullName", "address", "currentDate"),
            intArrayOf(R.id.tvFullName, R.id.tvAddress, R.id.tvDate,)
        )
        binding.listview.adapter = adapter

        binding.listview.setOnItemClickListener { parent, view, position, id ->
            // Handle the click event here
            val item = parksList[position]
            val fullName = item["fullName"]
            val address = item["address"]
            val action = ItineraryFragmentDirections.actionItineraryFragmentToParkItinerary(fullName, address)
            findNavController().navigate(action)
        }

        db.collection("parks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    parksList.add(document.data as HashMap<String, String>)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("FBASE", "Error getting documents: ", exception)
            }
    }

    override fun onResume() {
        super.onResume()
        (binding.listview.adapter as? SimpleAdapter)?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
