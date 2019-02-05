package basic

import java.util.concurrent.TimeUnit
import java.util.{Locale, Properties}

import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object WordCount extends App {

  import org.apache.kafka.streams.scala.Serdes._
  import org.apache.kafka.streams.scala.ImplicitConversions._

  val conf = ConfigFactory.load()

  val props = new Properties
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("bootstrap.servers"))
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass)

  val builder = new StreamsBuilder()
  val source: KStream[String, String] = builder.stream[String, String](conf.getString("wordcount.source.topic"))

  val wordCounts: KTable[String, Long] = source
    .flatMapValues(v => v.toLowerCase(Locale.getDefault()).split("\\W+"))
    .groupBy((_, word) => word)
    .count()

  wordCounts.toStream.to(conf.getString("wordcount.sink.topic"))

  val streams = new KafkaStreams(builder.build(), props)

  streams.cleanUp()

  streams.start()

  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }

}
