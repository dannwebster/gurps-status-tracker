import settings.ProgramSettings
import util.Colors
import util.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


/**
 */
val HP_HEADER_COLOR = Colors.DARK_GREEN
val WP_HEADER_COLOR = Colors.VD_BLUE
val FP_HEADER_COLOR = Colors.INDIGO

val HP_COLORS = listOf(
        Colors.GREEN,
        Colors.AMBER,
        Colors.GOLD,
        Colors.YELLOW,
        Colors.ORANGE,
        Colors.RED,
        Colors.CRIMSON,
        Colors.BLACK,
        Colors.BLACK)

val FP_COLORS = listOf(
    Colors.L_PURPLE,
    Colors.PURPLE,
    Colors.INDIGO,
    Colors.BLACK
)

val WP_COLORS = listOf(
        Colors.VL_BLUE,
        Colors.L_BLUE,
        Colors.D_BLUE,
        Colors.BLACK
)

fun String.camelCase() = this.capitalize().replace("[^\\w]*".toRegex(), "")

class ImageVisualization(val statusTracker : StatusTracker) {


    companion object {
        val DPI = 300

        fun Double.inches() = (this * DPI).toInt()

        val PAGE_WIDTH_PX = 8.5.inches()
        val PAGE_HEIGHT_PX = 11.0.inches()

        val MARGIN_PX = 1.0.inches()
        val GUTTER_PX = 0.125.inches()

        val MAX_WIDTH_PX = PAGE_WIDTH_PX - ( 2 * MARGIN_PX )
        val MAX_HEIGHT_PX = PAGE_HEIGHT_PX - ( 2 * MARGIN_PX )

        val ROW_WIDTH_PX = MAX_WIDTH_PX - (2 * GUTTER_PX)
        val ROW_HEIGHT_PX = 0.30.inches()

        val FONT_PX = 0.1.inches()
        val TEXT_V_GUTTER = (0.1 * FONT_PX).toInt()
    }

    fun exportImage(programSettings: ProgramSettings) {
        val bi = construct(statusTracker)
        writeImage(bi, programSettings)
    }

    fun construct(statusTracker: StatusTracker) : BufferedImage {
        val bi = BufferedImage(MAX_WIDTH_PX, MAX_HEIGHT_PX, BufferedImage.TYPE_INT_RGB)
        val g = bi.createGraphics()
        var startY = GUTTER_PX
        g.paint = Color.white
        g.fillRect(0, 0, MAX_WIDTH_PX, MAX_HEIGHT_PX)
        startY = drawHeaders(startY, g, statusTracker) + GUTTER_PX
        startY = drawTrack(startY, g, statusTracker.hpTrack, HP_HEADER_COLOR, HP_COLORS) + GUTTER_PX
        startY = drawTrack(startY, g, statusTracker.fpTrack, FP_HEADER_COLOR, FP_COLORS) + GUTTER_PX
        startY = drawTrack(startY, g, statusTracker.wpTrack, WP_HEADER_COLOR, WP_COLORS) + GUTTER_PX
        return bi
    }

    fun drawNamesBar(startY: Int, g: Graphics2D, statusTracker: StatusTracker): Int {
        var y = startY
        var h = ROW_HEIGHT_PX
        var w = ROW_WIDTH_PX
        var textW = w/2
        var textH = h
        var text1X = GUTTER_PX
        var text2X = GUTTER_PX + w / 2

        g.r(GUTTER_PX, startY, ROW_WIDTH_PX, ROW_HEIGHT_PX)
        val headerStyle = g
                .font(fontSize = FONT_PX, bold = true)
                .align(hGutter = GUTTER_PX, vGutter = 0, hAlign = HAlign.CENTER, vAlign = VAlign.MIDDLE)
        val textBounds = headerStyle.bounds(textW, textH)

        textBounds.pos(text1X, y).write("Character Name: ${statusTracker.characterName}")
        textBounds.pos(text2X, y).write("Player Name: ${statusTracker.characterSettings.playerName}")

        y += ROW_HEIGHT_PX

        return y
    }

    fun drawStatsBar(startY: Int, g: Graphics2D, statusTracker: StatusTracker): Int {
        var y = startY

        return y
    }

    fun drawHeaders(startY: Int, g: Graphics2D, statusTracker: StatusTracker): Int {
        var y = startY
        y = drawNamesBar(y, g, statusTracker)
        y = drawStatsBar(y, g, statusTracker)
        return y
    }


    fun drawTrack(startY: Int, g: Graphics2D, track: Track, headerColor: Color, colors: List<Color>): Int {
        val x = GUTTER_PX
        var y = startY
        val w = ROW_WIDTH_PX
        val h = ROW_HEIGHT_PX
        val fontSize = FONT_PX
        val col0Width = (0.3 * w).toInt()
        val remainingColWidth = w - col0Width
        g.r(x, y, w, h, headerColor, Colors.BLACK)

        val headerStyle = g.font(Colors.WHITE, fontSize = fontSize, bold = true)
                .align(hGutter = GUTTER_PX, vGutter = 0, hAlign = HAlign.LEFT, vAlign = VAlign.MIDDLE)

        val textStyle = g.font(Colors.WHITE, fontSize = fontSize, bold = false)
                .align(hGutter = GUTTER_PX, vGutter = TEXT_V_GUTTER, hAlign = HAlign.LEFT, vAlign = VAlign.TOP)

        val info = listOf(track.headers.drop(1)) + track.rows.map { row -> row.info }
        val headerHBounds = listOf(col0Width) + headerStyle.textDistributions(info, remainingColWidth)

        drawBorders(g, x, y, headerHBounds, h, headerColor, Color.BLACK)
        track.headers.forEachIndexed { index, header ->
            headerStyle.distributeTexts(track.headers, x, y, headerHBounds, h)
        }
        y += h
        track.rows.forEachIndexed { index, trackRow ->
            val trackRowInfo = listOf("") + trackRow.info
            val fillColor = colors.getOrElse(index) { i -> Colors.BLACK }
            drawBorders(g, x, y, headerHBounds, h, fillColor, Color.BLACK)
            textStyle.distributeTexts(trackRowInfo, x, y, headerHBounds, h)
            y += h
        }
        return y
    }

    fun drawBubbles(g: Graphics2D, startX: Int, startY: Int, range: IntProgression) {
        val diameter = 10
        range.forEach {
            val y = startY + (diameter * 2)
            g.background = Colors.WHITE
            g.stroke = Colors.B
            g.fillOval(startX, y, diameter, diameter)
        }

    }

    fun drawBorders(g: Graphics2D, x0: Int, y0: Int, headerHBounds: List<Int>, h: Int, fillColor: Color, borderColor: Color) {
        var boundX = x0
        for (hBound in headerHBounds) {
            g.r(boundX, y0, hBound, h, fillColor, borderColor)
            boundX += hBound
        }
    }

    fun writeImage(bi: BufferedImage, programSettings: ProgramSettings) {
        var imgFile = getValidFile(programSettings)
        ImageIO.write(bi, "png", imgFile)
    }


    fun getValidFile(programSettings: ProgramSettings): File {
        val imgFile = programSettings.imageOutputFile ?: File.createTempFile(statusTracker.characterName.camelCase(), ".png")
        if (imgFile.exists()) {
            if (!imgFile.canWrite()) throw IllegalArgumentException("Image Output File '${imgFile.canonicalPath}' is not writable")
        }
        println("Outputting image to '${imgFile.canonicalPath}'")
        return imgFile
    }
}