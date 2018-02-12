/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pl.treksoft.kvision.panel

import pl.treksoft.kvision.core.Component
import pl.treksoft.kvision.core.WidgetWrapper
import pl.treksoft.kvision.html.ALIGN
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag

/**
 * Bootstrap grid sizes.
 */
enum class GRIDSIZE(internal val size: String) {
    XS("xs"),
    SM("sm"),
    MD("md"),
    LG("lg")
}

internal const val MAX_COLUMNS = 12

internal data class WidgetParam(val widget: Component, val size: Int, val offset: Int)

/**
 * The container with support for Bootstrap responsive grid layout.
 *
 * @constructor
 * @param gridsize grid size
 * @param rows number of rows
 * @param cols number of columns
 * @param align text align of grid cells
 * @param classes a set of CSS class names
 */
open class ResponsiveGridPanel(
        private val gridsize: GRIDSIZE = GRIDSIZE.MD,
        private var rows: Int = 0, private var cols: Int = 0, align: ALIGN? = null,
        classes: Set<String> = setOf()
) : SimplePanel(classes) {

    /**
     * Text align of grid cells.
     */
    var align = align
        set(value) {
            field = value
            refreshRowContainers()
        }

    internal val map = mutableMapOf<Int, MutableMap<Int, WidgetParam>>()
    private var auto: Boolean = true

    /**
     * Adds child component to the grid.
     * @param child child component
     * @param col column number
     * @param row row number
     * @param size cell size (colspan)
     * @param offset cell offset
     * @return this container
     */
    open fun add(child: Component, col: Int, row: Int, size: Int = 0, offset: Int = 0): ResponsiveGridPanel {
        val cRow = maxOf(row, 0)
        val cCol = maxOf(col, 0)
        if (row > rows - 1) rows = cRow + 1
        if (col > cols - 1) cols = cCol + 1
        map.getOrPut(cRow, { mutableMapOf() })[cCol] = WidgetParam(child, size, offset)
        if (size > 0 || offset > 0) auto = false
        refreshRowContainers()
        return this
    }

    override fun add(child: Component): ResponsiveGridPanel {
        return this.add(child, this.cols, 0)
    }

    override fun addAll(children: List<Component>): ResponsiveGridPanel {
        children.forEach { this.add(it) }
        return this
    }

    @Suppress("NestedBlockDepth")
    override fun remove(child: Component): ResponsiveGridPanel {
        map.values.forEach { row ->
            row.filterValues { it.widget == child }
                    .forEach { (i, _) -> row.remove(i) }
        }
        refreshRowContainers()
        return this
    }

    /**
     * Removes child component at given location (column, row).
     * @param col column number
     * @param row row number
     * @return this container
     */
    open fun removeAt(col: Int, row: Int): ResponsiveGridPanel {
        map[row]?.remove(col)
        refreshRowContainers()
        return this
    }

    @Suppress("ComplexMethod", "NestedBlockDepth")
    private fun refreshRowContainers() {
        singleRender {
            clearRowContainers()
            val num = MAX_COLUMNS / cols

            for (i in 0 until rows) {
                val rowContainer = SimplePanel(setOf("row"))
                val row = map[i]
                if (row != null) {
                    (0 until cols).map { row[it] }.forEach { wp ->
                        if (auto) {
                            val widget = wp?.widget?.let {
                                WidgetWrapper(it, setOf("col-" + gridsize.size + "-" + num))
                            } ?: Tag(TAG.DIV, classes = setOf("col-" + gridsize.size + "-" + num))
                            align?.let {
                                widget.addCssClass(it.className)
                            }
                            rowContainer.add(widget)
                        } else {
                            if (wp != null) {
                                val s = if (wp.size > 0) wp.size else num
                                val widget = WidgetWrapper(wp.widget, setOf("col-" + gridsize.size + "-" + s))
                                if (wp.offset > 0) {
                                    widget.addCssClass("col-" + gridsize.size + "-offset-" + wp.offset)
                                }
                                align?.let {
                                    widget.addCssClass(it.className)
                                }
                                rowContainer.add(widget)
                            }
                        }
                    }
                }
                addInternal(rowContainer)
            }
        }
    }

    private fun clearRowContainers() {
        children.forEach { it.dispose() }
        removeAll()
    }
}
