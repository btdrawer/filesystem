package filesystem.commands
import filesystem.files.{File, Item}
import filesystem.filesystem.State

class Touch(name: String) extends CreateItem(name) {
  override def createItem(name: String, state: State): Item =
    File.createEmpty(state.wd.parentPath, name)
}
