package com.shakif.weatheralertapp.network

import com.shakif.weatheralertapp.utility.StringConstants.NO_CONNECTION
import com.shakif.weatheralertapp.utility.StringConstants.SOMETHING_WENT_WRONG

class APIError {
    var code: Int = 0
    var message: String = ""
    var error: String = ""

    fun message(): String {
        if (message.isNotEmpty()) {
            return message
        }
        if (error.isNotEmpty()) {
            return error
        }
        return ""
    }

    companion object {
        fun generic(): APIError {
            val apiError = APIError()
            apiError.message = SOMETHING_WENT_WRONG
            return apiError
        }

        fun noConnection(): APIError {
            val apiError = APIError()
            apiError.message = NO_CONNECTION
            return apiError
        }
    }
}