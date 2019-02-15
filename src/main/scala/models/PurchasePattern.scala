package models

case class PurchasePattern(var zipCode: String, var item: String, var date: String, var amount: Double) {
  override def toString = s"PurchasePattern($zipCode, $item, $date, $amount)"
}