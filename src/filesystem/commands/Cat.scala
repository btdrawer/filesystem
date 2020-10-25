package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Cat(name: String) extends Command {
  def getFileContents(wd: Directory): String = wd
    .findItem(name)
    .flatMap(item => Some(item.asFile.contents))
    .getOrElse(s"File not found: $name")

  override def apply(state: State): State =
    state.setMessage(getFileContents(state.wd))
}
