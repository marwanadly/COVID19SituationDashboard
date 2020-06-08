package task.who.coviddashboard.data.model

import com.squareup.moshi.Json
import java.text.DecimalFormat

data class Attribute (
    @field:Json(name = "CumCase")
    var totalCases: Long,

    @field:Json(name = "CumDeath")
    var totalDeath: Long,

    var countryCode: String
) {
    fun getFormattedTotalCases(): String = DecimalFormat("##,###").format(totalCases)

    fun getFormattedTotalDeaths(): String  = "${DecimalFormat("##,###").format(totalDeath)} Total Deaths"

    fun getHeaderText(): String = "$countryCode's Situation in Numbers"
}