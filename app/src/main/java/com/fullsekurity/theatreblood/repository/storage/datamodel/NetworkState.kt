package com.fullsekurity.theatreblood.repository.storage.datamodel

class NetworkState(val status: Status, val msg: String) {
    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {

        val LOADED: NetworkState
        val LOADING: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
        }
    }


}
