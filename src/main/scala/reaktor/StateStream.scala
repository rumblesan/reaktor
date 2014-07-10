package com.rumblesan.reaktor

class StateStream[InputEvent, StateType, OutputEvent](
  initialState: StateType,
  handlerFunc: (StateType, InputEvent) => (StateType, OutputEvent)
) extends EventOps[InputEvent, OutputEvent] {

  override var listeners: List[OutputEvent => Unit] = Nil

  private var internalState: StateType = initialState

  override val handler: InputEvent => OutputEvent = {
    event => {
      val (newState, newEvent) = handlerFunc(internalState, event)
      internalState = newState
      newEvent
    }
  }

  def state = internalState

}

object StateStream {

  def apply[InputEvent, StateType, OutputEvent](
    initialState: StateType,
    handlerFunc: (StateType, InputEvent) => (StateType, OutputEvent)
  ) = {
    new StateStream(initialState, handlerFunc)
  }

}

