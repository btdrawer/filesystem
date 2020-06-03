package filesystem.commands

import filesystem.filesystem.State

class IncompleteCommand(name: String, text: String) extends Command {
  override def apply(state: State): State = state.setMessage(s"Incomplete command: $name. $text")
}
