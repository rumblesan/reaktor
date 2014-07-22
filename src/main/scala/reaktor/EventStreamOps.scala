package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }


trait EventStreamOps[InputEvent] {

  def self: EventStreamBase[InputEvent]

  def push[ParentOutputEvent](
    func: ParentOutputEvent => InputEvent
  )(
    implicit strategy: Strategy
  ): EventStream[ParentOutputEvent] = {
    new EventStream(self.apply _ compose func)
  }

  def pushMaybe[ParentOutputEvent](
    func: ParentOutputEvent => Option[InputEvent]
  )(
    implicit strategy: Strategy
  ): EventStream[ParentOutputEvent] = {
    val f: ParentOutputEvent => Unit = e => {
      func(e).map(self.apply)
      ()
    }
    new EventStream(f)
  }


  def <<=[ParentOutputEvent] = push[ParentOutputEvent] _

  def <<=?[ParentOutputEvent] = pushMaybe[ParentOutputEvent] _

  def filter(
    predicate: InputEvent => Boolean
  )(
    implicit strategy: Strategy
  ): EventStream[InputEvent] = {
    new EventStream(event => if (predicate(event)) self.apply(event))
  }

  def fromSource(
    loopsPerSecond: Int,
    source: () => InputEvent
  ): EventSource[InputEvent] = {
    new EventSource(loopsPerSecond, source, self.apply, None)
  }

}


object EventStreamOps {

  def fanOut[InputEvent](
    children: EventStreamBase[InputEvent]*
  )(
    implicit strategy: Strategy
  ): EventStream[InputEvent] = {
    val func = (event: InputEvent) => children.foreach(c => c(event))
    new EventStream(func)
  }

}
