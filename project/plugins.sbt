addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-RC1")

// provides server side compilation of typescript to ecmascript 5 or 3
addSbtPlugin("name.de-vries" % "sbt-typescript" % "2.6.2")

// checks your typescript code for error prone constructions
addSbtPlugin("name.de-vries" % "sbt-tslint" % "5.7.0")

// runs jasmine tests
addSbtPlugin("name.de-vries" % "sbt-jasmine" % "0.0.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0")

resolvers += Classpaths.typesafeReleases
resolvers += Resolver.bintrayRepo("webjars","maven")