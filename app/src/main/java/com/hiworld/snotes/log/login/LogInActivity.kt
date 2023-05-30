package com.hiworld.snotes.log.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hiworld.snotes.home.HomeActivity
import com.hiworld.snotes.R
import com.hiworld.snotes.log.register.RegisterActivity
import com.hiworld.snotes.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    lateinit var iUsername :String
    lateinit var iPassword :String

    lateinit var mDatabase : DatabaseReference
    lateinit var preferences : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@LogInActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        button_login.setOnClickListener {
            iUsername = edt_username.text.toString()
            iPassword = edt_password.text.toString()

            if (iUsername == "") {
                edt_username.error = "Silahkan tulis Username Anda"
                edt_username.requestFocus()
            } else if (iPassword == "") {
                edt_password.error = "Silahkan tulis Password Anda"
                edt_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        button_register.setOnClickListener {
            val intent = Intent(this@LogInActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@LogInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    if (user.password.equals(iPassword)){
                        Toast.makeText(this@LogInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@LogInActivity,
                            HomeActivity::class.java).putExtra("nama", user.nama)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@LogInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LogInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
