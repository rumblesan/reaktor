package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }


class EventSink[InputEvent](
  handler: InputEvent => Unit
)(
  implicit val strategy: Strategy
) extends EventStream[InputEvent] {

  val actor: Actor[InputEvent] = Actor(handler, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

}

object EventSink {

  def apply[InputEvent](
    handler: InputEvent => Unit
  )(
    implicit strategy: Strategy
  ): EventSink[InputEvent] = new EventSink(handler)

}



