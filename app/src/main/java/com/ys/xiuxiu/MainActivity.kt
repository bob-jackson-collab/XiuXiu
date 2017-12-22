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
import android.content.Intent
import android.view.KeyEvent
import android.widget.CompoundButton
import android.widget.Toast
import kotlin.reflect.KFunction0


class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    var mCurrentFragment: BaseFragment? = null
    var mFragments: ArrayList<BaseFragment>? = null
    val mFragmentManager: FragmentManager? = null
    var mCurrentIndex: Int = 0
    var mExitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.index_radio ->
                if(mFragments!![0]!=null)showCurrentFragment(mFragments!![0])
            R.id.circle_radio ->
                if(mFragments!![1]!=null) showCurrentFragment(mFragments!![1])
            R.id.search_radio ->
                if(mFragments!![2]!=null)showCurrentFragment(mFragments!![2])
            R.id.my_radio ->
                if(mFragments!![3]!=null)showCurrentFragment(mFragments!![3])
        }
    }

    private fun initData() {
        mFragments = ArrayList()
        val indexFragment = IndexFragment()
        val personFragment = PersonFragment()
        mFragments!!.add(indexFragment)
        mFragments!!.add(personFragment)
        mCurrentFragment = indexFragment

        showCurrentFragment(mCurrentFragment as IndexFragment)
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
        } else mFragments = ArrayList()

        mFragments!!.add(
                if (mFragmentManager!!.findFragmentByTag(IndexFragment::class.java.simpleName) != null)
                    mFragmentManager.findFragmentByTag(IndexFragment::class.java.simpleName) as IndexFragment
                else
                    IndexFragment())
        mFragments!!.add(
                if (mFragmentManager.findFragmentByTag(PersonFragment::class.java.simpleName) != null)
                    mFragmentManager.findFragmentByTag(PersonFragment::class.java.simpleName) as PersonFragment
                else
                    PersonFragment())

        val beginTransaction = mFragmentManager.beginTransaction()
        for (i in mFragments!!.indices) {
            if (i == mCurrentIndex) {
                beginTransaction.show(mFragments!![mCurrentIndex])
            } else {
                beginTransaction.hide(mFragments!![i])
            }
        }

        beginTransaction.commit()
        mCurrentFragment = mFragments!!.get(mCurrentIndex)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_FRAGMENT_NAME, mCurrentIndex)
        super.onSaveInstanceState(outState)
        restoreFragment(outState)
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

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val currentTime: Long = System.currentTimeMillis()
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.repeatCount == 0) {
            if (currentTime - mExitTime < 2000) {
                System.exit(0)
            } else {
                Toast.makeText(this@MainActivity, "在按一次退出程序", Toast.LENGTH_SHORT).show()
                mExitTime = currentTime
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
