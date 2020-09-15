package com.peterpartner.testapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.peterpartner.testapp.R
import com.peterpartner.testapp.entities.TransactionHistory
import com.peterpartner.testapp.entities.Valute
import com.peterpartner.testapp.utils.beautify
import com.peterpartner.testapp.utils.beautifyRub
import com.peterpartner.testapp.utils.logd
import kotlinx.android.synthetic.main.item_history.view.*
import kotlin.math.log

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val itemList = mutableListOf<TransactionHistory>()
    private var currentValue = 0.0
    private var currentUSD = 0.0
    private var currencyChar = ""

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun addItems(list: List<TransactionHistory>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateValute(value: Double, USD: Double, char: String) {
        currentValue = value
        currentUSD = USD
        currencyChar = char
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: TransactionHistory) {
            with(itemView) {
                transaction_image.load(item.icon_url)
                transaction_name.text = item.title
                transaction_date.text = item.date
                transaction_price_us.text = "$ ${item.amount.replace("-", "")}"
                if (currentValue != 0.0 && currencyChar != "₽") {
                    transaction_price.text = (item.amount.toDouble() * currentUSD / currentValue).beautify()
                        .replace("0 0-", "- $currencyChar ")
                } else if (currencyChar == "₽") {
                    transaction_price.text = (item.amount.toDouble() * currentUSD).beautifyRub(currencyChar)
                }
            }
        }
    }
}