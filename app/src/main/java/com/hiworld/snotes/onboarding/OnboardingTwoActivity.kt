package com.hiworld.snotes.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hiworld.snotes.log.login.LogInActivity
import com.hiworld.snotes.R
import kotlinx.android.synthetic.main.activity_onboarding_two.*

class OnboardingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        button_next.setOnClickListener {
            var intent = Intent(this@OnboardingTwoActivity, OnboardingThreeActivity::class.java)
            startActivity(intent)
        }

        button_skip.setOnClickListener {
            finishAffinity()
            var intent = Intent(this@OnboardingTwoActivity, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}