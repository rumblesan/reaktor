package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }


object EventSink {

  def apply[InputEvent](
    handler: InputEvent => Unit
  )(
    implicit strategy: Strategy
  ): EventStream[InputEvent] = new EventStream(handler)

}



