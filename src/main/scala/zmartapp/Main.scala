//package zmartapp
//
//import java.util.Properties
//import java.util.concurrent.TimeUnit
//
//import com.typesafe.config.ConfigFactory
//import models.{Purchase, PurchasePattern, RewardAccumulator}
//import org.apache.kafka.common.serialization.{Serde, Serdes}
//import org.apache.kafka.streams.kstream.Printed
//import org.apache.kafka.streams.scala.StreamsBuilder
//import org.apache.kafka.streams.scala.kstream.KStream
//import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
//
//object Main extends App {
//
//  import org.apache.kafka.streams.scala.ImplicitConversions._
//  import org.apache.kafka.streams.scala.Serdes._
//
//  val conf = ConfigFactory.load()
//
//  val stringSerde: Serde[String] = Serdes.String()
//  //implicit val purchaseSerde: Serde[Purchase] = new StreamsSerde().purchaseSerde()
//  implicit val patternsSerde: Serde[PurchasePattern] = new StreamsSerde().purchasePatternsSerde()
//  implicit val rewardAccumulatorSerde: Serde[RewardAccumulator] = new StreamsSerde().rewardAccumulatorSerde()
//
//  val props = new Properties()
//  props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("bootstrap.servers"))
//  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, conf.getString("zmart.application.id"))
//
//  val builder = new StreamsBuilder()
//
////  val purchaseKStream: KStream[String, Purchase] = builder.stream[String, Purchase]("transactions").mapValues(p => p.maskCreditCard)
////  purchaseKStream.print(Printed.toSysOut[String, Purchase].withLabel("Purchase"))
////  purchaseKStream.to("purchases")
//
//  val patternKStream = purchaseKStream.mapValues(p => new PurchasePattern(p.zipCode, p.itemPurchased, p.purchaseDate, p.price))
//  patternKStream.print(Printed.toSysOut[String, PurchasePattern].withLabel("PurchasePattern"))
//  patternKStream.to("patterns")
//
//  val rewardsKStream = purchaseKStream.mapValues(p => new RewardAccumulator().accumulate(p))
//  rewardsKStream.print(Printed.toSysOut[String, RewardAccumulator].withLabel("RewardAccumulator"))
//  rewardsKStream.to("rewards")
//
//  val streams = new KafkaStreams(builder.build(), props)
//
//  streams.cleanUp()
//
//  streams.start()
//
//  sys.ShutdownHookThread {
//    streams.close(10, TimeUnit.SECONDS)
//  }
//
//}
