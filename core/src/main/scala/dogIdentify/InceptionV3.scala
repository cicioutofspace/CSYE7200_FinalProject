package dogIdentify

import java.nio.charset.Charset
import java.nio.file.{Files, Paths}

import scala.collection.JavaConverters._

class InceptionV3 (graphPath: String, labelMapPath: String) extends Model with Labels {
  def this(modelPath: String) = this(s"$modelPath/retrained_graph.pb", s"$modelPath/retrained_labels.txt")

  private val codeLabelSeq: Array[String] = {Files.readAllLines(Paths.get(labelMapPath), Charset.defaultCharset()).asScala.toArray
  }

  override def getBytes: Array[Byte] = Files.readAllBytes(Paths.get(graphPath))

  override def getLabelOf(tensor: Array[Float], limit: Int): Seq[Label] = {
    val all = tensor.zip(codeLabelSeq).map { case (score, label) =>
      Label(label, score)
    }
    all.sortBy(-_.score).take(limit).toSeq
  }
}
