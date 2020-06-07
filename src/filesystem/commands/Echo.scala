package filesystem.commands

import filesystem.files.{Directory, File, Item}
import filesystem.filesystem.{State}

class Echo(tokens: List[String]) extends Command {
  def mkString(tokens: List[String]): String =
    tokens.mkString(" ")

  def getContents(tokens: List[String]): String =
    mkString(tokens.slice(0, tokens.size - 2))

  def printContents(tokens: List[String], state: State): State =
    state.setMessage(mkString(tokens))

  def successMessage: String = "Wrote to file"

  def isFile(wd: Directory, name: String): Boolean =
    wd
      .findItem(name)
      .flatMap(item => Some(!item.isDirectory))
      .getOrElse(true)

  def echoToFile(name: String, contents: String, state: State): State = {
    if (!isFile(state.wd, name)) state.setMessage(
      s"A directory exists with this name: $name."
    )
    else if (Item.isIllegalName(name)) state.setMessage(
      s"Illegal name: $name. File names cannot include dots or separators."
    )
    else {
      val file = new File(state.wd.parentPath, name, contents)
      UpdateItem(
        file, Directory.REPLACE_ITEM, successMessage, state
      ).getOrElse(
        // This will only be called if something else is wrong in the program
        state.setMessage("An error occurred.")
      )
    }
  }

  def appendToFile(name: String, contents: String, state: State): State =
    state.wd.findItem(name).flatMap(item => {
      val oldFile = item.asFile
      val newFile = new File(
        oldFile.parentPath,
        oldFile.name,
        oldFile.contents + "\n" + contents
      )
      UpdateItem(
        newFile, Directory.REPLACE_ITEM, successMessage, state
      )
    }).getOrElse(
      state.setMessage(s"File not found: $name.")
    )

  override def apply(state: State): State = {
    val newTokens = tokens.slice(1, tokens.size)
    val size = newTokens.size
    val doPrintContents = printContents(newTokens, state)

    if (size > 2) {
      val command = newTokens(size - 2)
      val name = tokens.last
      val contents = getContents(newTokens)

      if (command.equals(Command.ECHO_TO_FILE))
        echoToFile(name, contents, state)
      else if (command.equals(Command.APPEND_TO_FILE))
        appendToFile(name, contents, state)
      else doPrintContents
    } else doPrintContents
  }
}
