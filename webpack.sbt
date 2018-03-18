import scala.sys.process.Process

/**
  * Exit codes
  */
val Success = 0
val Error = 1

PlayKeys.playRunHooks += baseDirectory.map(Webpack.apply).value

def runScript(script: String)(implicit dir: File): Int = Process(script, dir)!

def uiWasInstalled(implicit dir: File): Boolean = (dir / "node_modules").exists()

def runNpmInstall(implicit dir: File): Int =
  if (uiWasInstalled) Success else runScript(
    if (Webpack.isWindows()) {
      "cmd /c npm install"
    } else {
      "npm install"
    }
  )

def ifUiInstalled(task: => Int)(implicit dir: File): Int =
  if (runNpmInstall == Success) task
  else Error

def runProdBuild(implicit dir: File): Int = ifUiInstalled(runScript(
  if (Webpack.isWindows())
    "cmd /c npm run build"
  else
    "npm run build"
))

lazy val `ui-prod-build` = TaskKey[Unit]("Run UI build when packaging the application.")
`ui-prod-build` := {
  implicit val UIroot = baseDirectory.value / "frontend"
  if (runProdBuild != Success) throw new Exception("UI Build failed.")
}

dist := (dist dependsOn `ui-prod-build`).value

stage := (stage dependsOn `ui-prod-build`).value
