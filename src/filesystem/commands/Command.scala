package filesystem.commands

import filesystem.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val CD = "cd"
  val TOUCH = "touch"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  val SEND_TO = ">"

  def from(input: String): Command = {
    val tokens = input.split(" ").toList

    tokens match {
      // MKDIR
      case List(MKDIR, name) => new Mkdir(name)
      case List(MKDIR) => new IncompleteCommand(
        MKDIR,
        "You must supply a name for your directory."
      )
      case List(MKDIR, _*) => new TooManyArguments(MKDIR)

      // LS
      case List(LS) => new Ls()
      case List(LS, _*) => new TooManyArguments(LS)

      // PWD
      case List(PWD) => new Pwd()
      case List(PWD, _*) => new TooManyArguments(PWD)

      // CD
      case List(CD, path) => new Cd(path)
      case List(CD) => new IncompleteCommand(
        CD,
        "You must supply the name of the directory you wish to change to."
      )
      case List(CD, _*) => new TooManyArguments(CD)

      // TOUCH
      case List(TOUCH, name) => new Touch(name)
      case List(TOUCH) => new IncompleteCommand(
        TOUCH,
        "You must supply a name for your new file."
      )
      case List(TOUCH, _*) => new TooManyArguments(TOUCH)

      // RM
      case List(RM, name) => new Rm(name)
      case List(RM) => new IncompleteCommand(
        RM,
        "You must supply the name of the item you wish to remove."
      )
      case List(RM, _*) => new TooManyArguments(RM)

      // ECHO
      case List(ECHO, _*) => new Echo(tokens)
      case List(ECHO) => new IncompleteCommand(
        ECHO,
        "You must supply the name of the file you wish to add contents to."
      )

      // CAT
      case List(CAT, name) => new Cat(name)
      case List(CAT) => new IncompleteCommand(
        CAT,
        "You must supply the name of the file you wish to read."
      )
      case List(CAT, _*) => new TooManyArguments(CAT)

      case _ => new UnknownCommand
    }
  }
}
