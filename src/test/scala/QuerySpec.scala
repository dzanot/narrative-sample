package analytics

import data._
import java.time.Instant
import org.scalatest.FlatSpec 

class QuerySpec extends FlatSpec {

  "hourStartFromTimestamp" should "give the right hour time range for an arbitrary timestamp" in {
     val timestamp = Instant.parse("2019-05-17T22:50:29.545Z")
     val expectedStart = Instant.parse("2019-05-17T22:00:00.000Z")
     val expectedEnd = Instant.parse("2019-05-17T23:00:00.000Z")

     val timestampMillis = timestamp.toEpochMilli
    
     val (actualStart, actualEnd) = Queries.hourStartFromTimestamp(timestampMillis)

     assert(actualStart == expectedStart) 
     assert(actualEnd == expectedEnd)
  }
  "summaryReducer" should "correctly count unique users, clicks, and impressions" in {
    val rows = List((1, "impression")
                   ,(2, "impression")
                   ,(1, "click")
                   ,(3, "impression")
                   ,(3, "click")
                   ) 
    val expected = HourSummary(uniqueUsers = 3, clicks = 2, impressions = 3)
    val actual = Queries.summaryReducer(rows)
    assert(actual == expected)
  }
}
