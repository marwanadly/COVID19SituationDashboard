package task.who.coviddashboard.data.model

import com.squareup.moshi.Json

data class Attributes (
    @field:Json(name = "attributes")
    var attribute: Attribute
)