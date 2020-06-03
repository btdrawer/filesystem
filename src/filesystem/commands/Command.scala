package filesystem.commands

import filesystem.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"

  def from(input: String): Command = input.split(" ") match {
      // MKDIR
    case Array(MKDIR, name) => new Mkdir(name)
    case Array(MKDIR) => new IncompleteCommand(
      MKDIR,
      "You must supply a name for your directory."
    )
    case Array(MKDIR, _*) => new TooManyArguments(MKDIR)

      // LS
    case Array(LS) => new Ls()
    case Array(LS, _*) => new TooManyArguments(LS)

    case _ => new UnknownCommand
  }
}
