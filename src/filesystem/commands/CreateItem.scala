package filesystem.commands

import filesystem.files.{Directory, Item}
import filesystem.filesystem.State

abstract class CreateItem(name: String) extends Command {
  def createItem(name: String, state: State): Item

  def successMessage: String = "Created new item"

  override def apply(state: State): State =
    if (state.wd.hasItem(name))
      state.setMessage("An item with that name already exists.")
    else if (Item.isIllegalName(name))
      state.setMessage("Invalid name.")
    else UpdateItem(
      createItem(name, state),
      Directory.ADD_ITEM,
      successMessage,
      state
    ).getOrElse(
      state.setMessage("Could not create item.")
    )
}
