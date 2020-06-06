package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Cd(path: String) extends Command {
  def directoriesFromPath: List[String] =
    Directory.getDirectoriesFromPath(path)

  def moveToDirectory(
                       rootDirectory: Directory,
                       currentDirectory: Directory,
                       path: List[String]
                     ): Option[Directory] = {
    if (path.isEmpty) Some(currentDirectory)
    else path.head match {
      case Directory.CURRENT_DIRECTORY => moveToDirectory(
        rootDirectory, currentDirectory, path.tail
      )
      case Directory.GO_UP => {
        val descendant = rootDirectory.findDescendant(
          Directory.getDirectoriesFromPath(currentDirectory.parentPath)
        )
        descendant.flatMap(d => moveToDirectory(rootDirectory, d, path.tail))
      }
      case _ => {
        val nextDirectory = currentDirectory.findItem(path.head)
        nextDirectory.flatMap(
          directory => moveToDirectory(rootDirectory, directory.asDirectory, path.tail)
        )
      }
    }
  }

  override def apply(state: State): State = {
    val newWd = moveToDirectory(state.root, state.wd, directoriesFromPath)
    newWd.flatMap(wd => {
      val message = {
        if (wd.isRoot) "Switched to root directory"
        else s"Switched to directory: ${wd.name}"
      }
      Some(State(state.root, wd, message))
    }).getOrElse(
      state.setMessage("Directory not found.")
    )
  }
}
