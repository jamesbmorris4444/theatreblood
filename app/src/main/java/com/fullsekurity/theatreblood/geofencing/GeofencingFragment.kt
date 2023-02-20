package com.fullsekurity.theatreblood.geofencing

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.databinding.GeofencingFragmentBinding
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class GeofencingFragment : Fragment(), Callbacks {

    private val GEOFENCE_EXPIRATION_IN_MILLISECONDS = 1000 * 60 * 60 * 4L // four hours

    private lateinit var binding: GeofencingFragmentBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var geofencingViewModel: GeofencingViewModel
    private lateinit var geofencingClient: GeofencingClient
    private var geofenceList: MutableList<Geofence> = mutableListOf()

    companion object {
        fun newInstance(): GeofencingFragment {
            val fragment = GeofencingFragment()
            return fragment
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = Constants.GEOFENCING_TITLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.geofencing_fragment, container, false) as GeofencingFragmentBinding
        binding.lifecycleOwner = this
        geofencingViewModel = ViewModelProvider(this, GeofencingViewModelFactory(this)).get(GeofencingViewModel::class.java)
        binding.geofencingViewModel = geofencingViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        setupGeofencing()
        return binding.root
    }

    private fun setupGeofencing() {
        geofencingClient = LocationServices.getGeofencingClient(requireContext())
        geofencingViewModel.geofencingLiveData.observe(viewLifecycleOwner) { geoFencingList ->
            LogUtils.D(
                "JIMX",
                LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
                String.format(
                    "location  size=%d,  %f   %f   %f",
                    geoFencingList.size,
                    geoFencingList[0].latitude,
                    geoFencingList[0].longitude,
                    geoFencingList[0].radius
                )
            )
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId("KEY")
                    .setNotificationResponsiveness(1000)
                    .setCircularRegion(
                        geoFencingList[0].latitude,
                        geoFencingList[0].longitude,
                        geoFencingList[0].radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(10000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
                    .build()
            )
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    StandardModal(
                        this,
                        modalType = StandardModal.ModalType.STANDARD,
                        titleText = fetchActivity().getString(R.string.access_fine_permission_rationale_title),
                        bodyText = fetchActivity().getString(R.string.access_fine_permission_rationale_body),
                        positiveText = fetchActivity().getString(R.string.std_modal_yes),
                        negativeText = fetchActivity().getString(R.string.std_modal_no),
                        dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                            override fun onPositive(string: String) {
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    1
                                )
                            }

                            override fun onNegative() {}
                            override fun onNeutral() {}
                            override fun onBackPressed() {
                            }
                        }
                    ).show(fetchActivity().supportFragmentManager, "MODAL")
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Access Fine Permission Denied, show educational UI",
                        Toast.LENGTH_SHORT
                    ).show()
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
            } else {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
                    addOnSuccessListener {
                        // Geofences added
                        LogUtils.D(
                            "JIMX",
                            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
                            String.format("ADD SUCCEEDED")
                        )
                        Toast.makeText(requireActivity(), "ADD SUCCEEDED", Toast.LENGTH_SHORT)
                            .show()
                    }
                    addOnFailureListener {
                        // Failed to add geofences
                        LogUtils.D(
                            "JIMX",
                            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
                            String.format("ADD FAILED")
                        )
                        Toast.makeText(requireActivity(), "ADD FAILED", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Access Fine Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(),"Access Fine Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun fetchActivity(): MainActivity {
        return if (::mainActivity.isInitialized) {
            mainActivity
        } else {
            activity as MainActivity
        }
    }

    override fun fetchRootView(): View {
        return binding.root
    }

    override fun fetchFragment(): Fragment {
        return this
    }

    override fun fetchRadioButton(resId:Int): RadioButton {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchDropdown(resId: Int) : Spinner? {
        return fetchRootView().findViewById(resId)
    }

    override fun fetchCreateProductsListViewModel() : CreateProductsListViewModel? { return null }
    override fun fetchDonateProductsListViewModel() : DonateProductsListViewModel? { return null }
    override fun fetchReassociateProductsListViewModel() : ReassociateProductsListViewModel? { return null }
    override fun fetchViewDonorListViewModel() : ViewDonorListListViewModel? { return null }

}

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context,"onReceive entered", Toast.LENGTH_SHORT).show()
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("onReceive called"))
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("error: %s", errorMessage))
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val mp = MediaPlayer.create(context, R.raw.telegraphc_receiver)
            mp.start()
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            Toast.makeText(context,"ENTER", Toast.LENGTH_LONG).show()
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("transition: %s   geofence: %s", geofenceTransition, triggeringGeofences))
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            val mp = MediaPlayer.create(context, R.raw.dial_tone)
            mp.start()
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            Toast.makeText(context,"EXIT", Toast.LENGTH_LONG).show()
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("transition: %s   geofence: %s", geofenceTransition, triggeringGeofences))
        } else {
            val mp = MediaPlayer.create(context, R.raw.siren)
            mp.start()
            Toast.makeText(context,"DWELL", Toast.LENGTH_LONG).show()
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("DWELL"))
        }
    }

}