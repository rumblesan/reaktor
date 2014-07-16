package com.rumblesan.reaktor

import scalaz._, Scalaz._
import scalaz.concurrent.{ Actor, Strategy }



trait EventStreamBase[InputEvent] {

  def apply(event: InputEvent): Unit

}


object EventStreamBase {

  implicit def ToEventStreamOps[InputEvent](e: EventStreamBase[InputEvent]): EventStreamOps[InputEvent] =
    new EventStreamOps[InputEvent] {
      def self = e
    }

}


