name := "Reaktor"

organization := "com.rumblesan"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.13" % "test",
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "org.scalaz" %% "scalaz-concurrent" % "7.0.6",
  "org.scalaz" %% "scalaz-effect" % "7.0.6",
  "org.scalaz" %% "scalaz-typelevel" % "7.0.6",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.6" % "test"
)

initialCommands := "import com.rumblesan.reaktor._"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")

// settings for sbt-release plugin
releaseSettings

