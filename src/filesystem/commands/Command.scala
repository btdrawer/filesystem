package filesystem.commands

import filesystem.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  def from(input: String): Command = input.split(" ") match {
    case Array("mkdir", name) => new Mkdir(name)
    case _ => new UnknownCommand
  }
}
