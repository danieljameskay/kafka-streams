package basic

import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig, Topology}

object Pipe {

  def main(args: Array[String]): Unit = {
    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe")
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)

    val builder = new StreamsBuilder

    builder.stream("streams-plaintext-input").to("streams-plaintext-output")

    val topology = builder.build()

    println(topology.describe())

    val streams = new KafkaStreams(topology, config)

    streams.start()

  }

}