package analytics

import java.time.Instant
import analytics.data._
import cats._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._

import scala.concurrent.ExecutionContext

object H2Database extends Commands with Queries {
  implicit val cs = IO.contextShift(ExecutionContext.global)
  val tx = Transactor.fromDriverManager[IO]("org.h2.Driver", "jdbc:h2:mem:analytics;DB_CLOSE_DELAY=-1")
   
  def eventTypeToDatabseRep(et: EventType): String = et match {
    case Click => "click"
    case Impression => "impression"
  }

  def bootstrap: IO[Int] = {
    sql"""CREATE TABLE event_types (
              id INT PRIMARY KEY,
              name VARCHAR(255) NOT NULL UNIQUE
          );

          INSERT into event_types (id, name)
              VALUES
              (1, 'click'),
              (2, 'impression');

          CREATE TABLE events (
            timestamp TIMESTAMP NOT NULL,
            user_id INT NOT NULL, 
            type INT NOT NULL,
            FOREIGN KEY(type) REFERENCES event_types(id)
          );
          """.update.run.transact(tx)
  }

  override def recordEvent(event: Event): IO[Int] = {
    sql"""INSERT INTO events (timestamp, user_id, type) 
          SELECT ${java.time.Instant.ofEpochMilli(event.timestamp)}, ${event.userId}, id 
          FROM event_types 
          WHERE name = ${eventTypeToDatabseRep(event.eventType)}"""
      .update
      .run
      .transact(tx)
  }

  override def summaryForRange(start: Instant, end: Instant): IO[HourSummary] = {
    val r = sql"""SELECT e.user_id, et.name 
                  FROM events AS e 
                  INNER JOIN event_types AS et 
                  ON e.type = et.id
                  WHERE e.timestamp >= ${start} AND e.timestamp < ${end}"""
      .query[(Int, String)]
      .to[List]
      .transact(tx)

    r.map(Queries.summaryReducer)
  }
}
