package filesystem.commands

import filesystem.files.{Directory, File}
import filesystem.filesystem.State

class Echo(tokens: List[String]) extends Command {
  def mkString(tokens: List[String]): String =
    tokens.mkString(" ")

  def getContents(tokens: List[String]): String =
    mkString(tokens.slice(0, tokens.size - 2))

  def printContents(tokens: List[String], state: State): State =
    state.setMessage(mkString(tokens))

  def writeToFile(name: String, contents: String, state: State): State =
    state.wd.findItem(name).flatMap(item => {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val oldFile = item.asFile
      val newFile = new File(
        oldFile.parentPath,
        oldFile.name,
        oldFile.contents ++ contents
      )

      for {
        newRoot <- Directory.updateStructure(
          state.root, allDirectoriesInPath, newFile, Directory.REPLACE_ITEM
        )
        newWd <- newRoot.findDescendant(allDirectoriesInPath)
      } yield State(
        newRoot, newWd, s"Wrote to file: ${newFile.getPrettyName}"
      )
    }).getOrElse(
      state.setMessage(s"File not found: $name.")
    )

  override def apply(state: State): State = {
    val newTokens = tokens.slice(1, tokens.size)
    val size = newTokens.size
    val doPrintContents = printContents(newTokens, state)

    if (size > 2) {
      if (newTokens(size - 2).equals(Command.SEND_TO)) writeToFile(
        tokens.last, getContents(newTokens), state
      ) else doPrintContents
    } else if (
      size == 2 && newTokens(size - 2).equals(Command.SEND_TO)
    ) state.setMessage("You have not specified anything to write to this file.")
    else doPrintContents
  }
}
