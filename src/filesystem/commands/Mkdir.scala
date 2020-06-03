package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Mkdir(val name: String) extends Command {
  override def apply(state: State): State =
    State(
      state.root,
      Directory.addEmpty(state.wd, name),
      s"Created directory: $name"
    )
}
