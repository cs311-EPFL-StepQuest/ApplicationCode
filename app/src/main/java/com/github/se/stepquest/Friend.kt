package com.github.se.stepquest

data class Friend(val name: String = "", val status: Boolean = false) {
  val _name: String
    get() = name

  val _status: Boolean
    get() = status

  constructor() : this(name = "", status = false)
}
