package models

case class RewardAccumulator(purchaseTotal: Double, accountNo: Int) {

  var totalRewardPoints: Int = purchaseTotal.toInt

  def addRewardPoints(previousAmountOfPoints: Int) = {
    totalRewardPoints += previousAmountOfPoints
    totalRewardPoints
  }

  override def toString = s"RewardAccumulator($purchaseTotal, $totalRewardPoints, $accountNo)"

}
