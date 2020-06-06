package filesystem.files

import filesystem.filesystem.FilesystemException

class File(override val parentPath: String, override val name: String, contents: String)
  extends Item(parentPath, name) {
  override def asFile: File = this

  override def asDirectory: Directory =
    throw new FilesystemException("Cannot be converted to a directory.")

  override def getType: String = "File"
}

object File {
  def createEmpty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
