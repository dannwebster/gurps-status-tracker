package util

import java.awt.Color
import java.awt.Graphics2D

/**
 */
class Colors {
    companion object {
        val AMBER = Color(184, 134, 11)
        val GOLD = Color(255, 215,0)
        val CRIMSON = Color(220, 20, 6)
        val GREEN = Color(0, 128, 0)
        val DARK_GREEN = Color(0, 100, 0)
        val YELLOW = Color.YELLOW
        val ORANGE = Color.ORANGE
        val RED = Color.RED
        val BLACK = Color.BLACK
        val WHITE = Color.WHITE
        val L_PURPLE = Color(147, 112, 219)
        val PURPLE = Color(102,  51, 153)
        val INDIGO = Color(75, 0, 130)
        val VD_BLUE = Color(25,  25, 112)
        val D_BLUE = Color(0, 0, 128)
        val M_BLUE = Color(230, 230, 139)
        val L_BLUE = Color(0, 0, 205)
        val VL_BLUE = Color(65, 105, 225)
        val VVL_BLUE = Color(135, 206, 250)
    }
}

fun Graphics2D.r(x: Int, y: Int, w: Int, h: Int, fillColor: Color = Colors.WHITE, borderColor: Color = Colors.BLACK) {
    this.paint = fillColor
    this.fillRect(x, y, w, h)
    this.paint = borderColor
    this.drawRect(x, y, w, h)
}
