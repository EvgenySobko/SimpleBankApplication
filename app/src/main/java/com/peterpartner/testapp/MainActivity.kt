package com.peterpartner.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peterpartner.testapp.ui.base.IOnBackPressed
import com.peterpartner.testapp.ui.main.MainFragment
import kotlinx.android.synthetic.main.item_history.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}