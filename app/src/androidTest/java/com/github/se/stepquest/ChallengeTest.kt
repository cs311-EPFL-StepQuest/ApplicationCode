package com.github.se.stepquest

import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.data.model.ChallengeProgression
import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.services.createChallengeItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ChallengeTest {

  @Test
  fun challengeDataIsCorrect() {
    val challenge =
        ChallengeData(
            "testUuid",
            ChallengeType.REGULAR_STEP_CHALLENGE,
            1000,
            10,
            7,
            "2021-01-01",
            "testChallengedUsername",
            "testChallengedUserUuid",
            "testSenderUsername",
            "testSenderUserUuid",
            ChallengeProgression("testChallengedUserUuid", 0, 0),
            ChallengeProgression("testSenderUserUuid", 0, 0))

    assertEquals("testUuid", challenge._uuid)
    assertEquals(ChallengeType.REGULAR_STEP_CHALLENGE, challenge._type)
    assertEquals(1000, challenge._stepsToMake)
    assertEquals(10, challenge._kilometersToWalk)
    assertEquals(7, challenge._daysToComplete)
    assertEquals("2021-01-01", challenge._dateTime)
    assertEquals("testChallengedUsername", challenge._challengedUsername)
    assertEquals("testChallengedUserUuid", challenge._challengedUserUuid)
    assertEquals("testSenderUsername", challenge._senderUsername)
    assertEquals("testSenderUserUuid", challenge._senderUserUuid)
  }

  @Test
  fun testCompletionFunctions() {
    var challenge =
        createChallengeItem(
            "testUserId",
            "testUserName",
            "testUserId",
            "testUserName",
            ChallengeType.REGULAR_STEP_CHALLENGE)
    var result = ChallengeType.REGULAR_STEP_CHALLENGE.completionFunction(challenge)
    assertEquals(false, result)
    challenge =
        createChallengeItem(
            "testUserId",
            "testUserName",
            "testUserId",
            "testUserName",
            ChallengeType.DAILY_STEP_CHALLENGE)
    result = ChallengeType.DAILY_STEP_CHALLENGE.completionFunction(challenge)
    assertEquals(false, result)
  }

  @Test fun testDeleteChallenge() {}
}
