package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }

import java.util.concurrent.atomic.AtomicReference


class StateSink[InputEvent, StateType, OutputEvent](
  stateModifier: (StateType, InputEvent) => (StateType, Option[OutputEvent]),
  initialState: StateType,
  eventHandler: Option[OutputEvent] => Unit
)(
  implicit val strategy: Strategy
) extends EventStream[InputEvent] {

  private var internalState: AtomicReference[StateType] = new AtomicReference(initialState)

  val actor: Actor[InputEvent] = Actor(event => {
    val (newState, eventOpt) = stateModifier(internalState.get, event)
    internalState.set(newState)
    eventHandler(eventOpt)
  }, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

  def getState = internalState.get

}

object StateSink {

  def apply[InputEvent, StateType](
    stateModifier: (StateType, InputEvent) => StateType,
    initialState: StateType
  )(
    implicit strategy: Strategy
  ) = new StateSink(
    (state: StateType, event: InputEvent) => {
      (stateModifier(state, event), None)
    },
    initialState,
    (e: Any) => ()
  )

}

