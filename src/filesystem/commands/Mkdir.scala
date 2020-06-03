package filesystem.commands

import filesystem.files.Directory
import filesystem.filesystem.State

class Mkdir(val name: String) extends Command {
  override def apply(state: State): State = State(
    state.root,
    Directory.addItemToDirectory(
      state.wd,
      Directory.createEmpty(
        s"${state.wd.parentPath}/${state.wd.name}",
        name
      )
    ),
    s"Created directory: $name"
  )
}
