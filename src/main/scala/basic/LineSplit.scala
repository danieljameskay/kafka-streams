package basic

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.kstream.{KStream}
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}


object LineSplit extends App {

  import Serdes._

  val conf = ConfigFactory.load()

  val props = new Properties
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("bootstrap.servers"))
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "line-split")

  val builder = new StreamsBuilder()
  val source: KStream[String, String] = builder.stream[String, String](conf.getString("linesplit.source.topic"))

  val words = source
    .flatMapValues(v => v.split("\\W+"))
    .to(conf.getString("linesplit.sink.topic"))
  
  val stream = new KafkaStreams(builder.build(), props)
  stream.start()

  sys.ShutdownHookThread {
    stream.close(10, TimeUnit.SECONDS)
  }

}
