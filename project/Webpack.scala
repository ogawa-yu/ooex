import java.net.InetSocketAddress

import play.sbt.PlayRunHook
import sbt._

import scala.sys.process.Process

object Webpack {
  def isWindows() = {
    System.getProperty("os.name").toUpperCase().contains("WIN")
  }

  def apply(base: File): PlayRunHook = {
    object WebpackHook extends PlayRunHook {
      var process: Option[Process] = None

      override def beforeStarted() = {
        if (!(base / "frontend" / "node_modules").exists())
          if (isWindows()) {
            Process("cmd /c npm install", base / "frontend").run
          } else {
            Process("npm install", base / "frontend").run
          }
      }

      override def afterStarted(addr: InetSocketAddress): Unit = {
        process = Some(
          if (isWindows()) {
            Process("cmd /c node node_modules/webpack/bin/webpack.js --watch", base / "frontend").run
          } else {
            Process("node node_modules/webpack/bin/webpack.js --watch", base / "frontend").run
          }
        )
      }

      override def afterStopped(): Unit = {
        process.foreach { proc =>
          println("Stopping node process..")
          proc.destroy()
        }
        process = None
      }
    }

    WebpackHook
  }
}
