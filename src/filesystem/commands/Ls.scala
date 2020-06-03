package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

class Ls() extends Command {
  def getNamesOfContents(directory: Directory): List[String] =
    directory.contents.map {
      case dir: Directory => s"${dir.name} [DIR]"
      case item => item.name
    }

  def listContents(directory: Directory): String =
    getNamesOfContents(directory).mkString("\n")

  override def apply(state: State): State = state.setMessage(listContents(state.wd))
}
