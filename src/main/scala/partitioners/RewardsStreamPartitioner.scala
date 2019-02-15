package partitioners

import models.Transaction
import org.apache.kafka.streams.processor.StreamPartitioner

class RewardsStreamPartitioner extends StreamPartitioner[String, Transaction] {
  override def partition(topic: String, key: String, value: Transaction, numPartitions: Int): Integer = {
    value.accountNo.hashCode() % numPartitions
  }
}
