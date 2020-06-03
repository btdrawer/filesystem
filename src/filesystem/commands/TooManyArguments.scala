package filesystem.commands

import filesystem.filesystem.State

class TooManyArguments(val name: String) extends Command {
  override def apply(state: State): State =
    state.setMessage(s"Too many arguments: $name.")
}
