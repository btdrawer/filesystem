package filesystem.files

class Directory(override val parentPath: String, override val name: String, val contents: List[Item])
  extends Item(parentPath, name)

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def createEmpty(parentPath: String, name: String): Directory = new Directory(
    parentPath,
    name,
    List()
  )

  def createRoot: Directory = Directory.createEmpty("", "")

  def addEmpty(parentDirectory: Directory, name: String): Directory =
    Directory.createEmpty(
      s"${parentDirectory.parentPath}/${parentDirectory.name}",
      name
    )
}
