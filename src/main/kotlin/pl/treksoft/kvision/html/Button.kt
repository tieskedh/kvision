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
package pl.treksoft.kvision.html

import com.github.snabbdom.VNode
import org.w3c.dom.events.MouseEvent
import pl.treksoft.kvision.core.*

/**
 * Button styles.
 */
enum class BUTTONSTYLE(internal val className: String) {
    DEFAULT("btn-default"),
    PRIMARY("btn-primary"),
    SUCCESS("btn-success"),
    INFO("btn-info"),
    WARNING("btn-warning"),
    DANGER("btn-danger"),
    LINK("btn-link")
}

/**
 * Button sizes.
 */
enum class BUTTONSIZE(internal val className: String) {
    LARGE("btn-lg"),
    SMALL("btn-sm"),
    XSMALL("btn-xs")
}

/**
 * Button component.
 *
 * @constructor
 * @param text button label
 * @param icon button icon
 * @param style button style
 * @param disabled button state
 * @param classes a set of CSS class names
 */
open class Button(
    text: String, icon: String? = null, style: BUTTONSTYLE = BUTTONSTYLE.DEFAULT,
    disabled: Boolean = false, classes: Set<String> = setOf(),
    valueMap: ValueMap
) : Widget(classes, valueMap) {

    /**
     * Button label.
     */
    var text : String by refreshOnUpdate(text)
    /**
     * Button icon.
     */
    var icon : String? by refreshOnUpdate(icon)
    /**
     * Button style.
     */
    var style : BUTTONSTYLE by refreshOnUpdate(style)
    /**
     * Determines if button is disabled.
     */
    var disabled : Boolean by refreshOnUpdate(disabled)
    /**
     * Button image.
     */
    var image: ResString? by refreshOnUpdate(null)
    /**
     * Button size.
     */
    var size: BUTTONSIZE? by refreshOnUpdate(null)
    /**
     * Determines if the button takes all the space horizontally.
     */
    var block : Boolean by refreshOnUpdate(false)

    override fun render(): VNode {
        val t = createLabelWithIcon(text, icon, image)
        return render("button", t)
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        cl.add("btn" to true)
        cl.add(style.className to true)
        size?.let {
            cl.add(it.className to true)
        }
        if (block) {
            cl.add("btn-block" to true)
        }
        if (disabled) {
            cl.add("disabled" to true)
        }
        return cl
    }

    override fun getSnAttrs(): List<StringPair> {
        return super.getSnAttrs() + ("type" to "button")
    }

    /**
     * A convenient helper for easy setting onClick event handler.
     */
    open fun onClick(handler: Button.(MouseEvent) -> Unit): Button {
        this.setEventListener<Button> {
            click = { e ->
                self.handler(e)
            }
        }
        return this
    }
}
