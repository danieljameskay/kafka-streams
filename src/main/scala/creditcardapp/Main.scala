package creditcardapp

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory
import misc.VisaService
import models.{RewardAccumulator, Transaction}
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, Produced}
import org.apache.kafka.streams.state.Stores
import partitioners.RewardsStreamPartitioner
import transformers.PurchaseRewardTransformer
import zmartapp.StreamsSerde

object Main extends App {
  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.Serdes._

  val conf = ConfigFactory.load()

  val streamPartitioner = new RewardsStreamPartitioner()

  val stringSerde: Serde[String] = Serdes.String()
  implicit val transactionSerde: Serde[Transaction] = new StreamsSerde().transactionSerde()
  implicit val rewardsSerde: Serde[RewardAccumulator] = new StreamsSerde().rewardAccumulatorSerde()
  implicit val rewardPartitioner = Produced.`with`(streamPartitioner)(stringSerde, transactionSerde)

  val props = new Properties()
  props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("bootstrap.servers"))
  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, conf.getString("transactions.application.id"))

  val builder = new StreamsBuilder()

  // Source node and processing node stream the records from the topic and maks the credit card number.
  val transactionsKStream: KStream[String, Transaction] = builder.stream[String, Transaction]("transactions51")
    .mapValues(t => t.maskCreditCard)



  val rewardsPointsStore = "rewardsPointsStore"



  val storeSupplier = Stores.inMemoryKeyValueStore(rewardsPointsStore)

  val storeBuilder = Stores.keyValueStoreBuilder(storeSupplier, Serdes.Integer(), Serdes.Integer())

  builder.addStateStore(storeBuilder)

  val transByCustomerStream = transactionsKStream.through("customer.transactions")(rewardPartitioner)

  val statefulrewardAccumulator: KStream[String, RewardAccumulator] = transByCustomerStream.transformValues(() => new PurchaseRewardTransformer(rewardsPointsStore),rewardsPointsStore)

  statefulrewardAccumulator.print(Printed.toSysOut[String, RewardAccumulator].withLabel("rewards"))





  // we can print the transactionsKStream to the stdout.
  transactionsKStream.print(Printed.toSysOut[String, Transaction].withLabel("Transaction"))

  // we then send the new stream of records to the purchases sink.
  transactionsKStream.to("purchases")


  // here we are filtering out when the amount is over 100.00 and the type is a Mastercard.
  // The filter function takes a predicate, which is a function which returns a Boolean
  val filteredTransactionKStream = transactionsKStream
    .filter((_, t) => t.amount > 100.00).selectKey((_,t) => t.accountNo)
    .branch((_, t) => t.cardType.equals("MASTERCARD"),(_, t) => t.cardType.equals("VISA"))

  filteredTransactionKStream(0).print(Printed.toSysOut[Int, Transaction].withLabel("Mastercard Alert"))
  filteredTransactionKStream(1).print(Printed.toSysOut[Int, Transaction].withLabel("Visa Alert"))

  filteredTransactionKStream(1).foreach((k, t) => new VisaService().toDb(k, t))

  val streams = new KafkaStreams(builder.build(), props)

  streams.cleanUp()

  streams.start()

  sys.ShutdownHookThread {
    streams.close(10, TimeUnit.SECONDS)
  }
}
