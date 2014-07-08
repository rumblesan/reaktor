package com.rumblesan.reaktor

trait StateStream[EventType, StateType] {
}

object StateStream {

  def map[EventType, StateType](
    stream: StateStream[EventType, StateType],
    func: EventType => EventType
  ): StateStream[EventType, StateType] = {
    stream
  }

  def filter[EventType, StateType](
    stream: StateStream[EventType, StateType],
    func: EventType => Boolean
  ): StateStream[EventType, StateType] = {
    stream
  }

  def run[EventType, StateType](
    stream: StateStream[EventType, StateType],
    func: StateType => Unit
  ): Unit = {
    ()
  }

}

