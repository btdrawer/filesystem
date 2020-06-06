package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

class Mkdir(val name: String) extends CreateItem(name) {
  override def createItem(name: String, state: State): Item =
    Directory.createEmpty(state.wd.path, name)
}
