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
package test.pl.treksoft.kvision.html

import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.html.LISTTYPE
import pl.treksoft.kvision.html.ListTag
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import test.pl.treksoft.kvision.DomSpec
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class ListSpec : DomSpec {

    @Test
    fun renderElements() {
        run {
            val root = Root("test")
            val list = ListTag(LISTTYPE.DL_HORIZ, listOf("a1", "a2", "b1", "b2"))
            root.add(list)
            val element = document.getElementById("test")
            assertEquals(
                "<dl class=\"dl-horizontal\"><dt>a1</dt><dd>a2</dd><dt>b1</dt><dd>b2</dd></dl>",
                element?.innerHTML,
                "Should render correct html list"
            )
        }
    }

    @Test
    fun renderAsContainer() {
        run {
            val root = Root("test")
            val list = ListTag(LISTTYPE.UL)
            list.add(Tag(TAG.PRE, "pre"))
            list.add(Tag(TAG.DEL, "del"))
            root.add(list)
            val element = document.getElementById("test")
            assertEquals(
                "<ul><li><pre>pre</pre></li><li><del>del</del></li></ul>",
                element?.innerHTML,
                "Should render correct html list"
            )
        }
    }

}