package com.github.se.stepquest.data.model

data class ChallengeData(
    val uuid: String = "",
    val type: ChallengeType,
    var stepsToMake: Int = 0,
    var kilometersToWalk: Int = 0,
    var daysToComplete: Int = 0,
    val dateTime: String = "",
    val challengedUsername: String = "",
    val challengedUserUuid: String = "",
    val senderUsername: String = "",
    val senderUserUuid: String = ""
) {
  constructor() :
      this(
          uuid = "",
          type = ChallengeType.REGULAR_STEP_CHALLENGE,
          stepsToMake = 0,
          kilometersToWalk = 0,
          daysToComplete = 0,
          dateTime = "",
          challengedUsername = "",
          challengedUserUuid = "",
          senderUsername = "",
          senderUserUuid = "")
}

enum class ChallengeType(
    val type: String,
    val messageText: String,
    val completionFunction: (ChallengeData) -> Boolean
) {
  REGULAR_STEP_CHALLENGE(
      type = "REGULAR_STEP_CHALLENGE",
      messageText = "Regular Step Challenge",
      completionFunction = { false }),
  DAILY_STEP_CHALLENGE(
      type = "DAILY_STEP_CHALLENGE",
      messageText = "Daily Step Challenge",
      completionFunction = { false }),
  ROUTE_CHALLENGE(
      type = "ROUTE_CHALLENGE", messageText = "Route Challenge", completionFunction = { false })
}
