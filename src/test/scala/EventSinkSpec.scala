package com.rumblesan.reaktor.tests

import org.specs2.mutable._

import com.rumblesan.reaktor._


class EventSinkSpec extends Specification {

  "The 'EventSink' class" should {
    "work fine with side effects" in {

      var result: Int = 0

      val s = EventSink[Int](e => result = e * 2)

      s(3)

      result must_==(6)

    }
    "compose multiple actions together" in {

      var result: Int = 0

      val s = EventSink[Int](e => result = e * 2).push[Int](e => e + 1).push[String](e => e.length)

      s("abc")

      result must_==(8)

    }
    "work fine with multiple events" in {

      var result: Int = 0

      val s = EventSink[Int](e => result = e * 2).push[Int](e => e + 1).push[String](e => e.length)

      s("abc")
      result must_==(8)

      s("abcd")
      result must_==(10)

    }
    "filter ok" in {

      var result: Int = 0

      val s = EventSink[Int](e => result = e).filter(e => e % 2 == 0 ).push[Int](e => e + 1)

      s(3)
      result must_==(4)

      result = 0
      s(2)
      result must_==(0)

      s(5)
      result must_==(6)

    }

  }
}
