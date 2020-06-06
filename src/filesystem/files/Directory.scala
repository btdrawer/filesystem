package filesystem.files

class Directory(override val parentPath: String, override val name: String, val contents: List[Item])
  extends Item(parentPath, name) {
  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  def addItem(item: Item): Directory =
    new Directory(parentPath, name, contents :+ item)

  def findItem(name: String): Item =
    contents.filter(item => item.name == name).head

  def replaceItem(itemName: String, newItem: Item): Directory =
    new Directory(parentPath, name,
      contents.filter(item => item.name != itemName) :+ newItem
    )

  def hasItem(name: String): Boolean =
    contents.exists(item => item.name == name)

  def getAllDirectoriesInPath: List[String] =
    path
      .substring(1)
      .split(Directory.SEPARATOR)
      .toList
      .filter(directoryName => !directoryName.isEmpty)

  def findDescendant(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findItem(path.head).asDirectory.findDescendant(path.tail)
  }
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def createEmpty(parentPath: String, name: String): Directory = new Directory(
    parentPath,
    name,
    List()
  )

  def createRoot: Directory = Directory.createEmpty("", "")
}
