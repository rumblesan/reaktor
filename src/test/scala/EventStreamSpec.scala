package com.rumblesan.reaktor

import org.specs2.mutable._

class EventStreamSpec extends Specification {

  "The 'EventStream' class" should {

    "work fine with just one stream" in {

      var result: Int = 0
      val e = EventStream[Int, Int](identity)

      e.map(v => v + 1).map(v => v * 2).run(v => result = v)

      e.push(1)
      result must_==(4)

      e.push(2)
      result must_==(6)

    }

    "work fine with multiple streams" in {

      var result1: Int = 0
      var result2: Int = 0
      var result3: Int = 0
      val e = EventStream[String, Int](s => s.length)

      val s1 = e.map(l => l * 2).run(n => result1 = n)
      val s2 = e.map(l => l * 4).run(n => result2 = n)

      e.run(n => result3 = n)

      e.push("ab")

      result1 must_==(4)
      result2 must_==(8)

      result3 must_==(2)


    }

    "combine ok" in {

      var result: List[Int] = Nil

      val e = EventStream[String, Int](s => s.length)

      val s1 = e.map(l => l * 2)
      val s2 = e.map(l => l * 4)

      s2.combine(s1).run(v => result = v :: result)

      e.push("ab")

      result must_==(List(4, 8))

    }

    "filter ok" in {

      var result: List[Int] = Nil

      val e = EventStream[String, Int](s => s.length)

      val s1 = e.map(l => l * 2)
      val s2 = e.map(l => l * 4)

      s2.combine(s1).filter(v => v < 5).run(v => result = v :: result)

      e.push("ab")

      result must_==(List(4))

    }

  }
}
