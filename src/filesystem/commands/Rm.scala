package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Rm(name: String) extends Command {
  def successMessage: String = "Removed item"

  override def apply(state: State): State = {
    if (!state.wd.hasItem(name))
      state.setMessage(s"Item not found: $name.")
    else state.wd.findItem(name).flatMap(item =>
      UpdateItem(
        item, Directory.REMOVE_ITEM, successMessage, state
      )
    ).getOrElse(
      state.setMessage(s"Failed to delete item: $name.")
    )
  }
}
