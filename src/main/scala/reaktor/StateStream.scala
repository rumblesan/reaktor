package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }

import java.util.concurrent.atomic.AtomicReference


class StateStream[InputEvent, StateType, OutputEvent](
  stateModifier: (StateType, InputEvent) => (StateType, Option[OutputEvent]),
  initialState: StateType,
  eventHandler: OutputEvent => Unit
)(
  implicit val strategy: Strategy
) extends EventStreamBase[InputEvent] {

  private var internalState: AtomicReference[StateType] = new AtomicReference(initialState)

  val actor: Actor[InputEvent] = Actor(event => {
    val (newState, eventOpt) = stateModifier(internalState.get, event)
    internalState.set(newState)
    eventOpt.map(eventHandler)
  }, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

  def getState = internalState.get

}

object StateStream {

  def apply[InputEvent, StateType, OutputEvent](
    stateModifier: (StateType, InputEvent) => (StateType, Option[OutputEvent]),
    initialState: StateType,
    eventHandler: OutputEvent => Unit
  )(
    implicit strategy: Strategy
  ) = new StateStream(
    stateModifier,
    initialState,
    eventHandler
  )

}

