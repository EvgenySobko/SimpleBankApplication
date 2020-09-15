package com.peterpartner.testapp.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.lang.StringBuilder

data class User(
    val balance: Double,

    @SerializedName("card_number")
    val cardNumber: String,

    @SerializedName("cardholder_name")
    val cardholderName: String,

    @SerializedName("transaction_history")
    val transactionHistory: List<TransactionHistory>,

    val type: String,
    val valid: String
) {
    fun getType() = when (type) {
        "mastercard" -> CardType.MASTERCARD
        "visa" -> CardType.VISA
        "unionpay" -> CardType.UNIONPAY
        else -> CardType.UNKNOWN
    }
}

enum class CardType {
    MASTERCARD,
    VISA,
    UNIONPAY,
    UNKNOWN
}