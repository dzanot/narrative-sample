package analytics

import analytics.{ QueryParamMatchers => QP }
import analytics.data._ 

import cats.effect._
import cats.implicits._
import org.http4s.{ EntityEncoder, HttpRoutes, QueryParamDecoder }
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.syntax._

import scala.concurrent.ExecutionContext.Implicits.global


class Service(database: Commands with Queries) {
  implicit def hourlySummaryEncoder = EntityEncoder.simple[IO, HourSummary]()(hs => {
   val r =  s"""unique_users,${hs.uniqueUsers}
               |clicks,${hs.clicks}
               |impressions,${hs.impressions}
               |""".stripMargin

    fs2.Chunk.bytes(r.getBytes)
  })


  val analytics = HttpRoutes.of[IO] {
    case GET -> Root / "hello" => 
      Ok("hello\n")
    case GET -> Root / "analytics" :? QP.Timestamp(timestamp) =>
      Ok(database.summaryForHour(timestamp))
    case POST -> Root / "analytics" :? QP.Timestamp(timestamp) +& QP.UserId(userId) +& QP.EventType(eventType) =>
      for {
        _ <- database.recordEvent(Event(timestamp, userId, eventType))
        r <- NoContent()
      } yield r
  }.orNotFound
}

object Server extends IOApp {

  val service = new Service(H2Database)
  def run(args: List[String]) = for {
    _ <- H2Database.bootstrap
    e <- BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(service.analytics)
           .serve
           .compile
           .drain
           .as(ExitCode.Success)
  } yield e
}
