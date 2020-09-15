package com.peterpartner.testapp.ui.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peterpartner.testapp.R
import com.peterpartner.testapp.entities.CardType
import com.peterpartner.testapp.entities.User
import kotlinx.android.synthetic.main.item_card.view.*

class CardsAdapter(private val clickListener: (cardNumber: Int) -> Unit,
                   private val previousPosition: Int): RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    private val itemList = mutableListOf<User>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false))
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.bind(itemList[position], position)
        if (position == previousPosition) holder.itemView.selector.visibility = View.VISIBLE
    }

    fun addItems(list: List<User>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class CardsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: User, position: Int) {
            with(itemView.ic_card_type) {
                when (item.getType()) {
                    CardType.MASTERCARD -> setImageDrawable(resources.getDrawable(R.drawable.ic_mastercard))
                    CardType.VISA -> setImageDrawable(resources.getDrawable(R.drawable.ic_visa))
                    else -> setImageDrawable(resources.getDrawable(R.drawable.ic_unionpay))
                }
            }
            with(itemView) {
                setOnClickListener {
                    clickListener.invoke(position)
                    selector.visibility = View.VISIBLE
                }
                card_number.text = item.cardNumber
            }
        }
    }
}