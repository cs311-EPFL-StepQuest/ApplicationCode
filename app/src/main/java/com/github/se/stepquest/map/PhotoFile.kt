package com.github.se.stepquest.map

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

fun getPhotoFile(context: Context): File {
  val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
  val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
  return File.createTempFile(
      "JPEG_${timeStamp}_", /* prefix */ ".jpg", /* suffix */ storageDir /* directory */)
}
