package com.github.se.stepquest

import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.services.createChallengeItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ChallengeTest {
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
