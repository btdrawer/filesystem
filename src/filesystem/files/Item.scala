package filesystem.files

abstract class Item(val parentPath: String, val name: String) {
  def path: String = s"$parentPath/$name"

  def asDirectory: Directory

  def getType: String
}
