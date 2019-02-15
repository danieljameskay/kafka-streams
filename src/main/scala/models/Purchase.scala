package models

case class Purchase(var customerId: String, var firstName: String, var lastName: String, var creditCardNumber: String, var itemPurchased: String, var quantity: Int, var price: Double, var purchaseDate: String, var zipCode: String, var department: String, var employeeId: String, var storeId: String) {
  private val CC_NUMBER_REPLACEMENT = "xxxx-xxxx-xxxx-"

  def maskCreditCard: Purchase = {
    val parts: Array[String] = this.creditCardNumber.split("-")
    if(parts.length < 4) this.creditCardNumber = "xxxx"
    else {
      val last4Digits: String = this.creditCardNumber.split("-")(3)
      creditCardNumber = CC_NUMBER_REPLACEMENT + last4Digits
    }
    this
  }

}

