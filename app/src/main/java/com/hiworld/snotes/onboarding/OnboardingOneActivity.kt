package com.hiworld.snotes.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hiworld.snotes.log.login.LogInActivity
import com.hiworld.snotes.R
import com.hiworld.snotes.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preference:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preference = Preferences(this)

        if (preference.getValues("onboarding").equals("1")) {
            finishAffinity()
            var intent = Intent(this@OnboardingOneActivity, LogInActivity::class.java)
            startActivity(intent)
        }

        button_next.setOnClickListener{
            var intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        button_skip.setOnClickListener{
            preference.setValues("onboarding", "1")
            finishAffinity()
            var intent = Intent(this@OnboardingOneActivity, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}