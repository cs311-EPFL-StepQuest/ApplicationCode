package com.github.se.stepquest.data.model

import com.github.se.stepquest.IUserRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


data class ChallengeData(
    var uuid: String = "",
    var type: String = "",
    var stepsToMake: Int = 0,
    var kilometersToWalk: Int = 0,
    var daysToComplete: Int = 0,
    var dateTime: String = "",
    var challengedUserUuid: String = "",
    var senderUserUuid: String = ""
)

val userRepository = IUserRepository()
enum class ChallengeType(
    type: String,
    messageText: String,
    completionFunction: (ChallengeData) -> Boolean
) {
  REGULAR_STEP_CHALLENGE(
      "REGULAR_STEP_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
          val date = LocalDate.parse(data.dateTime)
          var dailySteps = 0
          var totalSteps = 0
          for (i in 0..data.daysToComplete) {
              val dateOffset = date.plusDays(i.toLong())
              userRepository.getDailyStepsForDate(asDate(dateOffset), { steps -> dailySteps = steps })
              totalSteps += dailySteps
              if (totalSteps >= data.stepsToMake) return true
          }
          return false
        return false
      }),
  DAILY_STEP_CHALLENGE(
      "DAILY_STEP_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
        val date = LocalDate.parse(data.dateTime)
        var dailySteps = 0
          for (i in 0..data.daysToComplete) {
              val dateOffset = date.plusDays(i.toLong())
              userRepository.getDailyStepsForDate(asDate(dateOffset), { steps -> dailySteps = steps })
             if (dailySteps >= data.stepsToMake) return true
          }
        return false
      }),
  ROUTE_CHALLENGE(
      "ROUTE_CHALLENGE",
      "",
      fun(data: ChallengeData): Boolean {
        return false
      })
}

private fun asDate(localDate: LocalDate): Date {
    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}
