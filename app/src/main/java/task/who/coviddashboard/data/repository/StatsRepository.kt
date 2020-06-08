package task.who.coviddashboard.data.repository

import task.who.coviddashboard.api.StatsApi

class StatsRepository(private val statsApi: StatsApi) {

    fun getStatsByCountry(countryCode: String) =
        statsApi.getStatsByCountry("ISO_2_CODE='$countryCode'")
}