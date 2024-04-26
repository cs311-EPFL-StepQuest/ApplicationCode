package com.github.se.stepquest.data.model

import com.google.firebase.Timestamp


data class NotificationData(val title: String, val dateTime: Timestamp, val uuid: String, val showButtons: Boolean)