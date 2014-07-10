package com.rumblesan.reaktor

import org.specs2.mutable._

class StateStreamSpec extends Specification {

  "The 'StateStream' class" should {

    "work as expected" in {

      var result: Int = 0
      val e = StateStream[Int, Int, Int](0, (s, e) => {
        (s + 1, e)
      })

      e.map(v => v + 1).map(v => v * 2).run(v => result = v)

      e.push(1)
      result must_==(4)

      e.state must_==(1)

      e.push(2)
      result must_==(6)

      e.state must_==(2)

    }

    "can be created inline" in {

      var result: Int = 0
      val e = EventStream[Int, Int](identity)

      e.withState(0, (s: Int, e: Int) => {
        (s + 1, s)
      }).map(v => v * 2).run(v => result = v)

      e.push(0)
      result must_==(0)
      e.push(0)
      result must_==(2)
      e.push(0)
      result must_==(4)

    }

  }
}
