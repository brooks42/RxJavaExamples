package com.example.brita.messingwithrxjava

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.brita.messingwithrxjava.picturedemo.PictureDemoActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.logging.Level
import java.util.logging.Logger

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // subscriptions so onDestroy() we can clean them out, if we don't we'll leak
    private val allSubscriptions = CompositeDisposable()

    // gives us some of our basic Observables
    private val example = RxJavaSimpleExamples()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        picDemoBtn.setOnClickListener {
            var intent = Intent(this, PictureDemoActivity::class.java)
            startActivity(intent)
        }

        // just emits 1, 2, 3 to the log
        allSubscriptions.add(example.someItems().subscribe {
            Logger.getLogger(TAG).log(Level.INFO, "Emitted: $it")
        })

        // in the future we'll log a message
        allSubscriptions.add(example.startDoingAFuture().subscribe { event ->
                Logger.getLogger(TAG).log(Level.INFO, "A message from the Future: $event")
            })

        // this one makes it look like the user is typing a message into the text field so that's fun
        val textView: TextView = this.findViewById(R.id.helloWorldTextView)
        allSubscriptions.add(example.startPopulatingATextView()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({it:String ->
                textView.append(it)
            }, {error:Throwable ->
                Logger.getLogger(TAG).log(Level.SEVERE, "Error $error")
            })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        allSubscriptions.dispose()
    }
}
