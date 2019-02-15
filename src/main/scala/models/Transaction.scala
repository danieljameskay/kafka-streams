package models

import java.time.{LocalDate}

case class Transaction(var number: String, cardType: String, expiry: LocalDate, amount: Double, location: String, accountNo: Int) {

  private val CC_NUMBER_REPLACEMENT = "xxxx-xxxx-xxxx-"
  def maskCreditCard: Transaction = {
    val parts: Array[String] = this.number.split("-")
    if(parts.length < 4) this.number = "xxxx"
    else {
      val last4Digits: String = this.number.split("-")(3)
      number = CC_NUMBER_REPLACEMENT + last4Digits
    }
    this
  }
  override def toString = s"Transaction($number, $cardType, $expiry, $amount, $location, $accountNo)"
}
