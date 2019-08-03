package com.krsolutions.tardy.presentation

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.krsolutions.tardy.R
import com.krsolutions.tardy.activities.UsernameFragment
import com.krsolutions.tardy.presentation.main_screen.MainActivity2

class SplashActivity : AppCompatActivity() {
    internal var prefs: SharedPreferences? = null
    internal var view: View? = null
    lateinit var usernameFragment: Fragment
    internal var fragContainer: FrameLayout? = null

    internal val splashDuration: Long?
        get() = 500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        view = this.findViewById(R.id.imageView)
        prefs = getSharedPreferences("com.krsolutions.tardy", Context.MODE_PRIVATE)
        fragContainer = findViewById(R.id.frag_container)
    }

    internal fun scheduleSplash(base: Context, view: View?) {
        val splashDuration = splashDuration
        val handler = Handler()
        handler.postDelayed(routeToMain(base, view), splashDuration!!)
    }

    internal fun routeToMain(base: Context, view: View?): Runnable {
        return Runnable {
            if (prefs!!.getBoolean("firstRun", true)) {
                prefs!!.edit().putBoolean("firstRun", false).commit()
                val ft = supportFragmentManager.beginTransaction()
                usernameFragment = UsernameFragment()
                (usernameFragment as UsernameFragment).passFt(ft)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.add(R.id.frag_container, usernameFragment).commit()
            } else {
                val intent = Intent(base, MainActivity2::class.java)
                val options = ActivityOptions.makeClipRevealAnimation(view, view!!.x.toInt(), view.y.toInt(), view.width, 100)
                base.startActivity(intent, options.toBundle())
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scheduleSplash(this, view)

    }
}
