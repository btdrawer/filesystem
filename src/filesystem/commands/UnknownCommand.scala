package filesystem.commands

import filesystem.filesystem.State

class UnknownCommand() extends Command {
  override def apply(state: State): State = state.setMessage("Unknown command.")
}
