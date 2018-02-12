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
package pl.treksoft.kvision.form.text

import com.github.snabbdom.VNode
import pl.treksoft.kvision.core.StringBoolPair
import pl.treksoft.kvision.core.StringPair
import pl.treksoft.kvision.core.Widget
import pl.treksoft.kvision.form.INPUTSIZE

/**
 * Base class for basic text components.
 *
 * @constructor
 * @param value text input value
 * @param classes a set of CSS class names
 */
abstract class AbstractTextInput(
    value: String? = null,
    classes: Set<String> = setOf()
) : Widget(classes) {

    init {
        this.setInternalEventListener<AbstractTextInput> {
            input = {
                self.changeValue()
            }
        }
    }

    /**
     * Text input value.
     */
    var value: String? by refreshOnUpdate(value)
    /**
     * The value attribute of the generated HTML input element.
     *
     * This value is placed directly in generated HTML code, while the [value] property is dynamically
     * bound to the text input value.
     */
    var startValue: String? = value
        set(value) {
            field = value
            this.value = value
            refresh()
        }
    /**
     * The placeholder for the text input.
     */
    var placeholder: String?by refreshOnUpdate(null)
    /**
     * The name attribute of the generated HTML input element.
     */
    var name: String? by refreshOnUpdate(null)
    /**
     * Maximal length of the text input value.
     */
    var maxlength: Int? by refreshOnUpdate(null)
    /**
     * Determines if the field is disabled.
     */
    var disabled: Boolean by refreshOnUpdate(false)
    /**
     * Determines if the text input is automatically focused.
     */
    var autofocus: Boolean? by refreshOnUpdate(null)
    /**
     * Determines if the text input is read-only.
     */
    var readonly: Boolean? by refreshOnUpdate(null)
    /**
     * The size of the input.
     */
    var size: INPUTSIZE? by refreshOnUpdate(null)

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        size?.let {
            cl.add(it.className to true)
        }
        return cl
    }

    override fun getSnAttrs(): List<StringPair> {
        val sn = super.getSnAttrs().toMutableList()
        placeholder?.let {
            sn.add("placeholder" to it)
        }
        name?.let {
            sn.add("name" to it)
        }
        autofocus?.let {
            if (it) {
                sn.add("autofocus" to "autofocus")
            }
        }
        maxlength?.let {
            sn.add("maxlength" to ("" + it))
        }
        readonly?.let {
            if (it) {
                sn.add("readonly" to "readonly")
            }
        }
        if (disabled) {
            sn.add("disabled" to "true")
        }
        return sn
    }

    override fun afterInsert(node: VNode) {
        refreshState()
    }

    /**
     * @suppress
     * Internal function
     */
    protected open fun refreshState() {
        value?.let {
            getElementJQuery()?.`val`(it)
        } ?: getElementJQueryD()?.`val`(null)
    }

    /**
     * @suppress
     * Internal function
     */
    protected open fun changeValue() {
        val v = getElementJQuery()?.`val`() as String?
        if (v != null && v.isNotEmpty()) {
            this.value = v
        } else {
            this.value = null
        }
    }

    /**
     * Makes the input element focused.
     */
    open fun focus() {
        getElementJQuery()?.focus()
    }

    /**
     * Makes the input element blur.
     */
    open fun blur() {
        getElementJQuery()?.blur()
    }
}
