package com.rumblesan.reaktor.concurrency

import java.util.concurrent.atomic.AtomicReference


case class Node[A](val a: A)

object NodeQueue {

  def apply[A](): AtomicReference[Node[A]] = new AtomicReference[Node[A]]

}

