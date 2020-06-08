package task.who.coviddashboard.common.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import timber.log.Timber
import java.lang.ref.WeakReference

class LocationApi(
    activity: Activity,
    private val callbacks: Callbacks
) {
    private var activityWeakReference = WeakReference(activity)
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val requestCheckSettings = 1235
    private val requestLocation = 1236

    interface Callbacks {
        fun onSuccess(location: Location)
        fun onFailed(locationFailedEnum: LocationFailedEnum)
    }

    enum class LocationFailedEnum {
        LocationPermissionNotGranted,
        LocationNotEnabled,
    }

    init {
        fusedLocationClient = activity.let { LocationServices.getFusedLocationProviderClient(it) }
        val task = fusedLocationClient?.lastLocation

        task?.addOnSuccessListener {
            requestLocation()
        }
        task?.addOnFailureListener {
            requestLocation()
        }
    }

    fun requestLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                Timber.e(locationResult.lastLocation.toString())
                callbacks.onSuccess(locationResult.lastLocation)
                fusedLocationClient?.removeLocationUpdates(locationCallback)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                super.onLocationAvailability(locationAvailability)
                if (locationAvailability?.isLocationAvailable == false) {
                    callbacks.onFailed(
                        LocationFailedEnum.LocationNotEnabled
                    )
                    fusedLocationClient?.removeLocationUpdates(locationCallback)
                }
            }
        }

        if (activityWeakReference.get() == null) {
            return
        } else {
            val permissions = ArrayList<String>()
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            var permissionGranted = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activityWeakReference.get() as Activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionGranted = false
                    break
                }
            }
            if (!permissionGranted) {
                val permissionsArgs = permissions.toTypedArray()
                ActivityCompat.requestPermissions(
                    activityWeakReference.get() as Activity,
                    permissionsArgs,
                    requestLocation
                )
            } else {
                getLocation()
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {

        if (activityWeakReference.get() == null) {
            return
        }

        if (requestCode == requestLocation) {
            if (grantResults.isEmpty()) {
                callbacks.onFailed(LocationFailedEnum.LocationPermissionNotGranted)
                return
            }

            var granted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            if (granted) {
                getLocation()
            } else {
                callbacks.onFailed(LocationFailedEnum.LocationPermissionNotGranted)
            }
        }
    }

    private fun getLocation() {
        if (activityWeakReference.get() == null) return
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient((activityWeakReference.get() as Activity))
                .checkLocationSettings(builder.build())

        result.addOnSuccessListener {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        result.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                if (activityWeakReference.get() == null) {
                    return@addOnFailureListener
                }
                try {
                    exception.startResolutionForResult(
                        activityWeakReference.get() as Activity,
                        requestCheckSettings
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (activityWeakReference.get() == null) {
            return
        }
        if (requestCode == requestCheckSettings) {
            if (resultCode == Activity.RESULT_OK) {
                getLocation()
            } else {
                val locationManager =
                    (activityWeakReference.get() as Activity)
                        .getSystemService(
                            Context.LOCATION_SERVICE
                        ) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    callbacks.onFailed(
                        LocationFailedEnum.LocationNotEnabled
                    )
                }
            }
        }
    }
}