package com.rumblesan.reaktor

import scalaz._, Scalaz._

class EventStream[InputEvent, OutputEvent](
  handlerFunc: InputEvent => OutputEvent
) extends EventOps[InputEvent, OutputEvent] {

  override var listeners: List[OutputEvent => Unit] = Nil

  override val handler: InputEvent => OutputEvent = handlerFunc

}

object EventStream {

  def apply[InputEvent, OutputEvent](handlerFunc: InputEvent => OutputEvent): EventStream[InputEvent, OutputEvent] = {
    new EventStream[InputEvent, OutputEvent](handlerFunc)
  }

}

