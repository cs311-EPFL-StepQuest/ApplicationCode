package com.github.se.stepquest.data.model

enum class NotificationType {
  FRIEND_REQUEST,
  CHALLENGE
}

data class NotificationData(
    var text: String = "",
    var dateTime: String = "",
    var uuid: String = "",
    var userUuid: String = "",
    var senderUuid: String = "",
    val objectUuid: String = "",
    var type: NotificationType = NotificationType.FRIEND_REQUEST
) {
  fun toMap(): Map<String, Any?> {
    return mapOf(
        "text" to text,
        "date_time" to dateTime,
        "uuid" to uuid,
        "user_uuid" to userUuid,
        "sender_uuid" to senderUuid,
        "type" to type.name)
  }

  companion object {
    fun fromMap(map: Map<String, Any?>): NotificationData {
      return NotificationData(
          text = map["text"] as String,
          dateTime = map["date_time"] as String,
          uuid = map["uuid"] as String,
          userUuid = map["user_uuid"] as String,
          senderUuid = map["sender_uuid"] as String,
          type = NotificationType.valueOf(map["type"] as String))
    }
  }

  fun isNull(): Boolean {
    return text.isEmpty() || dateTime.isEmpty() || uuid.isEmpty() || userUuid.isEmpty()
  }
}
