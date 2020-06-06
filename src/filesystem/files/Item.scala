package filesystem.files

abstract class Item(val parentPath: String, val name: String) {
  def path: String = s"$parentPath/$name"

  def isIllegalName: Boolean = Item.isIllegalName(name)

  def asDirectory: Directory

  def asFile: File

  def getType: String
}

object Item {
  def isIllegalName(name: String): Boolean =
    name.contains(Directory.SEPARATOR) || name.contains(".")
}
