import sbtsonar.SonarPlugin.autoImport.sonarProperties
import sbtsonar.SonarPlugin.autoImport.sonarScan

organization in ThisBuild := "com.knoldus"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val sonarSettings = Seq(
  sonarProperties ++= Map(
    "sonar.projectName" -> "lagom-scala-sbt",
    "sonar.host.url" -> "http://localhost:9000",
    "sonar.modules" -> "welcome-api,welcome-impl,product-api,product-impl",
    "sonar.projectKey" -> "lagom-scala-sbt",
    "sonar.login" -> "2e933edd1f548479de909ab4c76a9ed7c58c605d",
    "welcome-api.sonar.projectName" -> "welcome-api",
    "welcome-impl.sonar.projectName" -> "welcome-impl",
    "product-api.sonar.projectName" -> "product-api",
    "product-impl.sonar.projectName" -> "product-impl",
    "sonar.sourceEncoding" -> "UTF-8",
    "welcome-api.sonar.scala.scalastyle.reportPaths" -> "/home/knoldus/rccl/lagom-scala-sbt/welcome-api/target/scalastyle-result.xml",
    "welcome-impl.sonar.scala.scalastyle.reportPaths" -> "/home/knoldus/rccl/lagom-scala-sbt/welcome-impl/target/scalastyle-result.xml",
    "product-api.sonar.scala.scalastyle.reportPaths" -> "/home/knoldus/rccl/lagom-scala-sbt/product-api/target/scalastyle-result.xml",
    "product-impl.sonar.scala.scalastyle.reportPaths" -> "/home/knoldus/rccl/lagom-scala-sbt/product-impl/target/scalastyle-result.xml"
  )
)

lazy val `welcome` = (project in file("."))
  .aggregate(`welcome-api`, `welcome-impl`, `product-api`, `product-impl`).settings(sonarSettings).settings(aggregate in sonarScan := false)

lazy val `welcome-api` = (project in file("welcome-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `welcome-impl` = (project in file("welcome-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`welcome-api`)

lazy val `product-api` = (project in file("product-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `product-impl` = (project in file("product-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`product-api`)

//Define the external service’s host and port name.
lagomUnmanagedServices in ThisBuild := Map("external-service" -> "https://jsonplaceholder.typicode.com")
