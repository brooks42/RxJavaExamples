package com.example.brita.messingwithrxjava.picturedemo.ui.picturedemo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brita.messingwithrxjava.R
import com.example.brita.messingwithrxjava.picturedemo.api.RestClient
import com.example.brita.messingwithrxjava.picturedemo.api.Words
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.logging.Level
import java.util.logging.Logger

private val TAG = PictureDemoFragment::class.qualifiedName

class PictureDemoFragment : Fragment() {

    private val allSubscriptions = CompositeDisposable()

    companion object {
        fun newInstance() = PictureDemoFragment()
    }

    private lateinit var viewModel: PictureDemoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.picture_demo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PictureDemoViewModel::class.java)

        allSubscriptions.add(observableForImageUrls()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.from(Looper.getMainLooper(), true))
            .subscribe{
                Logger.getLogger(TAG).log(Level.INFO, "Response: $it")
            })
    }

    private fun observableForImageUrls() : Observable<String> {
        return Observable.create {
            // hit up https://raw.githubusercontent.com/dwyl/english-words/master/words.txt

            val wordsInterface = RestClient.retrofit.create(Words::class.java)
            val call = wordsInterface.getWords()
            val response = call.execute()
            if (response.isSuccessful) {
                it.onNext(response.body()!!)
                it.onComplete()
            } else {
                // TODO: idaelly this'll call onError
                it.onNext(response.errorBody().toString())
                it.onComplete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        allSubscriptions.dispose()
    }
}
