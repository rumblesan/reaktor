package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }


class StateSink[InputEvent, StateType](
  handler: (StateType, InputEvent) => StateType,
  initialState: StateType
)(
  implicit val strategy: Strategy
) extends EventStream[InputEvent] {

  private var state: StateType = initialState

  val actor: Actor[InputEvent] = Actor(event => {
    state = handler(state, event)
  }, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

  def getState = state

}

object StateSink {

  def apply[StateType, InputEvent](
    handler: (StateType, InputEvent) => StateType,
    initialState: StateType
  )(
    implicit strategy: Strategy
  ) = new StateSink(handler, initialState)

}

