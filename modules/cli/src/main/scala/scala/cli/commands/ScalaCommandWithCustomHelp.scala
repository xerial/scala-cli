package scala.cli.commands

import caseapp.core.Error
import caseapp.core.help.{Help, HelpCompanion, RuntimeCommandsHelp}
import caseapp.core.parser.Parser

import scala.cli.commands.default.DefaultOptions
import scala.cli.commands.shared.{HasLoggingOptions, ScalaCliHelp}
import scala.cli.commands.util.HelpUtils.*
import scala.cli.launcher.LauncherOptions

abstract class ScalaCommandWithCustomHelp[T <: HasLoggingOptions](
  actualHelp: => RuntimeCommandsHelp
)(
  implicit
  myParser: Parser[T],
  help: Help[T]
) extends ScalaCommand[T] {
  private def launcherHelp: Help[LauncherOptions] = HelpCompanion.deriveHelp[LauncherOptions]

  protected def customHelp(showHidden: Boolean): String = {
    val helpString         = actualHelp.help(helpFormat, showHidden)
    val launcherHelpString = launcherHelp.optionsHelp(helpFormat, showHidden)
    s"""$helpString
       |
       |$launcherHelpString""".stripMargin
  }

  protected def customHelpAsked(showHidden: Boolean): Nothing = {
    println(customHelp(showHidden))
    sys.exit(0)
  }
  override def helpAsked(progName: String, maybeOptions: Either[Error, T]): Nothing =
    customHelpAsked(showHidden = false)
  override def fullHelpAsked(progName: String): Nothing = customHelpAsked(showHidden = true)
}
