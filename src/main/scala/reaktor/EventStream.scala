package com.rumblesan.reaktor


trait EventStreamOps[InputEvent] {

  def self: EventStream[InputEvent]

  def push[ParentOutputEvent](func: ParentOutputEvent => InputEvent): EventSink[ParentOutputEvent] = {
    new EventSink(self.apply _ compose func)
  }

  def <<=[ParentOutputEvent] = push[ParentOutputEvent] _

  def filter(predicate: InputEvent => Boolean): EventSink[InputEvent] = {
    new EventSink(event => if (predicate(event)) self.apply(event))
  }

}

trait EventStream[InputEvent] {

  def apply(event: InputEvent): Unit

}

object EventStream {

  implicit def ToEventStreamOps[InputEvent](e: EventStream[InputEvent]): EventStreamOps[InputEvent] =
    new EventStreamOps[InputEvent] {
      def self = e
    }

}

