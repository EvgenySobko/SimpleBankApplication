package com.peterpartner.testapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.peterpartner.testapp.R
import com.peterpartner.testapp.api.HoldersState
import com.peterpartner.testapp.ui.base.BaseFragment
import com.peterpartner.testapp.entities.CardType
import com.peterpartner.testapp.entities.User
import com.peterpartner.testapp.entities.Valute
import com.peterpartner.testapp.ui.base.IOnBackPressed
import com.peterpartner.testapp.ui.cards.CardsFragment
import com.peterpartner.testapp.utils.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment(R.layout.main_fragment, MainViewModel()), IOnBackPressed {

    companion object {

        const val GBP_CHAR_CODE = "GBP"
        const val EUR_CHAR_CODE = "EUR"
        const val CARD_NUM = "card"

        @JvmStatic
        fun newInstance(cardNumber: Int) = MainFragment().apply {
            arguments = Bundle().apply {
                putInt(CARD_NUM, cardNumber)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        return false
    }

    private val transactionsAdapter = HistoryAdapter()

    override fun onInitView() {
        onInitClicks()
        requireActivity().toolbar_text.text = getString(R.string.main)
        (viewModel as MainViewModel).apply {
            currentCard = arguments?.getInt(CARD_NUM) ?: 0
            getHolders()
            getCurrency()
            mainState.observe(this@MainFragment.viewLifecycleOwner, Observer {
                when (it) {
                    is HoldersState.InProgress -> inProgress(true)
                    is HoldersState.Error -> {
                        inProgress(false)
                        toast(it.error)
                    }
                    is HoldersState.Success -> {
                        inProgress(false)
                        fillView(it.users.users[currentCard!!])
                    }
                }
            })
            currentValute.observe(this@MainFragment.viewLifecycleOwner, Observer { fillValuesByValute(it) })
        }
    }

    private fun onInitClicks() {
        with(viewModel as MainViewModel) {
            gbp.setOnClickListener { selectValute(MainViewModel.ValuteState.GBP) }
            eur.setOnClickListener { selectValute(MainViewModel.ValuteState.EUR) }
            rub.setOnClickListener {
                converted_money.text = "₽${currentUserBalanceRub.beautify()}"
                transactionsAdapter.updateValute(1.0, currencyUSD, "₽")
            }
            card.setOnClickListener {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.container, CardsFragment.newInstance(currentCard!!))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun fillView(user: User) {
        with(user) {
            card_number.text = cardNumber
            ic_card_type.setImageDrawable(
                (resources.getDrawable(
                    when (this.getType()) {
                        CardType.MASTERCARD -> R.drawable.ic_mastercard
                        CardType.VISA -> R.drawable.ic_visa
                        else -> R.drawable.ic_unionpay
                    }
                ))
            )
            transactionsAdapter.addItems(transactionHistory)
            history_rv.adapter = transactionsAdapter
            user_name.text = cardholderName
            valid_thru.text = valid
            balance_value.text = "$${balance}"
        }
        (viewModel as MainViewModel).selectValute(MainViewModel.ValuteState.GBP)
    }

    private fun fillValuesByValute(valute: Valute?) {
        if (valute != null) {
            with(viewModel as MainViewModel) {
                when (valute.CharCode) {
                    GBP_CHAR_CODE -> {
                        converted_money.text = "£${(currentUserBalanceRub / valute.Value).beautify()}"
                        transactionsAdapter.updateValute(valute.Value, currencyUSD, "£")
                    }
                    EUR_CHAR_CODE -> {
                        converted_money.text = "€${(currentUserBalanceRub / valute.Value).beautify()}"
                        transactionsAdapter.updateValute(valute.Value, currencyUSD, "€")
                    }
                }
            }
        }
    }

    private fun inProgress(isInProgress: Boolean) {
        if (isInProgress) {
            main.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
        } else {
            main.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }
    }
}