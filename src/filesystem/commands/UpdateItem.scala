package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

object UpdateItem {
  def apply(item: Item, action: String, successMessage: String, state: State): Option[State] = {
    val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
    for {
      newRoot <- Directory.updateStructure(
        state.root, allDirectoriesInPath, item, action
      )
      newWd <- newRoot.findDescendant(allDirectoriesInPath)
    } yield State(
      newRoot, newWd, s"$successMessage: ${item.getPrettyName}"
    )
  }
}
