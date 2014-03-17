package Example

import scala.slick.ast.{Select, FieldSymbol, Node, TableNode}
import scala.slick.lifted.Column
import scala.slick.driver.JdbcDriver

object Utilities
{
    // Utility classes and methods that work with Slick nodes.

    case class TableInfo(schemaName: Option[String], tableName: String)

    case class ColumnInfo(name: String)

    def tableInfo(table: TableNode): TableInfo = TableInfo(table.schemaName, table.tableName)

    def fieldSym(node: Node): Option[FieldSymbol] = node match
    {
        case Select(_, f: FieldSymbol) => Some(f)
        case _ => None
    }

    def fieldSym(column: Column[_]): FieldSymbol =
    {
        fieldSym(column.toNode) getOrElse sys.error("Invalid column: " + column)
    }

    def columnInfo(column: FieldSymbol): ColumnInfo =
    {
        ColumnInfo(column.name)
    }
}
