package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Rm(name: String) extends Command {
  override def apply(state: State): State = {
    if (!state.wd.hasItem(name))
      state.setMessage(s"Item not found: $name.")
    else {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath

      val deleteItem = for {
        itemToRemove <- state.wd.findItem(name)
        newRoot <- Directory.updateStructure(
          state.root, allDirectoriesInPath, itemToRemove, Directory.REMOVE_ITEM
        )
        newWd <- newRoot.findDescendant(allDirectoriesInPath)
      } yield State(
        newRoot, newWd, s"Removed item: ${itemToRemove.getPrettyName}"
      )

      deleteItem.getOrElse(
        state.setMessage(s"Failed to delete item: $name.")
      )
    }
  }
}
