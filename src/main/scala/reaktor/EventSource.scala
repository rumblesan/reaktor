package com.rumblesan.reaktor

import scalaz._, Scalaz._


class EventSource[OutputEvent](
  desiredLoopsPerSecond: Int,
  source: () => OutputEvent,
  handler: OutputEvent => Unit,
  maxRepeats: Option[Int]
) {

  var running = true

  def funcLoop() = {

    var lastLoopTime: Long = System.nanoTime()
    val loopsPerSecond: Int = desiredLoopsPerSecond
    val optimalTime: Long = 1000000000L / loopsPerSecond.toLong
    var lastFpsTime: Long = 0L
    var lps: Int = 0
    var repeats = maxRepeats.getOrElse(-1)

    while(running) {

      var now: Long = System.nanoTime()
      var updateLength: Double = now - lastLoopTime
      lastLoopTime = now
      var delta: Double = updateLength / optimalTime

      lastFpsTime += updateLength.toLong
      lps += 1

      if (lastFpsTime >= 1000000000L) {
        println(s"LPS: $lps")
        lastFpsTime = 0L
        lps = 0
      }

      handler(source())

           if (repeats >  0) repeats -= 1
      else if (repeats == 0) running = false

      // we want each frame to take 10 milliseconds, to do this
      // we've recorded when we started the frame. We add 10 milliseconds
      // to this and then factor in the current time to give 
      // us our final value to wait for
      // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
      try{
        Thread.sleep(
          (lastLoopTime - System.nanoTime() + optimalTime) / 1000000
        )
      }


    }

  }

  def run = {
    val loop: Thread = new Thread(){
      override def run() = {
        funcLoop()
      }
    }
    loop.start()
  }


}

