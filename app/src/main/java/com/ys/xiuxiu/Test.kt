package com.ys.xiuxiu

import android.content.Intent
import android.os.CountDownTimer

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by yangsong on 2017/12/21.
 */

class Test {

    private fun test() {

        Observable.just("1")
                .map { s -> Integer.parseInt(s) }.flatMap(object : Function<Int, ObservableSource<String>> {
            override fun apply(t: Int): ObservableSource<String> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                return Observable.just(t!!.toString() + "1")
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

        }
    }
}
