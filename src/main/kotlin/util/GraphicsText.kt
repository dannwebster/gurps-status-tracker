package util

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

/**
 */

data class FontSettings(val g: Graphics2D,
                        val textColor: Color,
                        val fontSize: Int,
                        val italics: Boolean,
                        val bold: Boolean,
                        val fontName: String) {

    val style = Font.PLAIN or
            if (italics) Font.ITALIC else Font.PLAIN or
            if (bold) Font.BOLD else Font.PLAIN

    val font = Font(fontName, style, fontSize)

    val metrics = g.getFontMetrics(font)

    fun textW(text: String) : Int = text.split("\n").map{ line -> metrics.stringWidth(line) }.max() ?: 0

    fun textH(text: String) : Int = text.split("\n").map{ line -> lineH(line) }.sum()

    fun lineH(line: String) : Int = metrics.getStringBounds(line, g).height.toInt()

    fun pos(x: Int, y: Int) = PositionSettings(this, x, y, null)

    fun align(hGutter: Int = 0, vGutter: Int = 0, hAlign: HAlign = HAlign.CENTER, vAlign: VAlign= VAlign.MIDDLE) =
            AlignSettings(this, hGutter, vGutter, hAlign, vAlign)
}

data class PositionSettings(val fontSettings: FontSettings, val x0: Int, val y0: Int, val boundsSettings: BoundsSettings?) {
    fun write(text: String) = TextSettings(text, this, boundsSettings).write()

}

enum class HAlign {
    CENTER, LEFT, RIGHT
}

enum class VAlign {
    MIDDLE, TOP, BOTTOM
}

data class AlignSettings(val fontSettings: FontSettings,
                         val hGutter: Int, val vGutter: Int,
                         val hAlign: HAlign, val vAlign: VAlign) {
    val g = fontSettings.g

    fun bounds(w: Int, h: Int) = BoundsSettings(w, h, this)

    fun <T> List<List<T>>.transpose() = this[0].mapIndexed { index, _ ->
            this.map { row -> row[index] }
    }

    fun textDistributions(texts: List<List<String>>, maxWidth: Int): List<Int> =
        rowTextDistributions(
            texts.map { textRow -> textRow.zip(rowTextDistributions(textRow, maxWidth)) }
             .transpose()
             .map { col -> col.maxBy { (text, width) -> width } }
             .filterNotNull()
             .map { (text, width) -> text}, maxWidth
        )

    fun rowTextDistributions(texts: List<String>, maxWidth: Int): List<Int> {
        val textWidths = texts.map { fontSettings.textW(it) }
        val totalWidth = textWidths.sum()
        val usableWidth = maxWidth
        val hBoundWidths = textWidths.map { textWidth -> (1.0 * textWidth * usableWidth) / (1.0 * totalWidth) }.map { it.toInt() }
        return hBoundWidths
    }

    fun distributeTexts(texts: List<String>, x0: Int, y0: Int, hBoundWidths: List<Int>, height: Int) {
        var x = x0
        val textsToHBounds = hBoundWidths.zip(texts)
        textsToHBounds.forEach { (hBound, text) ->
            this.bounds(hBound, height).pos(x, y0).write(text)
            x += hBound
        }
    }
}

data class BoundsSettings(val w: Int, val h: Int, val alignSettings: AlignSettings) {


    fun x(x0: Int, text: String) = with(alignSettings) {
        when (hAlign) {
            HAlign.LEFT -> x0 + hGutter
            HAlign.CENTER -> x0 + w / 2 - fontSettings.textW(text) / 2
            HAlign.RIGHT -> x0 - hGutter - fontSettings.textW(text)
        }
    }


    fun y(y0: Int, text: String) = with(alignSettings) {
        when(vAlign){
            VAlign.TOP -> y0 + vGutter + fontSettings.lineH(text)
            VAlign.MIDDLE -> y0 + h/2 + fontSettings.textH(text)/2
            VAlign.BOTTOM -> y0 + h - vGutter
        }
    }

    fun pos(x: Int, y: Int) = PositionSettings(alignSettings.fontSettings, x, y, this)
}

data class TextSettings(val text: String,
                        val positionSettings: PositionSettings,
                        val boundsSetting: BoundsSettings?) {

    val x = if (boundsSetting == null) positionSettings.x0 else boundsSetting.x(positionSettings.x0, text)
    val y = if (boundsSetting == null) positionSettings.y0 else boundsSetting.y(positionSettings.y0, text)
    val g = positionSettings.fontSettings.g

    fun write() {
        val lines = text.split("\n").map { it.trim() }
        g.setFont(positionSettings.fontSettings.font)
        g.paint = positionSettings.fontSettings.textColor
        var lineY = y
        for (line in lines) {
            g.drawString(line, x, lineY)
            lineY += positionSettings.fontSettings.lineH(line)
        }
    }
}

fun Graphics2D.font(color: Color = Colors.BLACK,
                    fontSize: Int = 10,
                    italics: Boolean = false,
                    bold: Boolean = false,
                    fontName: String = "TimesRoman") = FontSettings(this, color, fontSize, italics, bold, fontName)


