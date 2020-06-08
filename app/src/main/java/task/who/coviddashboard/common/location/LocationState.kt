package task.who.coviddashboard.common.location

import android.location.Location

sealed class LocationState {
    data class SUCCESS(var location: Location): LocationState()
    data class FAILED(var error: Throwable): LocationState()
}