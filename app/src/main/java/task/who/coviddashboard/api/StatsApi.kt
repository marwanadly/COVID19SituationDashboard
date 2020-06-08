package task.who.coviddashboard.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import task.who.coviddashboard.data.model.StatsResponse

interface StatsApi {
    @GET("COVID19_hist_cases_adm0_v5_view/FeatureServer/0/query?objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&outFields=CumCase%2CCumDeath&returnHiddenFields=false&returnGeometry=false&featureEncoding=esriDefault&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false&returnDistinctValues=false&cacheHint=false&orderByFields=date_epicrv+DESC&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=1&returnZ=false&returnM=false&returnExceededLimitFeatures=false&quantizationParameters=&sqlFormat=none&f=pjson&token=")
    fun getStatsByCountry(@Query("where") countryCode: String): Single<StatsResponse>
}