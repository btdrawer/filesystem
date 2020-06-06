package filesystem.files

import filesystem.filesystem.FilesystemException

class Directory(override val parentPath: String, override val name: String, val contents: List[Item])
  extends Item(parentPath, name) {
  override def asFile: File =
    throw new FilesystemException("Cannot be converted to a file.")

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
    Directory.getDirectoriesFromPath(path.substring(1))

  def findDescendant(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findItem(path.head).asDirectory.findDescendant(path.tail)
  }
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  val GO_UP = ".."

  def createRoot: Directory = Directory.createEmpty("", "")

  def createEmpty(parentPath: String, name: String): Directory = new Directory(
    parentPath,
    name,
    List()
  )

  def getDirectoriesFromPath(path: String): List[String] = path
    .split(Directory.SEPARATOR)
    .toList
    .filter(directoryName => !directoryName.isEmpty)

  def updateStructure(currentDirectory: Directory, path: List[String], newItem: Item): Directory = {
    if (path.isEmpty) currentDirectory.addItem(newItem)
    else {
      val oldDirectory = currentDirectory.findItem(path.head).asDirectory
      currentDirectory.replaceItem(oldDirectory.name,
        updateStructure(oldDirectory, path.tail, newItem)
      )
    }
  }
}
