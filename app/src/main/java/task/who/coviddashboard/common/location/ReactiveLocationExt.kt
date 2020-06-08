package task.who.coviddashboard.common.location

import android.app.Activity
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReactiveLocationExt {

    private val _locationLiveData = MutableLiveData<LocationState>()
    val locationLiveData: LiveData<LocationState>
        get() = _locationLiveData

    private var locationApi: LocationApi? = null
    fun locations(activity: Activity) {
        locationApi = LocationApi(
            activity,
            callbacks = object : LocationApi.Callbacks {
                override fun onSuccess(location: Location) {
                    _locationLiveData.postValue(LocationState.SUCCESS(location))
                }

                override fun onFailed(locationFailedEnum: LocationApi.LocationFailedEnum) {
                    _locationLiveData.postValue(LocationState.FAILED(Throwable(locationFailedEnum.name)))
                }
            })
    }

    fun requestLocation() = locationApi?.requestLocation()

    fun onActivityResult(requestCode: Int, resultCode: Int) {
        locationApi?.onActivityResult(requestCode, resultCode)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        locationApi?.onRequestPermissionsResult(requestCode, grantResults)
    }
}