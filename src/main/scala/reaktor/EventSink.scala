package com.rumblesan.reaktor

import scalaz._, Scalaz._

import scalaz.concurrent.{ Actor, Strategy }

class EventSink[InputEvent](
  handler: InputEvent => Unit
)(
  implicit val strategy: Strategy
) {

  val actor: Actor[InputEvent] = Actor(handler, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

  def <<=[ParentOutputEvent](func: ParentOutputEvent => InputEvent): EventSink[ParentOutputEvent] = {
    new EventSink(apply _ compose func)
  }

}

