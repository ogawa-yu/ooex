import scala.collection.JavaConverters._

enablePlugins(JavaServerAppPackaging)

name := """ooex"""

organization := "org.ooex"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies ++= {
  Seq(
    guice,
    //lombok
    "org.projectlombok" % "lombok" % "1.16.16",
    //test
    "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test"
  )
}
