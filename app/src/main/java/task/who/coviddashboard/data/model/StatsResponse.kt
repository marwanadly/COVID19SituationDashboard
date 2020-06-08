package task.who.coviddashboard.data.model

import com.squareup.moshi.Json

data class StatsResponse (
    @field:Json(name = "features")
    var features: List<Attributes>
)