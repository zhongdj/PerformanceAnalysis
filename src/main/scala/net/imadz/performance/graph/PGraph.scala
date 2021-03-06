package net.imadz.performance.graph

import javax.swing.JComponent
import java.awt.{Font, Color, Graphics, Dimension}
import net.imadz.performance.metadata.SourceMetadata
import java.text.NumberFormat
import java.io.File

/**
 * Created by geek on 14-8-8.
 */
class PGraph(val sourceMetadata: SourceMetadata, val perfDataSources: List[(String, List[Double])]) extends JComponent {

  val minWidth = 400
  val minHeight = 300

  setPreferredSize(new Dimension(minWidth, minHeight))
  val margin = 60
  val yScaleCount = 10
  val xScaleCount = 10
  val leftMargin: Int = 1.5 * margin toInt

  lazy val xScaleMax : Int = perfDataSources.map(_._2.length).foldLeft(0)((acc, x) => Math.max(acc,x))

  lazy val screenSize = getSize(null)

  lazy val imageWidth = screenSize.getWidth() - margin - leftMargin;

  lazy val imageHeight = screenSize.getHeight() - 2 * margin;

  lazy val yBottomLine = imageHeight + margin toInt

  lazy val yMaxSampleDataSetValue = perfDataSources.map(x => x._2).flatten.foldLeft(0D)((acc, x) => Math.max(acc, x))
  lazy val xMaxSampleDataSetLength = xScaleMax
  lazy val heightFactor = imageHeight.toDouble / yMaxSampleDataSetValue
  lazy val widthFactor = imageWidth.toDouble / xMaxSampleDataSetLength

  val colors = Array(Color.RED, Color.BLUE, Color.ORANGE, Color.BLACK, Color.GREEN,
                     Color.DARK_GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                     Color.PINK, Color.YELLOW)

  def coordernatesOf(source: List[Double]) = {
    val xs: Array[Int] = 1 to source.length map (x => leftMargin + x * widthFactor toInt) toArray
    val ys: Array[Int] = source map (y => yBottomLine - y * heightFactor toInt) toArray

    (xs, ys)
  }

  override def paintComponent(g: Graphics): Unit = {
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, getWidth, getHeight)
    g.setColor(Color.BLACK)
    g.drawRect(1, 1, getWidth - 2, getHeight - 2)
    drawAxis(g)

    var streamIndex = 0;
    perfDataSources foreach { streamSource =>
      g.setColor(colors(streamIndex))
      val (xs, ys) = coordernatesOf(streamSource._2)
      g.drawPolyline(xs, ys, streamSource._2.length)
      drawLabel(g, streamIndex, identity(streamSource._1))
      streamIndex += 1
    }

    drawTitle(g)
  }

  private def identity(filePath: String): String = {
    val file = new File(filePath)
    file.getParentFile.getName + " / " + file.getName.split("\\.")(0)
  }

  def drawTitle(g: Graphics) {
    g.setColor(Color.DARK_GRAY)
    g.setFont(new Font("default", Font.BOLD, 16));
    g.drawString(sourceMetadata.aspect.getClass.getSimpleName + "--" + sourceMetadata.dimensionName, (imageWidth / 2).toInt, (1 * imageHeight / 5).toInt)
  }

  def drawLabel(g: Graphics, streamIndex: Int, streamSourceName: String) {
    val streamSourceNameX: Double = imageWidth - 100
    val streamSourceNameY: Double = imageHeight - 100 + streamIndex * 10

    val shortLineY: Double = streamSourceNameY  - 2
    val shortLineX1: Double = streamSourceNameX - 20
    val shortLineX2: Double = streamSourceNameX - 10
    g.drawLine(shortLineX1.toInt, shortLineY.toInt, shortLineX2.toInt, shortLineY.toInt)
    g.drawString(streamSourceName, streamSourceNameX.toInt, streamSourceNameY.toInt)
  }

  lazy val yScalePixel = (0 until yScaleCount).toList.map { y => imageHeight - y * imageHeight / yScaleCount toInt} toArray

  lazy val xScalePixel = (0 until xScaleCount).toList.map { x => x * imageWidth / xScaleCount} toArray
  lazy val yInterval = yMaxSampleDataSetValue / yScaleCount
  lazy val totalSeconds = xMaxSampleDataSetLength
  lazy val xInterval = totalSeconds.toDouble/ xScaleCount.toDouble

  private def drawAxis(g: Graphics): Unit = {

    //draw x, y axis
    g.setColor(Color.BLACK)
    g.drawLine(leftMargin - 1, margin, leftMargin - 1, yBottomLine + 1 toInt)
    g.drawLine(leftMargin - 1, yBottomLine + 1 toInt, screenSize.getWidth - margin toInt, yBottomLine + 1 toInt)

    val fixedGap = 5
    val font = new Font(Font.SANS_SERIF, Font.PLAIN, 14)
    g.setFont(font)
    val fm = getFontMetrics(font)

    0 to yScaleCount foreach { y =>
      val yScaleValue: Double = y * yInterval
      val format: NumberFormat = NumberFormat.getInstance()
      format.setMaximumFractionDigits(2)
      format.setMinimumFractionDigits(2)
      val chars = format.format(yScaleValue).replaceAll(",", "").toCharArray
      var yScaleWidth = fm.charsWidth(chars, 0, chars.length)

      var fontSize = 14
      while (yScaleWidth + 3 >= margin && fontSize > 0) {
        fontSize -= 1
        val font2 = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize)
        val fm2 = getFontMetrics(font2)
        yScaleWidth = fm2.charsWidth(chars, 0, chars.length)
        g.setFont(font2)
      }

      val yScaleX = leftMargin - fixedGap - 1 - yScaleWidth
      val yScaleY = (yBottomLine - y * imageHeight / yScaleCount) toInt

      g.setColor(Color.BLACK)
      g.drawChars(chars, 0, chars.length, yScaleX, yScaleY)
      g.drawLine(leftMargin - 3, yScaleY, leftMargin, yScaleY)
    }

    g.setFont(font)

    0 to xScaleCount foreach { x =>
      val xScaleValue: Int = x * xInterval toInt
      val chars = String.valueOf(xScaleValue).toCharArray
      val xScaleHeight = fm.getHeight
      val xScaleX = leftMargin + x * imageWidth / xScaleCount toInt
      val xScaleY = yBottomLine + fixedGap + 1 + xScaleHeight toInt

      g.setColor(Color.BLACK)
      g.drawChars(chars, 0, chars.length, xScaleX, xScaleY)
      g.drawLine(xScaleX, yBottomLine, xScaleX, yBottomLine + 3)
    }
  }

}
