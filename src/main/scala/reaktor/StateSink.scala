package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }

import java.util.concurrent.atomic.AtomicReference


object StateSink {

  def apply[InputEvent, StateType](
    stateModifier: (StateType, InputEvent) => StateType,
    initialState: StateType
  )(
    implicit strategy: Strategy
  ) = new StateStream(
    (state: StateType, event: InputEvent) => {
      (stateModifier(state, event), None)
    },
    initialState,
    (e: Any) => ()
  )

}

