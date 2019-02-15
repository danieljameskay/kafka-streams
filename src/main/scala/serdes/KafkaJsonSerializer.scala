//package serdes
//
//import java.util
//
//import io.circe._
//import io.circe.generic.semiauto._
//import io.circe.syntax._
//import org.apache.kafka.common.serialization.Serializer
//
//class KafkaJsonSerializer[Transaction] extends Serializer[Transaction] {
//
//  implicit val encodeTransaction: Encoder[Transaction] = deriveEncoder[Transaction]
//
//  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = 0
//
//  override def serialize(topic: String, data: Transaction): Array[Byte] = {
//    data.asJson(encodeTransaction).toString().getBytes
//  }
//
//  override def close(): Unit = 0
//}
