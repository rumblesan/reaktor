package com.rumblesan.reaktor.tests

import org.specs2.mutable._

import com.rumblesan.reaktor._


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

  }
}
