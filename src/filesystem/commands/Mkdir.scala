package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

class Mkdir(val name: String) extends Command {
  def isIllegalName: Boolean =
    name.contains(Directory.SEPARATOR) ||
    name.contains(".")

  def createDirectory(state: State): Directory =
    Directory.createEmpty(state.wd.path, name)

  def updateStructure(currentDirectory: Directory, path: List[String], newItem: Item): Directory = {
     if (path.isEmpty) currentDirectory.addItem(newItem)
     else {
       val oldDirectory = currentDirectory.findItem(path.head).asDirectory
       currentDirectory.replaceItem(oldDirectory.name,
         updateStructure(oldDirectory, path.tail, newItem)
       )
     }
  }

  override def apply(state: State): State = {
    if (state.wd.hasItem(name))
      state.setMessage("An item with that name already exists.")
    else if (isIllegalName)
      state.setMessage("Invalid name.")
    else {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val newDirectory = createDirectory(state)
      val newRoot = updateStructure(
        state.root, allDirectoriesInPath, newDirectory
      )
      val newWd = newRoot.findDescendant(allDirectoriesInPath)
      State(newRoot, newWd, s"Created new directory: ${newDirectory.name}")
    }
  }
}
