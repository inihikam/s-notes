package com.hiworld.snotes.log.register

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hiworld.snotes.R
import com.hiworld.snotes.utils.Preferences
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_register_photoupload.*
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.hiworld.snotes.home.HomeActivity
import com.hiworld.snotes.log.login.User
import java.util.*

class RegisterPhotoscreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath: Uri

    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference
    lateinit var preferences : Preferences

    lateinit var user : User
    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_photoupload)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        user = intent.getParcelableExtra("data")!!
        txt_hello.text = "Welcome,\n"+user.nama

        btn_addphoto.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                btn_addphoto.setImageResource(R.drawable.icon_plus)
                im_profile.setImageResource(R.drawable.avatar)
            } else {
                ImagePicker.with(this)
                    .galleryOnly()
                    .start()
            }
        }

        btn_skip.setOnClickListener {
            finishAffinity()

            val goHome = Intent(this@RegisterPhotoscreenActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_save.setOnClickListener {
            if (filePath != null){
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading photo....")
                progressDialog.show()

                val ref = storageReference.child("images/"+UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Photo sudah terupload", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            saveToFirebase(it.toString())
//                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()
                        val goHome = Intent(this@RegisterPhotoscreenActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }
                    .addOnFailureListener{
                        progressDialog.dismiss()
                        Toast.makeText(this, "Upload failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        taskSnapshot -> val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload "+progress.toInt()+"%")
                    }
            }
        }
    }

    private fun saveToFirebase(url: String) {

        mFirebaseDatabase.child(user.username!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                user.url = url
                mFirebaseDatabase.child(user.username!!).setValue(user)

                preferences.setValues("nama", user.nama.toString())
                preferences.setValues("user", user.username.toString())
                preferences.setValues("email", user.email.toString())
                preferences.setValues("status", "1")
                preferences.setValues("url", url)

                finishAffinity()
                val intent = Intent(this@RegisterPhotoscreenActivity,
                    HomeActivity::class.java)
                startActivity(intent)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterPhotoscreenActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .galleryOnly()
            .start()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {

    }

    override fun onBackPressed() {
        Toast.makeText(this, "Harap bersabar untuk menekan tombol", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(im_profile)

            btn_save.visibility = View.VISIBLE
            btn_addphoto.setImageResource(R.drawable.ic_baseline_delete_forever_24)
        } else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}