name := "Reaktor"

organization := "com.rumblesan"

version := "0.1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.13" % "test"
)

initialCommands := "import com.rumblesan.reaktor._"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")
