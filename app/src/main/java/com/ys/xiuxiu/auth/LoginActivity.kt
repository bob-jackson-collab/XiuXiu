package com.ys.xiuxiu.auth

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi

import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.ys.baselib.BaseActivity
import com.ys.xiuxiu.R

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {
    private var mLoginModel: LoginViewModel? = null

    override fun initView() {
        sign_in_button.setOnClickListener { judgeData() }
    }


    override fun initData() {

    }

    fun judgeData() {
        if (TextUtils.isEmpty(username.text.trim())) {
            return
        }

        if (TextUtils.isEmpty(password.text.trim())) {
            return
        }

        toLogin(username.text.trim().toString(), password.text.trim().toString())
    }

    private fun toLogin(username: String, password: String) {
        val loginParam = LoginParam()
        loginParam.username = username
        loginParam.password = password
        mLoginModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        mLoginModel!!.getUser(loginParam)!!.observe(this@LoginActivity, Observer { user ->
            {
                //对拿到的user信息进行存储
            }

        })
    }

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }


}
