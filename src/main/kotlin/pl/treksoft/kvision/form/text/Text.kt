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

import pl.treksoft.kvision.core.Container

/**
 * Form field text component.
 *
 * @constructor
 * @param type text input type (default "text")
 * @param value text input value
 * @param label label text bound to the input element
 * @param rich determines if [label] can contain HTML code
 */
open class Text(
    type: TEXTINPUTTYPE = TEXTINPUTTYPE.TEXT, value: String? = null,
    label: String? = null, rich: Boolean = false
) : AbstractText(label, rich) {

    /**
     * Text input type.
     */
    var type
        get() = input.type
        set(value) {
            input.type = value
        }
    /**
     * Determines if autocomplete is enabled for the input element.
     */
    var autocomplete
        get() = input.autocomplete
        set(value) {
            input.autocomplete = value
        }

    final override val input: TextInput = TextInput(type, value).apply { id = idc }

    init {
        @Suppress("LeakingThis")
        input.eventTarget = this
        this.addInternal(input)
        this.addInternal(validationInfo)
    }

    companion object {
        /**
         * DSL builder extension function.
         *
         * It takes the same parameters as the constructor of the built component.
         */
        fun Container.text(
            type: TEXTINPUTTYPE = TEXTINPUTTYPE.TEXT, value: String? = null,
            label: String? = null, rich: Boolean = false, init: (Text.() -> Unit)? = null
        ): Text {
            val text = Text(type, value, label, rich).apply { init?.invoke(this) }
            this.add(text)
            return text
        }
    }
}
