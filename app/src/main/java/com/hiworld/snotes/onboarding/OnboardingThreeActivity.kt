package com.hiworld.snotes.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hiworld.snotes.log.login.LogInActivity
import com.hiworld.snotes.R
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        button_start.setOnClickListener {
            finishAffinity()

            var intent = Intent(this@OnboardingThreeActivity, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}