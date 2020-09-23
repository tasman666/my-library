name := "MyLibrary"

version := "0.1"

scalaVersion := "2.13.3"

val http4sVersion = "0.21.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds")