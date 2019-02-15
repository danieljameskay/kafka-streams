package transformers

import models.{RewardAccumulator, Transaction}
import org.apache.kafka.streams.kstream.ValueTransformer
import org.apache.kafka.streams.processor.{ProcessorContext}
import org.apache.kafka.streams.state.KeyValueStore

class PurchaseRewardTransformer(private val storeName: String) extends ValueTransformer[Transaction, RewardAccumulator]{

  private var stateStore: KeyValueStore[Int, Int] = null

  override def init(context: ProcessorContext): Unit = {
    stateStore = context.getStateStore(storeName).asInstanceOf[KeyValueStore[Int, Int]]
  }

  override def transform(t: Transaction): RewardAccumulator = {
    val ra = new RewardAccumulator(t.amount, t.accountNo)
    val accumulatedSoFar = stateStore.get(ra.accountNo)

    if(accumulatedSoFar != 0) {
      ra.addRewardPoints(accumulatedSoFar)
    }

    stateStore.put(ra.accountNo, ra.totalRewardPoints)
    ra
  }

  override def close(): Unit = ???
}
