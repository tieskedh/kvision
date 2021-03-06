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
package pl.treksoft.kvision.form

import pl.treksoft.kvision.core.StringPair
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag

/**
 * Helper class for HTML label element.
 *
 * @constructor
 * @param forId the value of *for* attribute
 * @param text the text of the label
 * @param rich determines if [text] can contain HTML code
 * @param classes a set of CSS class names
 */
open class FieldLabel(
    internal val forId: String, text: String? = null, rich: Boolean = false,
    classes: Set<String> = setOf("control-label")
) : Tag(
    TAG.LABEL,
    text, rich, classes = classes
) {

    override fun getSnAttrs(): List<StringPair> {
        return super.getSnAttrs() + ("for" to forId)
    }

}
