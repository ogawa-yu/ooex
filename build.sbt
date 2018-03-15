enablePlugins(JavaServerAppPackaging)

//incOptions := incOptions.value.withNameHashing(true)
updateOptions := updateOptions.value.withCachedResolution(cachedResoluton = true)

lazy val commonSettings = Seq(scalaVersion := "2.11.8")

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
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
    "org.webjars.npm" % "typescript" % "2.7.1",
    //lombok
    "org.projectlombok" % "lombok" % "1.16.16",
    //test
    "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.webjars.npm" % "react" % "16.1.0",
    "org.webjars.npm" % "react-dom" % "16.1.0" /*exclude("org.webjars.npm", "react")*/,
    "org.webjars.npm" % "react-redux" % "5.0.6" /*exclude("org.webjars.npm", "react")*/,
    "org.webjars.npm" % "redux" % "3.7.2",
    "org.webjars.npm" % "redux-form" % "6.8.0",
    "org.webjars.npm" % "types__react" % "16.0.40",
    "org.webjars.npm" % "types__react-dom" % "16.0.3" exclude ("org.webjars.npm", "types__react"),
    "org.webjars.npm" % "types__react-redux" % "5.0.14" exclude ("org.webjars.npm", "types__react"),
    "org.webjars.npm" % "types__redux" % "3.6.31",
    "org.webjars.npm" % "types__node" % "9.4.6",
    "org.webjars" % "vue" % "2.5.13",

    "org.webjars.npm" % "core-js" % "2.4.1",
    "org.webjars.npm" % "systemjs" % "0.19.41",
    //tslint dependency
    "org.webjars.npm" % "tslint-eslint-rules" % "2.1.0",
    "org.webjars.npm" % "types__jasmine" % "2.5.38"
  )
}

// use the webjars npm directory (target/web/node_modules ) for resolution of module imports of angular2/core etc
resolveFromWebjarsNodeModulesDir := true

// use the combined tslint and eslint rules plus ng2 lint rules
//(rulesDirectories in tslint) := Some(List(
//  tslintEslintRulesDir.value,
//  ng2LintRulesDir.value
//))

routesGenerator := InjectedRoutesGenerator
