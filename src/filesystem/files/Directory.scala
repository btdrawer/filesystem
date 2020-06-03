package filesystem.files

class Directory(override val parentPath: String, override val name: String, val contents: List[Item])
  extends Item(parentPath, name)

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def addItemToDirectory(directory: Directory, item: Item): Directory = new Directory(
    directory.parentPath,
    directory.name,
    directory.contents :+ item
  )
  def createEmpty(parentPath: String, name: String): Directory = new Directory(
    parentPath,
    name,
    List()
  )
  def createRoot: Directory = Directory.createEmpty("", "")
}
