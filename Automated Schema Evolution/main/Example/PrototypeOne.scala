package PrototypeOne


import _root_.Entities.AccountTable
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.driver.SQLiteDriver
import scala.slick.jdbc.meta
import scala.slick.migration.api.{SQLiteDialect, TableMigration}
import scala.slick.model
import Example.Utilities._


object PrototypeOne extends App
{
    val dbUri = "jdbc:sqlite:SlickEvolutionExample.db"

    Database.forURL(dbUri, driver = "org.sqlite.JDBC") withSession
        {
            implicit session =>

                // The conceptual model of the table we are working with.
                val accountConceptualModel = TableQuery[AccountTable].baseTableRow

                // A list of the persistent data models.
                val dataBaseModels = meta.createModel(meta.MTable.getTables.list, SQLiteDriver.profile)

                // First check the conceptual model doesn't already exist in the persistent data model.
                if (dataBaseModels.tables.map(t => t.name.table).contains(accountConceptualModel.tableName))
                {
                    // The persistent model of the table we are working with.
                    val persistentDataModel = dataBaseModels.tables.find(t => t.name.table == accountConceptualModel.tableName).get

                    // Create two lists of the columns in the two models so they can be compared.
                    val persistentColumns: Seq[model.Column] = persistentDataModel.columns

                    val conceptualColumns: Seq[ColumnInfo] = accountConceptualModel.columns.map(c => columnInfo(fieldSym(c)))

                    // Compare the two lists and work out what needs adding, if any.
                    val columnsToAdd = conceptualColumns.map(c => c.name)
                        .diff(persistentColumns.map(c => c.name))
                        .map(name => conceptualColumns.filter(col => col.name == name).head)

                    implicit val dialect = new SQLiteDialect

                    // Create a new persistent table migration.
                    val migration = TableMigration(TableQuery[AccountTable])

                    // Add any missing columns to the migration.
                    columnsToAdd.foreach(col =>
                    {
                        migration.addColumns(_.columns.find(c => fieldSym(c).name == col.name).get)

                        println("Adding new column: %s".format(col.name))
                    })

                    // Complete the migration.
                    migration()

                    println("Completed migration.")
                }
                else
                {
                    TableQuery[AccountTable].ddl.create

                    println("Created new table: %s".format(accountConceptualModel.tableName))
                }
        }
}