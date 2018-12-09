package com.example.brita.messingwithrxjava;

import android.annotation.SuppressLint;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressLint("CheckResult")
public class RxJavaSimpleExamples {

    public static String TAG = "RxJavaSimpleExamples";

    // the most basic RxJava: log 1, 2, 3, synchronously
    public Observable<Integer> someItems() {
        // superfluous use of an Observable, if this was blocking this wold actually be really bad but since
        // it's a non-blocking sync I/O it's fine
        return Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onComplete();
        });
    }

    // a little less basic, makes a "mouse listener" and handles dispatching of user events to a subscriber
    // on the main thread
    public void startDispatchingInputEvents() {
        Observable.create(s -> {

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(event -> {

        });
    }

    // populates a text view to make it look like a user typing some text very slowly
    public Observable<String> startPopulatingATextView() {
        return Observable.create(s -> {
            String output = "Stop observing me thanks";
            Random random = new Random();
            output.chars().forEach(in -> {
                try {
                    s.onNext("" + (char)in);
                    Thread.sleep(random.nextInt(25) * 100);
                } catch (InterruptedException e) {
                    s.onError(e);
                }
            });
            s.onComplete();
        });
    }

    // uses a Future to get a value in the future
    public Observable<String> startDoingAFuture() {
        return Observable.create(s -> {
            Thread thread = new Thread(() -> {
                try {
                    s.onNext(startAFuture().get());
                    s.onComplete();
                } catch (ExecutionException e) {
                    s.onError(e);
                } catch (InterruptedException e) {
                    s.onError(e);
                }
            });
            thread.start();
        });
    }

    private Future<String> startAFuture() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        return executorService.submit(() -> {
            try {
                Thread.sleep(5000);
                return "I hit 88 mph";
            } catch (InterruptedException e) {
                // RIP
                return null;
            }
        });
    }
}
