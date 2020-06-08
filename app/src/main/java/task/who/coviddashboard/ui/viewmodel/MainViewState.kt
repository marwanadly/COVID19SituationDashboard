package task.who.coviddashboard.ui.viewmodel

import task.who.coviddashboard.data.model.Attribute

data class MainViewState(
    var dataState: Attribute? = null,
    var error: Throwable? = null
)