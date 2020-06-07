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

    // PWD
    case Array(PWD) => new Pwd()
    case Array(PWD, _*) => new TooManyArguments(PWD)

    // CD
    case Array(CD, path) => new Cd(path)
    case Array(CD) => new IncompleteCommand(
      CD,
      "You must supply the name of the directory you wish to change to."
    )
    case Array(CD, _*) => new TooManyArguments(CD)

    // TOUCH
    case Array(TOUCH, name) => new Touch(name)
    case Array(TOUCH) => new IncompleteCommand(
      TOUCH,
      "You must supply a name for your new file."
    )
    case Array(TOUCH, _*) => new TooManyArguments(TOUCH)

    // RM
    case Array(RM, name) => new Rm(name)
    case Array(RM) => new IncompleteCommand(
      RM,
      "You must supply the name of the item you wish to remove."
    )
    case Array(RM, _*) => new TooManyArguments(RM)

    // ECHO
    case Array(ECHO, name, contents) => new Echo(name, contents)
    case Array(ECHO) => new IncompleteCommand(
      ECHO,
      "You must supply the name of the file you wish to add contents to."
    )
    case Array(ECHO, _) => new IncompleteCommand(
      ECHO,
      "You must supply the contents you wish to add to the file."
    )
    case Array(ECHO, _*) => new TooManyArguments(ECHO)

    // CAT
    case Array(CAT, name) => new Cat(name)
    case Array(CAT) => new IncompleteCommand(
      CAT,
      "You must supply the name of the file you wish to read."
    )
    case Array(CAT, _*) => new TooManyArguments(CAT)

    case _ => new UnknownCommand
  }
}
