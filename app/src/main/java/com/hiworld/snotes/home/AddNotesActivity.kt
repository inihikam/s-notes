package com.hiworld.snotes.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.hiworld.snotes.R
import com.hiworld.snotes.data.AppDatabase
import com.hiworld.snotes.databinding.ActivityAddNotesBinding
import com.hiworld.snotes.model.Notes
import kotlinx.coroutines.launch

class AddNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding
    private var notes: Notes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notes = intent.getSerializableExtra("Data") as? Notes

        if (notes == null){
            Log.d("NULL", "NULL NOTES")
        } else {
            binding.judulNotes.setText(notes?.judul.toString())
            binding.tanggalNotes.setText(notes?.tanggal.toString())
            binding.isiNotes.setText(notes?.isi.toString())
        }

        binding.tambahNotes.setOnClickListener { newNotes() }
        binding.btnBack.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@AddNotesActivity, HomeActivity::class.java))
        }
    }

    private fun newNotes(){
        val judul = binding.judulNotes.text.toString()
        val tanggal = binding.tanggalNotes.text.toString()
        val isi = binding.isiNotes.text.toString()

        lifecycleScope.launch {
            if (notes == null){
                val notes = Notes(judul = judul, tanggal = tanggal, isi = isi)
                AppDatabase(this@AddNotesActivity).getNotesDao().newNotes(notes)
                finish()
            } else {
                val n = Notes(judul = judul, tanggal = tanggal, isi = isi)
                n.id = notes?.id ?: 0
                AppDatabase(this@AddNotesActivity).getNotesDao().updateNotes(n)
                finish()
            }
        }
    }
}