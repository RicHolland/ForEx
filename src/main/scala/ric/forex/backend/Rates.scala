package ric.forex.backend

import scala.util.parsing.json._
import scala.io.Source
import scala.util.Try

class Rates(val rates: Map[String, Double]){

    def getRate(code: String): Option[Double] = {
        this.rates.get(code.toUpperCase)
    }

}

object Rates {

    //def apply(opt: Option[Any]): Rates = new Rates(opt)

    def apply(url: String) = {
        new Rates(JSON.parseFull(getJson(url)).flatMap{ x =>
            val tmp = x.asInstanceOf[Map[String, Any]]
            Console.err.println(
                "Date: " + tmp.get("date").getOrElse("1900-01-01")
           )

            tmp.get("rates")}.get.asInstanceOf[Map[String, Double]]
        )
    }

    def getJson(url: String) = {
        Try(Source.fromURL(url).mkString).
            getOrElse(Source.fromFile("/home/h7743735/rates.json").mkString)
    }

}
