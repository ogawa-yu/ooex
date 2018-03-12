enablePlugins(JavaServerAppPackaging)

lazy val root = (project in file("."))
  .settings(
    name := """ooex""",
    organization := "org.ooex",
    version := "1.0-SNAPSHOT"
  )
  .enablePlugins(PlayJava)
  .enablePlugins(SbtWeb)

pomIncludeRepository := { _ => false }

libraryDependencies ++= {
  Seq(
    guice,
    "org.webjars" %% "webjars-play" % "2.6.3",
    "org.webjars" % "react" % "15.0.0",
    //lombok
    "org.projectlombok" % "lombok" % "1.16.16",
    //test
    "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test"
  )
}
