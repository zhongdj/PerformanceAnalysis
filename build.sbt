name := "perf-test-lib"

organization := "net.imadz"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.scalaz.stream" %% "scalaz-stream" % "0.4.1"

resolvers ++= Seq(Resolver.sonatypeRepo("releases"), Resolver.sonatypeRepo("snapshots"))

resolvers += "JAnalyse Repository" at "http://www.janalyse.fr/repository/"

resolvers += "Eclipselink" at "http://download.eclipse.org/rt/eclipselink/maven.repo/"

resolvers += "Glassfish" at "http://download.java.net/maven/glassfish/"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps",
  // "-Xfatal-warnings", // this makes cross compilation impossible from a single source
  "-Yno-adapted-args"
)

resolvers ++= Seq(Resolver.sonatypeRepo("releases"), Resolver.sonatypeRepo("snapshots"))

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10+"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "org.scalaz" %% "scalaz-concurrent" % "7.0.6",
  "org.typelevel" %% "scodec-bits" % "1.0.2",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "org.apache.commons" % "commons-io" % "1.3.2"
)

libraryDependencies += "fr.janalyse" %% "janalyse-ssh" % "0.9.10" % "compile"

libraryDependencies +=  "com.typesafe.akka" %% "akka-actor" % "2.3.4"  withJavadoc() withSources()

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies += "io.spray" %%  "spray-json" % "1.2.6"

libraryDependencies += "org.eclipse.persistence" % "org.eclipse.persistence.moxy" % "2.5.2" withSources()

libraryDependencies += "org.projectlombok" % "lombok" % "1.14.4" withJavadoc() withSources()