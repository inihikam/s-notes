package com.hiworld.snotes.log.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hiworld.snotes.R
import com.hiworld.snotes.log.login.User
import com.hiworld.snotes.utils.Preferences
import com.google.firebase.database.*
import com.hiworld.snotes.home.HomeActivity
import com.hiworld.snotes.log.register.RegisterPhotoscreenActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var sUsername:String
    lateinit var sPassword:String
    lateinit var sNama:String
    lateinit var sEmail:String

    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        button_reg.setOnClickListener {
            sUsername = reg_username.text.toString()
            sPassword = reg_password.text.toString()
            sNama = reg_nama.text.toString()
            sEmail = reg_email.text.toString()

            if (sUsername.equals("")) {
                reg_username.error = "Silahkan isi Username"
                reg_username.requestFocus()
            } else if (sPassword.equals("")) {
                reg_password.error = "Silahkan isi Password"
                reg_password.requestFocus()
            } else if (sNama.equals("")) {
                reg_nama.error = "Silahkan isi Nama"
                reg_nama.requestFocus()
            } else if (sEmail.equals("")) {
                reg_email.error = "Silahkan isi Email"
                reg_email.requestFocus()
            } else {
                saveUser(sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUser(sUsername: String, sPassword: String, sNama: String, sEmail: String) {

        val user = User()
        user.email = sEmail
        user.username = sUsername
        user.nama = sNama
        user.password = sPassword

        if (sUsername != null){
            checkingUsername(sUsername, user)
        }
    }

    private fun checkingUsername(sUsername: String, data: User) {
        mDatabaseReference.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    mDatabaseReference.child(sUsername).setValue(data)

                    preferences.setValues("nama", data.nama.toString())

                    finishAffinity()
                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java).putExtra("nama", data.nama)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RegisterActivity, "User sudah digunakan", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
