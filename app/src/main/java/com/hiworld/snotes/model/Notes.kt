package com.hiworld.snotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Notes(
    var judul:String = "",
    var tanggal:String = "",
    var isi:String = ""
): Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}