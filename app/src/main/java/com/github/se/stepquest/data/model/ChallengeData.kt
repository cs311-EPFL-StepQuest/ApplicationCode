package com.github.se.stepquest.data.model

import com.github.se.stepquest.IUserRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class ChallengeData(
    var uuid: String = "",
    var type: ChallengeType = ChallengeType.REGULAR_STEP_CHALLENGE,
    var stepsToMake: Int = 0,
    var kilometersToWalk: Int = 0,
    var daysToComplete: Int = 0,
    var dateTime: String = "",
    val challengedUsername: String = "",
    val challengedUserUuid: String = "",
    val senderUsername: String = "",
    val senderUserUuid: String = "",
    var challengedProgress: ChallengeProgression = ChallengeProgression("", 0, 0),
    var senderProgress: ChallengeProgression = ChallengeProgression("", 0, 0)
) {
  val _uuid: String
    get() = uuid

  val _type: ChallengeType
    get() = type

  var _stepsToMake: Int
    get() = stepsToMake
    set(value) {
      stepsToMake = value
    }

  var _kilometersToWalk: Int
    get() = kilometersToWalk
    set(value) {
      kilometersToWalk = value
    }

  var _daysToComplete: Int
    get() = daysToComplete
    set(value) {
      daysToComplete = value
    }

  val _dateTime: String
    get() = dateTime

  val _challengedUsername: String
    get() = challengedUsername

  val _challengedUserUuid: String
    get() = challengedUserUuid

  val _senderUsername: String
    get() = senderUsername

  val _senderUserUuid: String
    get() = senderUserUuid

  var _challengedProgress: ChallengeProgression
    get() = challengedProgress
    set(value) {
      challengedProgress = value
    }

  var _senderProgress: ChallengeProgression
    get() = senderProgress
    set(value) {
      senderProgress = value
    }

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
          senderUserUuid = "",
          challengedProgress = ChallengeProgression("", 0, 0),
          senderProgress = ChallengeProgression("", 0, 0))
}

data class ChallengeProgression(
    val userUuid: String = "",
    var stepsCompleted: Int = 0,
    var kilometersWalked: Int = 0
)

val userRepository = IUserRepository()

enum class ChallengeType(
    val type: String,
    val messageText: String,
    val completionFunction: (ChallengeData) -> Boolean
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
          userRepository.getDailyStepsForDate(asDate(dateOffset)) { steps -> dailySteps = steps }
          totalSteps += dailySteps
          if (totalSteps >= data.stepsToMake) return true
        }
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
          userRepository.getDailyStepsForDate(asDate(dateOffset)) { steps -> dailySteps = steps }
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
