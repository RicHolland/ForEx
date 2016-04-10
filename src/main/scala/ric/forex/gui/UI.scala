package ric.forex.gui

import scala.swing._
import scala.swing.event._
import ric.forex.backend.Rates

class UI extends MainFrame {
  val rates = Rates("http://api.fixer.io/latest?base=GBP")
  title = "GUI Program #1"
  preferredSize = new Dimension(1300, 700)
  val currSelect = new ComboBox[String](rates.rates.keySet.toList.sorted)
  val setCurr = new Button("Set Currency")
  val currLine = new BoxPanel(Orientation.Horizontal) {
    contents += currSelect
    contents += Swing.HStrut(10)
    contents += setCurr
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

  listenTo(setCurr)
  listenTo(setAmount)

  reactions += {
    case ButtonClicked(`setCurr`) => eval()
    case ButtonClicked(`setAmount`) => eval()
  }

  def eval(){
    val rateCode = currSelect.selection.item
    val num = (amount.text.toDouble) * (rates.getRate(rateCode).getOrElse(0.0))
    results.text = num.toString + " " + rateCode
  }
}
