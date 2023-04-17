package com.example.project_g09

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g09.databinding.FragmentMapBinding
import com.example.project_g09.models.AllStatesResponse
import com.example.project_g09.models.State
import com.example.project_g09.networking.ApiService
import com.example.project_g09.networking.RetrofitInstance
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class MapFragment : Fragment(R.layout.fragment_map),OnMapReadyCallback {

    //Binding Variables
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val lisOfStates:MutableList<String> = mutableListOf()
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var myMap: GoogleMap
    //list of locations
    //private val parkLocations:MutableList<LatLng> = mutableListOf()

    val statesFromDataStorage = DataStorage.getInstance().stateList
    var markerList:MutableList<LatLng> = mutableListOf()
    private lateinit var dataFromAPI: AllStatesResponse

    //Binding logic to use with fragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =  childFragmentManager.findFragmentById(binding.mapContainer.id) as? SupportMapFragment

        if (mapFragment == null) {
            Log.d(TAG, "++++ map fragment is null")
        }
        else {
            Log.d(TAG, "++++ map fragment is NOT null")
            // assuming the screen can find the map fragment in the xml file, then
            // connect with Google and get whatever information you need from Google
            // to setup the map
            mapFragment?.getMapAsync(this)
        }

        for(states in statesFromDataStorage){
            lisOfStates.add(states.stateFullName)
        }

        //Array adapter for spinner
        this.spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,lisOfStates)
        binding.listOfStatesSpinner.adapter = spinnerAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getAllStatesFromAPI(statesToSearch:String): AllStatesResponse? {
        var apiService: ApiService = RetrofitInstance.retrofitService
        val response: retrofit2.Response<AllStatesResponse> = apiService.getStates(statesToSearch.trim(),"FnvA2Hh1uDqFiQqvmAJRPInDrW6a1E94IpBaiOtT")

        if (response.isSuccessful) {
            val dataFromAPI = response.body()   /// myresponseobject
            if (dataFromAPI == null) {
                Log.d("API", "No data from API or some other error")
                return null
            }

            // if you reach this point, then you must have received a response from the api
            Log.d(TAG, "Here is the data from the API")
            Log.d(TAG, dataFromAPI.toString())
            return dataFromAPI
        }
        else {
            // Handle error
            Log.d(TAG, "An error occurred")
            return null
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "+++ Map callback is executing...")
        // initialize the map
        this.myMap = googleMap

        // configure the map's options
        // - set the map type (hybrid, satellite, etc)
        myMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        // - select if traffic data should be displayed
        myMap.isTrafficEnabled = true
        // - add user interface controls to the map (zoom, compass, etc)
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true

        val intialLocation = LatLng(43.6426, -79.3871)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 2.0f))
        myMap.addMarker(MarkerOptions().position(intialLocation).title("Welcome to Toronto"))

        //Spinner listener when one category is selected.
        binding.findParks.setOnClickListener{
                 myMap.clear()
                //Gets the value from the spinner as a String
                //var selectedItem = parent?.getItemAtPosition(position) as String

                val position = binding.listOfStatesSpinner.selectedItemPosition

                var stateToSearch = statesFromDataStorage[position].stateShort

            fun moveToNextFragment(stateInfoToSend:State) {
                val action = MapFragmentDirections.actionMapFragmentToViewParkDetails(stateInfoToSend)
                findNavController().navigate(action)
            }

                //Launching API query

                lifecycleScope.launch {
                    var responseFromAPI:AllStatesResponse? = getAllStatesFromAPI(stateToSearch)
                    if (responseFromAPI == null) {
                        return@launch
                    }
                    Log.d(TAG, "Success: Data retrieved from API")
                    Log.d(TAG, responseFromAPI.toString())


                    var stateParkDataList:List<State> = responseFromAPI.data

                    var parkLocation:LatLng
                    val builder = LatLngBounds.Builder()
                    var stateInfo:State

                    for(states in stateParkDataList){

                        val lat = states.latitude.toDouble()
                        val long = states.longitude.toDouble()

                        parkLocation = LatLng(lat,long)

                        markerList.add(parkLocation)

                        builder.include(parkLocation)

                        var addressForMap = "${states.addresses[0].line1}, ${states.addresses[0].city} , ${states.addresses[0].stateCode}, ${states.addresses[0].postalCode}"

                        stateInfo = State(states.url,states.fullName,states.description,states.latitude,states.longitude,states.addresses,states.images)

                        myMap.addMarker(
                            MarkerOptions()
                                .position(parkLocation)
                                .title("${states.fullName}")
                                .snippet("${addressForMap}\n Click Here For Details")

                        )

//                        myMap.setOnInfoWindowClickListener(markerListener)
                        myMap.setOnInfoWindowClickListener {
                            moveToNextFragment(stateInfo)
                        }

                    }


                    val bounds = builder.build()
                    val padding = 200
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    googleMap.moveCamera(cameraUpdate)

                }

            }

        }

}



