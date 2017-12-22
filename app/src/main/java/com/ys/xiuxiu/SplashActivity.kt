package com.ys.xiuxiu

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ys.baselib.BaseActivity
import com.ys.xiuxiu.R
import com.ys.xiuxiu.auth.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by yangsong on 2017/12/20.
 */
class SplashActivity : BaseActivity() {

    override fun initView() {
        img_splash.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher))
        tv_skip.setOnClickListener { v ->
            val intent = Intent()
            intent.setClass(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initData() {
        mTimer = MyTimer()
        mTimer!!.start()
    }


    var mTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        initData()
    }


    inner class MyTimer : CountDownTimer(3000, 1000) {
        override fun onFinish() {
            val intent: Intent = Intent(this@SplashActivity, LoginActivity::class.java)
            this@SplashActivity.startActivity(intent)
        }

        override fun onTick(millisUntilFinished: Long) {
           tv_skip.setText("跳过"+(millisUntilFinished/1000)+"s")
        }
    }

}