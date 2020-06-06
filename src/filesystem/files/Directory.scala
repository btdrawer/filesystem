package filesystem.files

import filesystem.filesystem.FilesystemException

class Directory(override val parentPath: String, override val name: String, val contents: List[Item])
  extends Item(parentPath, name) {
  override def asFile: File =
    throw new FilesystemException("Cannot be converted to a file.")

  override def isDirectory: Boolean = true

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  def isRoot: Boolean = parentPath.isEmpty

  def addItem(item: Item): Directory =
    new Directory(parentPath, name, contents :+ item)

  def findItem(name: String): Option[Item] =
    contents.find(item => item.name == name)

  def replaceItem(itemName: String, newItem: Item): Directory =
    new Directory(parentPath, name,
      contents.filter(item => item.name != itemName) :+ newItem
    )

  def hasItem(name: String): Boolean =
    contents.exists(item => item.name == name)

  def getAllDirectoriesInPath: List[String] =
    Directory.getDirectoriesFromPath(path.substring(1))

  def findDescendant(path: List[String]): Option[Directory] = {
    if (path.isEmpty) Some(this)
    else findItem(path.head)
      .flatMap(directory =>
        directory.asDirectory.findDescendant(path.tail)
      )
  }
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  val CURRENT_DIRECTORY = "."
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

  def updateStructure(currentDirectory: Directory, path: List[String], newItem: Item): Option[Directory] = {
    if (path.isEmpty) Some(currentDirectory.addItem(newItem))
    else for {
      nextDirectory <- currentDirectory.findItem(path.head)
      updatedDirectory <- updateStructure(nextDirectory.asDirectory, path.tail, newItem)
    } yield currentDirectory.replaceItem(updatedDirectory.name, updatedDirectory)
  }
}
