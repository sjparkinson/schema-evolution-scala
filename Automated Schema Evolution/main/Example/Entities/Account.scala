package Entities

import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.lifted.Column
import Example.Traits.Entity

case class Account(id: Option[Int], name: String, email: String, disabled: Boolean)

class AccountTable(tag: Tag) extends Table[Account](tag, "Account") with Entity
{
    def id = column[Int]("Id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("Name")

    def email = column[String]("Email")

    def disabled = column[Boolean]("Disabled", O.Nullable)

    def * = (id.?, name, email, disabled) <> (Account.tupled, Account.unapply)

    val columns: Seq[Column[_]] = Seq(id, name, email, disabled)
}
