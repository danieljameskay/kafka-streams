package producers

import java.time.LocalDateTime
import java.util.Properties

import models.Purchase
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import zmartapp.KafkaBytesSerializer

object PurchaseProducer extends App {

  val props = new Properties
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaBytesSerializer[Purchase]])

  val producer = new KafkaProducer[String, Purchase](props)

  val transaction = new Purchase(
    "CUST0067",
    "Danny",
    "Kay",
    "5647-0909-8305-9099",
    "beer3490",
    1,
    1.20,
    null,
    "YO83AY",
    "Wines & Spirits",
    "1AQWE",
    "10SELBY"
  )

  while(true) {
    transaction.purchaseDate = LocalDateTime.now().toString
    val data: ProducerRecord[String, Purchase] = new ProducerRecord("transactions", "", transaction)
    producer.send(data)
    println(s"Record sent: $transaction")
    Thread.sleep(5000)
  }

  producer.close()

}
