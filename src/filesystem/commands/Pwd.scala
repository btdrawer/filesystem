package filesystem.commands
import filesystem.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State = {
    val path = state.wd.path
    if (state.wd.isRoot) state.setMessage(path)
    else state.setMessage(path.substring(1))
  }
}
