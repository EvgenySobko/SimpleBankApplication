package com.peterpartner.testapp.api

import com.peterpartner.testapp.entities.UserList

sealed class HoldersState {
    object InProgress : HoldersState()
    data class Error(val error: Throwable) : HoldersState()
    data class Success(val users: UserList) : HoldersState()
}