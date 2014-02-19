import sbt._
import Keys._

object TacZombieBuild extends Build {
    lazy val root = Project(id = "taczombie", base = file(".")) settings (ScctPlugin.mergeReportSettings: _*) aggregate(model,wui,gui)
    
    lazy val model = Project(id = "model", base = file("model")) settings (ScctPlugin.instrumentSettings: _*)

    lazy val wui = Project(id = "wui", base = file("wui")) settings (ScctPlugin.instrumentSettings: _*) dependsOn(model)

    lazy val gui = Project(id = "TacZombieClient", base = file("gui")) settings (ScctPlugin.instrumentSettings: _*) dependsOn(model)
}
