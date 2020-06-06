package filesystem.commands
import filesystem.files.Directory
import filesystem.filesystem.State

class Cd(path: String) extends Command {
  def directoriesFromPath: List[String] =
    Directory.getDirectoriesFromPath(path)

  def moveToDirectory(rootDirectory: Directory, currentDirectory: Directory, path: List[String]): Directory = {
    if (path.isEmpty) currentDirectory
    else path.head match {
      case Directory.GO_UP => moveToDirectory(
        rootDirectory,
        rootDirectory.findDescendant(
          Directory.getDirectoriesFromPath(currentDirectory.parentPath)
        ),
        path.tail
      )
      case _ => {
        val nextDirectory = currentDirectory.findItem(path.head).asDirectory
        moveToDirectory(rootDirectory, nextDirectory, path.tail)
      }
    }
  }

  override def apply(state: State): State = {
    val newWd = moveToDirectory(state.root, state.wd, directoriesFromPath)
    State(state.root, newWd, s"Switched to directory: ${newWd.name}")
  }
}
