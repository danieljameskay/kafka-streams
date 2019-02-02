name := "kafka-streams"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts( Artifact("javax.ws.rs-api", "jar", "jar"))
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.1.0"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"

