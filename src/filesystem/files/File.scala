package filesystem.files

class File(override val parentPath: String, override val name: String) extends Item(parentPath, name) {
  override def asDirectory: Directory =
    throw new RuntimeException("Cannot be converted to a directory")

  override def getType: String = "File"
}
