package ric.forex.gui

import scala.swing._
import scala.swing.event._
import scala.math.BigDecimal.RoundingMode
import ric.forex.backend.Rates

class UI extends MainFrame {
  val rates = Rates("http://api.fixer.io/latest?base=GBP")
  title = "GBP Currency Conversion"
  preferredSize = new Dimension(1300, 700)
  val currSelect = new ComboBox[String](rates.rates.keySet.toList.sorted)
  val convDir = new ComboBox[String](List("From GBP", "To GBP"))
  val currLine = new BoxPanel(Orientation.Horizontal) {
    contents += convDir
    contents += Swing.HStrut(10)
    contents += currSelect
  }

  val amount = new TextField
  val setAmount = new Button("Set Amount")
  val amountLine = new BoxPanel(Orientation.Horizontal) {
    contents += amount
    contents += Swing.HStrut(10)
    contents += setAmount
  }

  val results = new TextArea {
    rows = 1
    editable = false
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += currLine
    contents += Swing.VStrut(10)
    contents += amountLine
    contents += Swing.VStrut(10)
    contents += results
    border = Swing.EmptyBorder(20, 20, 20, 20)
  }

  listenTo(setAmount)

  reactions += {
    case ButtonClicked(`setAmount`) => eval()
  }

  def eval(){
    val fromGBP = convDir.selection.item match {
      case "From GBP" => true
      case "To GBP" => false
      case _ => throw new Exception("Incorrect conversion direction option.")
    }
    val rateCode = currSelect.selection.item
    if (fromGBP) {
      val num = BigDecimal(amount.text) * BigDecimal(rates.getRate(rateCode).getOrElse(0.0).toString)
      results.text = num.setScale(2, RoundingMode.HALF_UP).toString + " " + rateCode + "(" + rates.getSym(rateCode).getOrElse("") + ")"
    } else {
      val num2 = BigDecimal(amount.text) / BigDecimal(rates.getRate(rateCode).get.toString)
      results.text = num2.setScale(2, RoundingMode.HALF_UP).toString + " GBP(£)"
    }
  }
}
