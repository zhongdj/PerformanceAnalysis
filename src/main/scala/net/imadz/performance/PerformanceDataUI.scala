package net.imadz.performance

import scala.io.Source
import scala.swing._
import scalaz.std.string._
import scala.collection.immutable.List
import net.imadz.performance.metadata.SourceMetadata
import net.imadz.performance.graph.PGraph
import net.imadz.performance.metadata.DiskIO
import scala.swing.Component._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import javax.swing.JTree

/**
 * Created by geek on 14-8-8.
 */
object PerformanceDataUI extends SimpleSwingApplication {

  //  val sourceFile1 = "report/20140815032932/Query_against_Binary_UUID/network.log.updated"
  //  val sourceFile2 = "report/20140815033054/Query_against_AutoIncremental_PK/network.log.updated"
  //  val sourceFile3 = "report/20140815033442/Query_against_Hex_PK/network.log.updated"
  val sourceFile1 = "report/20140815050213/Query_against_Binary_UUID/io.log.updated"
  val sourceFile2 = "report/20140815050435/Query_against_AutoIncremental_PK/io.log.updated"
  //val sourceFile3 = "report/20140815044217/Query_against_Hex_PK/io.log.updated"

  //  val sourceFile1 = "report/20140815032932/Query_against_Binary_UUID/io.log.updated"
  //  val sourceFile2 = "report/20140815033054/Query_against_AutoIncremental_PK/io.log.updated"
  //  val sourceFile3 = "report/20140815033442/Query_against_Hex_PK/io.log.updated"

  val intialSources = sourceFile1 :: sourceFile2 :: Nil //sourceFile3 :: Nil
  //  val intialSources = sourceFile1 :: sourceFile2 :: sourceFile3 :: Nil

  var __sources = intialSources

  def sources = __sources

  def sources_=(files: List[String]) {
    __sources = files
  }

  def header = Source.fromFile(sources.head).getLines().take(1).toList.head.split("\t")

  def validcolumns = header.drop(1)

  def validcolumnpairs = 1 to header.length zip validcolumns

  def perfData(col: Int): List[(String, List[Double])] = {

    val data = sources map { source =>
      Source.fromFile(source).getLines().drop(1).map { line =>
        val split: Array[String] = line.split("\t")
        if (split.length - 1 < col) 0.0D
        else {
          val d = parseDouble(split(col)).toOption
          if (d.isDefined) d.get
          else 0.0D
        }
      }.toList
    }

    sources zip data

  }

  def sourceMetadata(dimensionName: String) = new SourceMetadata(DiskIO(), dimensionName)

  val tree = new JTree

  def top = new MainFrame {

    private val gridPanel: GridPanel = new GridPanel(validcolumns.length, 1) {
      validcolumnpairs foreach (
        x => contents += wrap(new PGraph(sourceMetadata(x._2), perfData(x._1)))
        )
    }

    val scrollPanel = new scala.swing.ScrollPane(gridPanel)

    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("FileAction") {})
        contents += new MenuItem(Action("Exit") {System.exit(0)})
      }
    }

    contents = new SplitPane(Orientation.Vertical, wrap(tree), scrollPanel)


    size = java.awt.Toolkit.getDefaultToolkit.getScreenSize
    visible = true

    val bi = new BufferedImage(gridPanel.size.getWidth.toInt, gridPanel.size.getHeight.toInt, BufferedImage.TYPE_INT_ARGB);
    val g2d = bi.createGraphics();
    gridPanel.paint(g2d);
    ImageIO.write(bi, "PNG", new File("frame.png"));


  }


}
