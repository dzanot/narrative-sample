package analytics.data

import java.time.Instant
import java.time.temporal.ChronoUnit
import cats.effect.IO

sealed trait EventType

case object Click extends EventType

case object Impression extends EventType

case class Event(timestamp: Long, userId: Int, eventType: EventType)

case class HourSummary(uniqueUsers: Int, clicks: Int, impressions: Int)

trait Commands {
  def recordEvent(event: Event): IO[Int]
}

object Queries {
  def hourStartFromTimestamp(millis: Long): (Instant, Instant) = {
    val start = Instant.ofEpochMilli(millis).truncatedTo(ChronoUnit.HOURS)
    val end = start.plus(1, ChronoUnit.HOURS)
    (start, end)
  }
  def summaryReducer(rows: List[(Int, String)]): HourSummary = {
    val uniqueUsers = rows.map({ case (id, _) => id}).toSet.size
    val clicks = rows.filter({ case (_, e) => e == "click" }).size
    val impressions = rows.filter({ case (_, e) => e == "impression" }).size
    HourSummary(uniqueUsers = uniqueUsers, clicks = clicks, impressions = impressions)
  }
}
trait Queries {
  def summaryForHour(millis: Long): IO[HourSummary] = {
    val (start, end) = Queries.hourStartFromTimestamp(millis)
    summaryForRange(start, end)
  }

  def summaryForRange(start: Instant, end: Instant): IO[HourSummary] 
  
}
