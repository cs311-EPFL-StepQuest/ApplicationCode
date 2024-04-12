package com.github.se.stepquest.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MapScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MapScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("MapScreen") }) {

  // Structural elements of the UI
  val createRouteButton: KNode = child { hasTestTag("createRouteButton") }
}
