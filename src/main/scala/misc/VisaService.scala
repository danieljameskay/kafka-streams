package misc

import models.Transaction

class VisaService {
  def toDb(k: Int, t: Transaction): Unit = {
    println(s"Record sent to DB, $t, $k")
  }
}
