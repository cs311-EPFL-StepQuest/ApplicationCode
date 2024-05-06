package com.github.se.stepquest.data.model

data class ChallengeData(
    var uuid: String = "",
    var type: ChallengeType,
    var stepsToMake: Int = 0,
    var kilometersToWalk: Int = 0,
    var daysToComplete: Int = 0,
    var dateTime: String = "",
    var challengedUserUuid: String = "",
    var senderUserUuid: String = ""
) {}

enum class ChallengeType(
    type: String,
    messageText: String,
    completionFunction: (ChallengeData) -> Boolean
) {
  REGULAR_STEP_CHALLENGE(
      "REGULAR_STEP_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
        return false
      }),
  DAILY_STEP_CHALLENGE(
      "DAILY_STEP_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
        return false
      }),
  ROUTE_CHALLENGE(
      "ROUTE_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
        return false
      })
}
