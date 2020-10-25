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
  ): Option[Directory] =
    if (path.isEmpty) Some(currentDirectory)
    else path.head match {
      case Directory.CURRENT_DIRECTORY => moveToDirectory(
        rootDirectory, currentDirectory, path.tail
      )
      case Directory.GO_UP => for {
        descendant <- rootDirectory.findDescendant(
          Directory.getDirectoriesFromPath(currentDirectory.parentPath)
        )
        d <- moveToDirectory(rootDirectory, descendant, path.tail)
      } yield d
      case _ => for {
        nextDirectory <- currentDirectory.findItem(path.head)
        directory <- moveToDirectory(rootDirectory, nextDirectory.asDirectory, path.tail)
      } yield directory
    }

  override def apply(state: State): State = {
    val newWd =
      if (path.startsWith(Directory.SEPARATOR))
        moveToDirectory(state.root, state.root, directoriesFromPath)
      else moveToDirectory(state.root, state.wd, directoriesFromPath)
    newWd.flatMap(wd => {
      val message =
        if (wd.isRoot) "Switched to root directory"
        else s"Switched to directory: ${wd.name}"
      Some(State(state.root, wd, message))
    }).getOrElse(
      state.setMessage("Directory not found.")
    )
  }
}
