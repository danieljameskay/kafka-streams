package zmartapp

import models.{PurchasePattern, RewardAccumulator, Transaction}
import org.apache.kafka.common.serialization.{Serde, Serdes}

class StreamsSerde {
  def rewardAccumulatorSerde(): Serde[RewardAccumulator] = Serdes.serdeFrom(new KafkaBytesSerializer[RewardAccumulator], new KafkaBytesDeserializer[RewardAccumulator])
  def transactionSerde(): Serde[Transaction] = Serdes.serdeFrom(new KafkaBytesSerializer[Transaction], new KafkaBytesDeserializer[Transaction])
  def purchasePatternsSerde(): Serde[PurchasePattern] = Serdes.serdeFrom(new KafkaBytesSerializer[PurchasePattern], new KafkaBytesDeserializer[PurchasePattern])
}