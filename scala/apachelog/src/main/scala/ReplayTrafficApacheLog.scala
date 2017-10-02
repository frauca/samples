import com.typesafe.scalalogging._
import com.alvinalexander.accesslogparser._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import scala.collection.immutable.ListMap

object ReplayTrafficApacheLog extends App{

  val p = new AccessLogParser
  val logger = Logger("ReplayTrafficApacheLog")
  val conf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("apacheLogReplay")
  val sc: SparkContext = new SparkContext(conf)



  logger.info("Spark context already started")

  val log = sc.textFile("c:/tmp/access.log")

  logger.info(log.count()+" are going to be read")

  // works: use the previous example to get to a series of "(URI, COUNT)" pairs; (MapReduce like)
  val uriCount = log.map(p.parseRecordReturningNullObjectOnFailure(_).request)
    .filter(request => request != "")  // filter out records that wouldn't parse properly
    .map(_.split(" ")(1))              // get the uri field
    .map(uri => (uri, 1))              // create a tuple for each record
    .reduceByKey((a, b) => a + b)      // reduce to get this for each record: (/java/java_oo/up.png,2)
    .collect                           // convert to Array[(String, Int)], which is Array[(URI, numOccurrences)]

  val uriHitCount = ListMap(uriCount.toSeq.sortWith(_._2 > _._2):_*)    // (/bar, 10), (/foo, 3), (/baz, 1)
  logger.info("show result"+uriHitCount.size)
  uriHitCount.take(20).foreach(println)

  sc.stop()
}
