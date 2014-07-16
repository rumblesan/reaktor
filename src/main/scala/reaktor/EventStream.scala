package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }


class EventStream[InputEvent](
  handler: InputEvent => Unit
)(
  implicit val strategy: Strategy
) extends EventStreamBase[InputEvent] {

  val actor: Actor[InputEvent] = Actor(handler, e => println(e.getMessage))

  def apply(event: InputEvent): Unit = actor(event)

}

object EventStream {

  def apply[InputEvent](
    handler: InputEvent => Unit
  )(
    implicit strategy: Strategy
  ): EventStream[InputEvent] = new EventStream(handler)

}

