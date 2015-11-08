package org.kenix.ibconnector

import java.util

import com.ib.controller.ApiConnection.ILogger
import com.ib.controller.Types.NewsType
import com.ib.controller.{Formats, ApiController}
import com.ib.controller.ApiController.{IBulletinHandler, ITimeHandler, IConnectionHandler}


object IbConnector {
  val INSTANCE: IbConnector = new IbConnector

  def main(args: Array[String]) = {
    INSTANCE.run
  }

  private class Logger extends ILogger {

    def log(msg: String) {
      print(msg)
    }
  }
}

class IbConnector extends IConnectionHandler {


  def run {
    m_controller.connect("127.0.0.1", 4001, 0)
  }

  // IConnectionHandler implementation
  private final val m_inLogger: IbConnector.Logger = new IbConnector.Logger()
  private final val m_outLogger: IbConnector.Logger = new IbConnector.Logger()
  private val m_controller: ApiController = new ApiController(this, m_inLogger, m_outLogger)
  private final val m_acctList: util.ArrayList[String] = new util.ArrayList[String]()

  override def show(str: String) {
    println(str)
  }

  override def error(e: Exception) {
    show(e.toString)
  }

  override def message(id: Int, errorCode: Int, errorMsg: String) {
    show( id + " " + errorCode + " " + errorMsg)
  }

  override def connected {
    show("connected")
    m_controller.reqCurrentTime(new ITimeHandler() {
      override def currentTime(time: Long) {
        show("Server date/time is " + Formats.fmtDate(time * 1000))
      }
    })
    m_controller.reqBulletins(true, new IBulletinHandler() {
      override def bulletin(msgId: Int, newsType: NewsType, message: String, exchange: String) {
        val str = String.format("Received bulletin:  type=%s  exchange=%s", newsType, exchange)
        println(str)
        println(message)
      }
    })
  }

  override def disconnected {
  }

  override def accountList(list: util.ArrayList[String]) {
    m_acctList.clear
    m_acctList.addAll(list)
  }
}
