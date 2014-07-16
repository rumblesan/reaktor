package com.rumblesan.reaktor.tests

import org.specs2.mutable._

import com.rumblesan.reaktor._
import scalaz.concurrent.Strategy.Sequential


class StateSinkSpec extends Specification {

  "The 'StateSink' class" should {
    "work fine with side effects" in {

      val s = StateSink[Int, Int]((state, event) => event * 2, 0)

      s(3)

      s.getState must_==(6)

    }
    "compose multiple actions together" in {

      val s = StateSink[Int, Int]((state, event) => event * 2, 0)
      val e = s.push[Int](e => e + 1).push[String](e => e.length)

      e("abc")

      s.getState must_==(8)

    }
    "work fine with multiple events" in {

      var result: Int = 0

      val s = StateSink[Int, Int]((state, event) => event * 2, 0)
      val e = s.push[Int](e => e + 1).push[String](e => e.length)

      e("abc")
      s.getState must_==(8)

      e("abcd")
      s.getState must_==(10)

    }
    "filter ok" in {

      var result: Int = 0

      val s = StateSink[Int, Int]((state, event) => event, 0)
      val e = s.filter(e => e % 2 == 0 ).push[Int](e => e + 1)

      e(3)
      s.getState must_==(4)

      e(2)
      s.getState must_==(4)

      e(5)
      s.getState must_==(6)

    }
    "fan events in ok" in {

      val s = StateSink[Int, Int]((state, event) => event + 1, 0)
      val e1 = s push ((str: String) => str.length)
      val e2 = s push ((i: Int) => i * 2)

      s(3)
      s.getState must_==(4)

      e1("abcde")
      s.getState must_==(6)

      e2(7)
      s.getState must_==(15)

    }
    "fan events out ok" in {

      val s1 = StateSink[Int, Int]((s, e) => e + 1, 0)
      val s2 = StateSink[Int, Int]((s, e) => e + 2, 0)
      val s3 = StateSink[Int, Int]((s, e) => e + 3, 0)

      val e = EventStreamOps.fanOut(s1, s2, s3).push[Int](e => e * 2)

      e(2)
      s1.getState must_==(5)
      s2.getState must_==(6)
      s3.getState must_==(7)

    }


  }
}
