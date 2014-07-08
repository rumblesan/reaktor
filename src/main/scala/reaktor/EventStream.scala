package com.rumblesan.reaktor

trait EventStream[EventType] {

  def events: List[EventType]

}


object EventStream {

  def push[EventType](
    stream: EventStream[EventType],
    event: EventType
  ): EventStream[EventType] = {
    stream
  }

  def map[EventType](
    stream: EventStream[EventType],
    func: EventType => EventType
  ): EventStream[EventType] = {
    stream
  }

  def flatMap[EventType](
    stream: EventStream[EventType],
    func: EventType => EventStream[EventType]
  ): EventStream[EventType] = {
    stream
  }

  def filter[EventType](
    stream: EventStream[EventType],
    func: EventType => Boolean
  ): EventStream[EventType] = {
    stream
  }

  def combine[EventType](
    left: EventStream[EventType],
    right: EventStream[EventType]
  ): EventStream[EventType] = {
    left
  }

  def run[EventType](
    stream: EventStream[EventType],
    func: EventType => Unit
  ): Unit = {
    ()
  }

}


