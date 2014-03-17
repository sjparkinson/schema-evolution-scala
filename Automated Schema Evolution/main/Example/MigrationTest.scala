package Example

import _root_.Entities._
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.migration.api.{TableMigration, SQLiteDialect}
import Example.Utilities._

object MigrationTest extends App
{
    implicit val dialect = new SQLiteDialect

    val dbUri = "jdbc:sqlite:SlickEvolutionExample.db"

    val accountMigration = TableMigration(TableQuery[AccountTable]).addColumns(_.columns.find(c => fieldSym(c).name == "Disabled").get)

    Database.forURL(dbUri, driver = "org.sqlite.JDBC") withSession
        {
            implicit session: Session =>

                accountMigration()
        }
}
