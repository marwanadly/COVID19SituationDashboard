package task.who.coviddashboard.ui

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.islamkhsh.CardSliderViewPager
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import task.who.coviddashboard.R
import task.who.coviddashboard.common.extension.viewModel
import task.who.coviddashboard.common.location.LocationState
import task.who.coviddashboard.common.location.ReactiveLocationExt
import task.who.coviddashboard.data.model.Attribute
import task.who.coviddashboard.ui.adapter.CardsAdapter
import task.who.coviddashboard.ui.viewmodel.MainViewModel
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val rxLocation = ReactiveLocationExt()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRxLocation()
        mainViewModel.mainViewState.observe(this, Observer { mainViewState ->
            manageDataState(mainViewState.dataState)
            manageErrorState(mainViewState.error)
        })
    }

    private fun manageDataState(attributes: Attribute?) {
        attributes?.let {
            viewPager.adapter = CardsAdapter(arrayListOf(it))
            viewPager.autoSlideTime = CardSliderViewPager.STOP_AUTO_SLIDING
        }
    }

    private fun manageErrorState(error: Throwable?) {
        error?.let {
            internetErrorDialog()
        }
    }

    private fun internetErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Internet Error")
            .setMessage("There is something went wrong.\nPlease check your internet connection and then try again")
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .show()
    }

    private fun initRxLocation() {
        rxLocation.locations(this)
        rxLocation.locationLiveData.observe(this, Observer {
            when (it) {
                is LocationState.SUCCESS -> {
                    request_location_layout.visibility = GONE
                    try {
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        val addresses =
                            geoCoder.getFromLocation(it.location.latitude, it.location.longitude, 1)
                        mainViewModel.getStatsByCountry(addresses[0].countryCode)
                    }catch (error: IOException) {
                        internetErrorDialog()
                    }
                }
                is LocationState.FAILED -> {
                    request_location_layout.visibility = VISIBLE
                    turn_on_location.setOnClickListener { rxLocation.requestLocation() }
                    no_thanks.setOnClickListener { finish() }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        rxLocation.onActivityResult(requestCode, resultCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        rxLocation.onRequestPermissionsResult(requestCode, grantResults)
    }
}
