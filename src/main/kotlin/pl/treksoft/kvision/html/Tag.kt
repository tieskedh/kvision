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
import pl.treksoft.kvision.KVManager
import pl.treksoft.kvision.core.StringBoolPair
import pl.treksoft.kvision.panel.SimplePanel

/**
 * HTML tags.
 */
@Suppress("EnumNaming")
enum class TAG(internal val tagName: String) {
    H1("h1"),
    H2("h2"),
    H3("h3"),
    H4("h4"),
    H5("h5"),
    H6("h6"),
    P("p"),
    ABBR("abbr"),
    ADDRESS("address"),
    BLOCKQUOTE("blockquote"),
    SECTION("section"),
    HEADER("header"),
    FOOTER("footer"),
    PRE("pre"),
    UL("ul"),
    OL("ol"),
    DIV("div"),
    LABEL("label"),

    MARK("mark"),
    DEL("del"),
    S("s"),
    INS("ins"),
    U("u"),
    SMALL("small"),
    STRONG("strong"),
    EM("em"),
    CITE("cite"),
    CODE("code"),
    KBD("kbd"),
    VAR("var"),
    SAMP("samp"),
    SPAN("span"),
    LI("li")
}

/**
 * CSS align attributes.
 */
enum class ALIGN(val className: String) {
    LEFT("text-left"),
    CENTER("text-center"),
    RIGHT("text-right"),
    JUSTIFY("text-justify"),
    NOWRAP("text-nowrap")
}

/**
 * HTML tag component.
 *
 * @constructor
 * @param type tag type
 * @param text text content of the tag
 * @param rich determines if [text] can contain HTML code
 * @param align text align
 * @param classes a set of CSS class names
 */
open class Tag(
    type: TAG, text: String? = null, rich: Boolean = false, align: ALIGN? = null,
    classes: Set<String> = setOf()
) : SimplePanel(classes) {

    /**
     * Tag type.
     */
    var type by refreshOnUpdate(type)
    /**
     * Text content of the tag.
     */
    var text by refreshOnUpdate(text)
    /**
     * Determines if [text] can contain HTML code.
     */
    var rich by refreshOnUpdate(rich)
    /**
     * Text align.
     */
    var align by refreshOnUpdate(align)

    override fun render(): VNode {
        return if (text != null) {
            if (rich) {
                render(type.tagName, arrayOf(KVManager.virtualize("<span>$text</span>")) + childrenVNodes())
            } else {
                render(type.tagName, childrenVNodes() + arrayOf(text))
            }
        } else {
            render(type.tagName, childrenVNodes())
        }
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        align?.let {
            cl.add(it.className to true)
        }
        return cl
    }
}
