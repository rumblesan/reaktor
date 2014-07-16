package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }

import java.util.concurrent.atomic.AtomicReference


class StateSink[InputEvent, StateType](
  handler: (StateType, InputEvent) => StateType,
  initialState: StateType
)(
  implicit val strategy: Strategy
) extends EventStream[InputEvent] {

  private var internalState: AtomicReference[StateType] = new AtomicReference(initialState)

  val actor: Actor[InputEvent] = Actor(event => {
    internalState.set(handler(internalState.get, event))
  }, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

  def getState = internalState.get

}

object StateSink {

  def apply[StateType, InputEvent](
    handler: (StateType, InputEvent) => StateType,
    initialState: StateType
  )(
    implicit strategy: Strategy
  ) = new StateSink(handler, initialState)

}

