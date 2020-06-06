package filesystem.commands
import filesystem.files.{Directory, File, Item}
import filesystem.filesystem.State

class Touch(name: String) extends Command {
  override def apply(state: State): State = {
    if (state.wd.hasItem(name))
      state.setMessage("An item with that name already exists.")
    else if (Item.isIllegalName(name))
      state.setMessage("Invalid name.")
    else {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val newFile = new File(state.wd.parentPath, name)
      val newRoot = Directory.updateStructure(
        state.root, allDirectoriesInPath, newFile
      )
      val newWd = newRoot.findDescendant(allDirectoriesInPath)
      State(newRoot, newWd, s"Created new file: ${newFile.name}")
    }
  }
}
