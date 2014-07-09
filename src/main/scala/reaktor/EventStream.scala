package com.rumblesan.reaktor

import scalaz._, Scalaz._

class EventStream[InputEvent, OutputEvent](handlerFunc: InputEvent => OutputEvent) {

  private var listeners: List[OutputEvent => Unit] = Nil

  private val handler: InputEvent => OutputEvent = handlerFunc

  type Subscriber[Out] = EventStream[OutputEvent, Out]
  type Pusher[In] = EventStream[In, OutputEvent]

  def subscribe[SubscriberOutput](
    subscriber: EventStream[OutputEvent, SubscriberOutput]
  ): EventStream[OutputEvent, SubscriberOutput] = {
    val func: OutputEvent => Unit = event => subscriber.push(event)
    listeners = func :: listeners
    subscriber
  }

  def push(event: InputEvent): Unit = {

    val newEvent: OutputEvent = handler(event)

    listeners.map(l => l(newEvent))

    ()
  }

  def map[SubscriberOutput](func: OutputEvent => SubscriberOutput): Subscriber[SubscriberOutput] = {
    subscribe(new EventStream[OutputEvent, SubscriberOutput](func))
  }

  def filter(predicate: OutputEvent => Boolean): Subscriber[OutputEvent] = {
    val subscriber = EventStream[OutputEvent, OutputEvent](identity)

    val func: OutputEvent => Unit = event => {
      if (predicate(event)) subscriber.push(event)
    }

    listeners = func :: listeners
    subscriber
  }

  def combine[PusherInput](pusher: Pusher[PusherInput]): EventStream[OutputEvent, OutputEvent] = {
    val newStream = EventStream[OutputEvent, OutputEvent](identity)
    subscribe[OutputEvent](newStream)
    pusher.subscribe[OutputEvent](newStream)
    newStream
  }

  def run(func: OutputEvent => Unit): Unit = {
    listeners = func :: listeners
  }

}

object EventStream {

  def apply[InputEvent, OutputEvent](handlerFunc: InputEvent => OutputEvent): EventStream[InputEvent, OutputEvent] = {
    new EventStream[InputEvent, OutputEvent](handlerFunc)
  }

}

