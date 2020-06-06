package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

class Mkdir(val name: String) extends Command {
  override def apply(state: State): State = {
    if (state.wd.hasItem(name))
      state.setMessage("An item with that name already exists.")
    else if (Item.isIllegalName(name))
      state.setMessage("Invalid name.")
    else {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val newDirectory = Directory.createEmpty(state.wd.path, name)
      val newRoot = Directory.updateStructure(
        state.root, allDirectoriesInPath, newDirectory
      )
      val newWd = newRoot.findDescendant(allDirectoriesInPath)
      State(newRoot, newWd, s"Created new directory: ${newDirectory.name}")
    }
  }
}
