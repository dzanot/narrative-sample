package analytics

import data._

import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._


object QueryParamMatchers {
  object Timestamp extends QueryParamDecoderMatcher[Long]("timestamp")
  object UserId extends QueryParamDecoderMatcher[Int]("user_id")
  object EventType extends QueryParamDecoderMatcher[EventType]("event")

  implicit def eventTypeDecoder: QueryParamDecoder[EventType] = new QueryParamDecoder[EventType] {
    override def decode(value: QueryParameterValue) = value.value match {
      case "click" => Click.validNel//cats.data.Validated.Valid(Click)
      case "impression" => Impression.validNel //cats.data.Validated.Valid(Impression)
      case _ => org.http4s.ParseFailure("unsupported event type", "only 'click' and 'impression' are supported event types").invalidNel
    }
  }
}
