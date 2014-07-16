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
    "fan events in ok" in {

      var result: Int = 0

      val s  = EventSink[Int](e => result = e).push[Int](e => e + 1)
      val e1 = s push ((str: String) => str.length)
      val e2 = s push ((i: Int) => i * 2)

      s(3)
      result must_==(4)

      e1("abcde")
      result must_==(6)

      e2(7)
      result must_==(15)

    }

    "fan events out ok" in {

      var result1: Int = 0
      var result2: Int = 0
      var result3: Int = 0

      val s1 = EventSink[Int](e => result1 = e).push[Int](e => e + 1)
      val s2 = EventSink[Int](e => result2 = e).push[Int](e => e + 2)
      val s3 = EventSink[Int](e => result3 = e).push[Int](e => e + 3)

      val e = EventStream.fanOut(s1, s2, s3).push[Int](e => e * 2)

      e(3)
      result1 must_==(7)
      result2 must_==(8)
      result3 must_==(9)

    }

  }
}
