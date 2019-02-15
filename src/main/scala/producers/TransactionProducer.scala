package producers

import java.time.{LocalDate, LocalDateTime}
import java.util.Properties

import models.{Transaction}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import zmartapp.KafkaBytesSerializer

object TransactionProducer extends App {

  val props = new Properties
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaBytesSerializer[Transaction]])

  val producer = new KafkaProducer[String, Transaction](props)

  val transaction1 = new Transaction(
    "5647-0909-8305-9099",
    "AMEX",
    LocalDate.of(2020, 1, 1),
    123.20,
    "12.234254353, -34.35644353",
    45671234)

  val transaction2 = new Transaction(
    "5647-2444-8305-8542",
    "MASTERCARD",
    LocalDate.of(2024, 2, 10),
    4.00,
    "12.234254353, -34.35644353",
    97872345)

  val transaction3 = new Transaction(
    "1256-0909-4578-0168",
    "MASTERCARD",
    LocalDate.of(2024, 5, 20),
    123.20,
    "12.234254353, -34.35644353",
    56749876)

  val transaction4 = new Transaction(
    "5647-8305-8305-3506",
    "VISA",
    LocalDate.of(2019, 12, 12),
    10.10,
    "12.234254353, -34.35644353",
    34561234)

  val transaction5 = new Transaction(
    "5647-0909-8305-0909",
    "SOLO",
    LocalDate.of(2020, 11, 12),
    3.50,
    "12.234254353, -34.35644353",
    12345678)

  val transaction6 = new Transaction(
    "5647-8305-8305-3506",
    "VISA",
    LocalDate.of(2019, 12, 12),
    203.40,
    "12.234254353, -34.35644353",
    34561234)

  val transaction7 = new Transaction(
    "5647-8305-8305-3506",
    "VISA",
    LocalDate.of(2019, 12, 12),
    500.00,
    "12.234254353, -34.35644353",
    34561234)

  val transactions = Array(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7)

  while(true) {
    val data: ProducerRecord[String, Transaction] = new ProducerRecord("transactions51", "", transactions(new scala.util.Random().nextInt(6)))
    producer.send(data)
    println(s"Record sent: $data")
    Thread.sleep(3000)
  }

  producer.close()

}
