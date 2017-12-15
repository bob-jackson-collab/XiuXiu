package com.ys.xiuxiu

import com.ys.baselib.BaseFragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.widget.RadioGroup
import com.ys.xiuxiu.person.PersonFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    val mRadioGroup: RadioGroup? = null
    var mCurrentFragment: BaseFragment? = null
    var mFragments: ArrayList<BaseFragment>? = null
    val mFragmentManager: FragmentManager? = null
    var mCurrentIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
    }

    private fun initData(): Unit {
        mFragments = ArrayList()
        val indexFragment = IndexFragment()
        val personFragment = PersonFragment()
        mFragments!!.add(indexFragment)
        mFragments!!.add(personFragment)
        mCurrentFragment = indexFragment
    }

    private fun showFirstFragment(fragment: BaseFragment) {
        val beginTranscation = mFragmentManager!!.beginTransaction()

    }

    private fun showCurrentFragment(fragment: BaseFragment) {
        val beginTransaction = mFragmentManager!!.beginTransaction()
        if (fragment.isAdded) {
            beginTransaction.hide(mCurrentFragment).add(R.id.frame_layout, fragment, fragment.javaClass.simpleName)
        } else {
            beginTransaction.hide(mCurrentFragment).show(fragment)
        }
        beginTransaction.commit()
        mCurrentFragment = fragment
    }

    private fun restoreFragment(savedInstanceState: Bundle) {

        mCurrentIndex = savedInstanceState.getInt(STATE_FRAGMENT_NAME, 0)

        if (mFragments != null) {
            mFragments!!.removeAll(mFragments!!)
        } else {
            mFragments = ArrayList()
        }

        mFragments!!.add(
                if (mFragmentManager!!.findFragmentByTag(IndexFragment::class.java!!.getSimpleName()) != null)
                    mFragmentManager.findFragmentByTag(IndexFragment::class.java!!.getSimpleName()) as IndexFragment
                else
                    IndexFragment())
        mFragments!!.add(
                if (mFragmentManager.findFragmentByTag(PersonFragment::class.java!!.getSimpleName()) != null)
                    mFragmentManager.findFragmentByTag(PersonFragment::class.java!!.getSimpleName()) as PersonFragment
                else
                    PersonFragment())

        val beginTransaction = mFragmentManager.beginTransaction()
        for (i in mFragments!!.indices) {

            if (i == mCurrentIndex) {
                beginTransaction.show(mFragments!!.get(mCurrentIndex))
            } else {
                beginTransaction.hide(mFragments!!.get(i))
            }
        }

        beginTransaction.commit()
        mCurrentFragment = mFragments!!.get(mCurrentIndex)
    }

    override protected fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_FRAGMENT_NAME, mCurrentIndex)
        super.onSaveInstanceState(outState)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        private val STATE_FRAGMENT_NAME = "STATE_FRAGMENT_NAME"

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
