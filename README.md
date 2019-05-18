# Analytics Api

## Endpoints 

```
POST /analytics?timestamp={millis_since_epoch}&user={user_id}&event={click|impression}
GET /analytics?timestamp={millis_since_epoch}
```

## Running

SBT is the only dependency. To start the server run `sbt run` from the projects root directory

This will start the server on `localhost:8080`

When you see a line like `[scala-execution-context-global-49] INFO  o.h.s.b.BlazeServerBuilder - http4s v0.20.1 on blaze v0.14.2 started at http://127.0.0.1:8080/` the server is ready to take traffic.


## Sample traffic
```
#in time frame
curl -X POST "localhost:8080/analytics?user_id=1&event=impression&timestamp=1558137029545" -v
curl -X POST "localhost:8080/analytics?user_id=1&event=click&timestamp=1558137030545" -v
curl -X POST "localhost:8080/analytics?user_id=2&event=impression&timestamp=1558137019545" -v
curl -X POST "localhost:8080/analytics?user_id=3&event=impression&timestamp=1558137039545" -v
#out of timeframe
curl -X POST "localhost:8080/analytics?user_id=4&event=impression&timestamp=1558133429544" -v
curl -X POST "localhost:8080/analytics?user_id=4&event=impression&timestamp=1558140629544" -v
#query time frame
curl "localhost:8080/analytics?timestamp=1558137029544" -v
```


