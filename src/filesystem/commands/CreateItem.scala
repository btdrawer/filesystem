package filesystem.commands
import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

abstract class CreateItem(name: String) extends Command {
  def createItem(name: String, state: State): Item

  override def apply(state: State): State = {
    if (state.wd.hasItem(name))
      state.setMessage("An item with that name already exists.")
    else if (Item.isIllegalName(name))
      state.setMessage("Invalid name.")
    else {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val newItem = createItem(name, state)
      val newRoot = Directory.updateStructure(
        state.root, allDirectoriesInPath, newItem
      )
      val newWd = newRoot.findDescendant(allDirectoriesInPath)
      State(newRoot, newWd, s"Created new item: ${newItem.name} [${newItem.getType}]")
    }
  }
}
