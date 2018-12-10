package com.example.brita.messingwithrxjava.picturedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.brita.messingwithrxjava.R
import com.example.brita.messingwithrxjava.picturedemo.ui.picturedemo.PictureDemoFragment

class PictureDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.picture_demo_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureDemoFragment.newInstance())
                .commitNow()
        }
    }

}
