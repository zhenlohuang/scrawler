package net.yidooo.scrawler.model

case class Item(fields: Map[String, Any]) {
  def get(key: String): Option[Any] = {
    fields.get(key)
  }

  def getOrElse(key: String, defaultValue: Any): Any = {
    fields.getOrElse(key, defaultValue)
  }
}
