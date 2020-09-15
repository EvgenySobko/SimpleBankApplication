package com.peterpartner.testapp.ui.cards

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.peterpartner.testapp.R
import com.peterpartner.testapp.api.HoldersState
import com.peterpartner.testapp.entities.User
import com.peterpartner.testapp.ui.base.BaseFragment
import com.peterpartner.testapp.ui.base.IOnBackPressed
import com.peterpartner.testapp.ui.main.MainFragment
import com.peterpartner.testapp.utils.toast
import kotlinx.android.synthetic.main.cards_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*

class CardsFragment : BaseFragment(R.layout.cards_fragment, CardsViewModel()), IOnBackPressed {

    companion object {

        const val CARD_NUM = "card"

        @JvmStatic
        fun newInstance(cardNum: Int) = CardsFragment().apply {
            arguments = Bundle().apply {
                putInt(CARD_NUM, cardNum)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        with (requireActivity()) {
            back_button.visibility = View.GONE
            toolbar_text.text = getString(R.string.main)
        }
        return false
    }

    private val clickListener: (cardNumber: Int) -> Unit = {
        with(requireActivity()) {
            back_button.visibility = View.GONE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(it))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit()
        }
    }

    override fun onInitView() {
        with(requireActivity()) {
            toolbar_text.text = getString(R.string.my_cards)
            back_button.apply { visibility = View.VISIBLE }.setOnClickListener {
                back_button.visibility = View.GONE
                toolbar_text.text = getString(R.string.main)
                supportFragmentManager.popBackStack()
            }
        }
        (viewModel as CardsViewModel).apply {
            getHolders()
            val cardsAdapter = CardsAdapter(clickListener, arguments!!.getInt(CARD_NUM))
            cards_rv.adapter = cardsAdapter
            mainState.observe(this@CardsFragment, Observer {
                when (it) {
                    is HoldersState.InProgress -> inProgress(true)
                    is HoldersState.Error -> {
                        inProgress(false)
                        toast(it.error)
                    }
                    is HoldersState.Success -> {
                        inProgress(false)
                        cardsAdapter.addItems(it.users.users)
                    }
                }
            })
        }
    }

    private fun inProgress(isInProgress: Boolean) {
        if (isInProgress) {
            cards_rv.visibility = View.GONE
            cards_progress_bar.visibility = View.VISIBLE
        } else {
            cards_rv.visibility = View.VISIBLE
            cards_progress_bar.visibility = View.GONE
        }
    }
}