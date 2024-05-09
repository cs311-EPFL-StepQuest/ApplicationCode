package com.github.se.stepquest

import android.net.Uri

data class Friend(
    val name: String = "",
    val profilePicture: Uri? = null,
    val status: Boolean = false
)
