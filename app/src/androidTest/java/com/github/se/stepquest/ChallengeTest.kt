package com.github.se.stepquest

import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.services.createChallengeItem
import org.junit.Test

class ChallengeTest {
  @Test
  fun testCompletionFunctions() {
    val challengeData1 =
        createChallengeItem("testUserId", "Raytimz", ChallengeType.REGULAR_STEP_CHALLENGE)
    ChallengeType.REGULAR_STEP_CHALLENGE.completionFunction(challengeData1)
    val challengeData2 =
        createChallengeItem("testUserId", "Raytimz", ChallengeType.DAILY_STEP_CHALLENGE)
    ChallengeType.DAILY_STEP_CHALLENGE.completionFunction(challengeData2)
  }
}
