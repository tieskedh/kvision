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
package pl.treksoft.kvision.dropdown

import com.github.snabbdom.VNode
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.html.*
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.utils.obj
import kotlin.coroutines.experimental.EmptyCoroutineContext.get

/**
 * Useful options for use in DropDown's *elements* parameter.
 */
enum class DD(val option: String) {
    HEADER("DD#HEADER"),
    DISABLED("DD#DISABLED"),
    SEPARATOR("DD#SEPARATOR")
}

/**
 * Bootstrap dropdown component.
 *
 * @constructor
 * @param text the label of the dropdown button
 * @param elements an optional list of link elements (special options from [DD] enum class can be used as values)
 * @param icon the icon of the dropdown button
 * @param style the style of the dropdown button
 * @param disabled determines if the component is disabled on start
 * @param classes a set of CSS class names
 */
open class DropDown(
    text: String, elements: List<StringPair>? = null, icon: String? = null,
    style: BUTTONSTYLE = BUTTONSTYLE.DEFAULT, disabled: Boolean = false,
    classes: Set<String> = setOf(), valueMap: ValueMap
) : SimplePanel(classes, valueMap) {
    private val idc = "kv_dropdown_" + counter

    internal val button: DropDownButton = DropDownButton(
            idc, text, icon, style,
            disabled, setOf("dropdown"), ValueMap()
    )
    /**
     * Label of the dropdown button.
     */
    var text : String by button.valueMap

    private var elements = elements
        set(value) {
            field = value
            setChildrenFromElements()
        }
    /**
     * The icon of the dropdown button.
     */
    var icon : String? by button.valueMap

    /**
     * The style of the dropdown button.
     */
    var style : BUTTONSTYLE by button.valueMap

    /**
     * The size of the dropdown button.
     */
    var size : BUTTONSIZE by button.valueMap

    /**
     * Determines if the dropdown button takes all the space horizontally.
     */
    var block : Boolean by button.valueMap

    /**
     * Determines if the dropdown is disabled.
     */
    var disabled : Boolean by button.valueMap
    /**
     * The image on the dropdown button.
     */
    var image : ResString? by button.valueMap
    /**
     * Determines if the dropdown is showing upwards.
     */
    var dropup : Boolean by refreshOnUpdate(false)
    /**
     * Width of the dropdown button.
     */
    override var width: CssSize?
        get() = super.width
        set(value) {
            super.width = value
            button.width = value
        }


    internal val list: DropDownListTag = DropDownListTag(idc, setOf("dropdown-menu"))

    init {
        setChildrenFromElements()
        this.addInternal(button)
        this.addInternal(list)
        counter++
    }

    companion object {
        internal var counter = 0
    }

    override fun add(child: Component): SimplePanel {
        list.add(child)
        return this
    }

    override fun addAll(children: List<Component>): SimplePanel {
        list.addAll(children)
        return this
    }

    override fun remove(child: Component): SimplePanel {
        list.remove(child)
        return this
    }

    override fun removeAll(): SimplePanel {
        list.removeAll()
        return this
    }

    override fun getChildren(): List<Component> {
        return list.getChildren()
    }


    private fun setChildrenFromElements() {
        list.removeAll()
        elements?.let { elems ->
            val c = elems.map {
                when (it.second) {
                    DD.HEADER.option -> Tag(TAG.LI, it.first, classes = setOf("dropdown-header"))
                    DD.SEPARATOR.option -> {
                        val tag = Tag(TAG.LI, it.first, classes = setOf("divider"))
                        tag.role = "separator"
                        tag
                    }
                    DD.DISABLED.option -> {
                        val tag = Tag(TAG.LI, classes = setOf("disabled"))
                        tag.add(Link(it.first, "#"))
                        tag
                    }
                    else -> Link(it.first, it.second)
                }
            }
            list.addAll(c)
        }
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        this.getElementJQuery()?.on("show.bs.dropdown", { e, _ ->
            this.dispatchEvent("showBsDropdown", obj { detail = e })
        })
        this.getElementJQuery()?.on("shown.bs.dropdown", { e, _ ->
            this.dispatchEvent("shownBsDropdown", obj { detail = e })
        })
        this.getElementJQuery()?.on("hide.bs.dropdown", { e, _ ->
            this.dispatchEvent("hideBsDropdown", obj { detail = e })
        })
        this.getElementJQuery()?.on("hidden.bs.dropdown", { e, _ ->
            this.dispatchEvent("hiddenBsDropdown", obj { detail = e })
        })
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        if (dropup)
            cl.add("dropup" to true)
        else
            cl.add("dropdown" to true)
        return cl
    }

    /**
     * Toggles dropdown visibility.
     */
    open fun toggle() {
        this.list.getElementJQueryD()?.dropdown("toggle")
    }
}

internal class DropDownButton(
    id: String, text: String, icon: String? = null, style: BUTTONSTYLE = BUTTONSTYLE.DEFAULT,
    disabled: Boolean = false, classes: Set<String> = setOf(), valueMap: ValueMap
) :
    Button(text, icon, style, disabled, classes, valueMap) {

    init {
        this.id = id
    }

    override fun getSnAttrs(): List<StringPair> {
        return super.getSnAttrs() + listOf(
            "data-toggle" to "dropdown", "aria-haspopup" to "true",
            "aria-expanded" to "false"
        )
    }
}

internal class DropDownListTag(private val ariaId: String, classes: Set<String> = setOf()) : ListTag(
    LISTTYPE.UL, null,
    false, classes
) {

    override fun getSnAttrs(): List<StringPair> {
        return super.getSnAttrs() + listOf("aria-labelledby" to ariaId)
    }
}
