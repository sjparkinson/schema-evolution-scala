package Example.Traits

import scala.slick.lifted.Column


trait Entity
{
    def columns: Seq[Column[_]]
}
