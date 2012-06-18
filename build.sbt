import AssemblyKeys._

assemblySettings

name := "bpEvolution"

version := "0.1"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-Xdisable-assertions","-deprecation","-optimize")

libraryDependencies += "com.github.scopt" %% "scopt" % "2.0.0"

jarName in assembly <<= (name, version) {(n,v) => n+"-"+v+".jar"}
