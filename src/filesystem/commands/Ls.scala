package filesystem.commands

import filesystem.files.{Directory}
import filesystem.filesystem.State

class Ls() extends Command {
  def getNamesOfContents(directory: Directory): List[String] =
    directory.contents.map(item => s"${item.name} [${item.getType}]")

  def listContents(directory: Directory): String =
    getNamesOfContents(directory).mkString("\n")

  override def apply(state: State): State =
    state.setMessage(listContents(state.wd))
}
