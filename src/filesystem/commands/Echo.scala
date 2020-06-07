package filesystem.commands

import filesystem.files.{Directory, File}
import filesystem.filesystem.State

class Echo(name: String, contents: String) extends Command {
  override def apply(state: State): State = {
    state.wd.findItem(name).flatMap(item => {
      val allDirectoriesInPath = state.wd.getAllDirectoriesInPath
      val oldFile = item.asFile
      val newFile = new File(
        oldFile.parentPath,
        oldFile.name,
        oldFile.contents ++ contents.substring(1, contents.length - 1)
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
  }
}
