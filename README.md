## COVID 19 Situation Dashboard

This application provides you with current COVID-19 situation based on your location.

## Challenge description
- Detect user location.
- Get country ISO_2_CODE of this location.
- Call api for getting Total Cases and Total deaths.
- Follow the given design.

## Screenshot
<img width="400" height="822" src="https://github.com/marwanadly/COVID19SituationDashboard/blob/master/screenshots/1.png"></a>
<img width="400" height="822" src="https://github.com/marwanadly/COVID19SituationDashboard/blob/master/screenshots/2.png"></a>
<img width="400" height="822" src="https://github.com/marwanadly/COVID19SituationDashboard/blob/master/screenshots/3.png"></a>

## Built With.

 * [Kotlin](https://kotlinlang.org/)
 * [androidX libraries](https://developer.android.com/jetpack/androidx)
 * [Android LifeCycle](https://developer.android.com/topic/libraries/architecture)
 * Retrofit2
 * [Kodein](https://kodein.org/Kodein-DI/)
 * [RxJava2](https://github.com/ReactiveX/RxJava)
 * [RxAndroid](https://github.com/ReactiveX/RxAndroid)
 * [CardSlider](https://github.com/IslamKhSh/CardSlider)
 * MVVM: Architectural Pattern

## Implementation
- In this project I am using MVVM with repository pattern as the application architecture.
- The communication between the viewmodels and the views I used Livedata because of its awareness of the view lifecycle.
- Using Kodein for for dependency injection that will make testing easier and our make code.
- Using Retrofit library to handle the APIs stuff.

