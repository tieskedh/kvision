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
package pl.treksoft.kvision.core

import pl.treksoft.kvision.utils.asString
import pl.treksoft.kvision.utils.capitalsToHyphens
import kotlin.reflect.KProperty

/**
 * Base class for components supporting CSS styling.
 */
abstract class StyledComponent(val valueMap : ValueMap) : Component {

    /**
     * creates a delegate for refreshing when updated or changed.
     * @param initialValue the initial value of the field
     * @param onChangeOnly if set to true, updates on change, else update on every set
     * @param nulAllowed tells if the property is nullable. If not set, the value is based on the value of initialvalue
     *
     * @return
     */
    protected fun <T> refreshOnUpdate(initialValue: T, onChangeOnly: Boolean = false, nulAllowed: Boolean? = null) : ValueMapProvider {
        val nullable = nulAllowed ?: (initialValue == null)
        return when {
            onChangeOnly && nullable -> valueMap.addProperty(initialValue, UpdateNullAble)
            onChangeOnly && !nullable -> valueMap.addProperty(initialValue, UpdateNonNull)
            !onChangeOnly && nullable -> valueMap.addProperty(initialValue, RefreshNullable)
            !onChangeOnly && !nullable -> valueMap.addProperty(initialValue, RefreshNonNull)
            else -> throw NoWhenBranchMatchedException("impossible exception")
        }
    }

    val RefreshNullable = Details(true, { old, new -> refresh(); true })
    val RefreshNonNull = Details(false, { old, new -> requireNotNull(new); refresh(); true })
    val UpdateNonNull = Details(false, { old, new -> requireNotNull(new); if (old != new) refresh(); true })
    val UpdateNullAble = Details(true, { old, new -> if (old != new) refresh(); true })

    /**
     * Width of the current component.
     */
    open var width: CssSize? by refreshOnUpdate(null)
    /**
     * Minimal width of the current component.
     */
    var minWidth: CssSize? by refreshOnUpdate(null)
    /**
     * Maximal width of the current component.
     */
    var maxWidth: CssSize? by refreshOnUpdate(null)
    /**
     * Height of the current component.
     */
    open var height: CssSize? by refreshOnUpdate(null)
    /**
     * Height of the current component.
     * TODO:
     */
    var heightby: CssSize? by refreshOnUpdate(null)
    /**
     * Minimal height of the current component.
     */
    var minHeight: CssSize? by refreshOnUpdate(null)
    /**
     * Maximal height of the current component.
     */
    var maxHeight: CssSize? by refreshOnUpdate(null)
    /**
     * Border of the current component.
     */
    var border: Border? by refreshOnUpdate(null)
    /**
     * Top border of the current component.
     */
    var borderTop: Border? by refreshOnUpdate(null)
    /**
     * Right border of the current component.
     */
    var borderRight: Border? by refreshOnUpdate(null)
    /**
     * Bottom border of the current component.
     */
    var borderBottom: Border? by refreshOnUpdate(null)
    /**
     * Left border of the current component.
     */
    var borderLeft: Border? by refreshOnUpdate(null)
    /**
     * Margin of the current component.
     */
    var margin: CssSize? by refreshOnUpdate(null)
    /**
     * Top margin of the current component.
     */
    var marginTop: CssSize? by refreshOnUpdate(null)
    /**
     * Right margin of the current component.
     */
    var marginRight: CssSize? by refreshOnUpdate(null)
    /**
     * Bottom margin of the current component.
     */
    var marginBottom: CssSize? by refreshOnUpdate(null)
    /**
     * Left margin of the current component.
     */
    var marginLeft: CssSize? by refreshOnUpdate(null)
    /**
     * Padding of the current component.
     */
    var padding: CssSize? by refreshOnUpdate(null)
    /**
     * Top padding of the current component.
     */
    var paddingTop: CssSize? by refreshOnUpdate(null)
    /**
     * Right padding of the current component.
     */
    var paddingRight: CssSize? by refreshOnUpdate(null)
    /**
     * Bottom padding of the current component.
     */
    var paddingBottom: CssSize? by refreshOnUpdate(null)
    /**
     * Left padding of the current component.
     */
    var paddingLeft: CssSize? by refreshOnUpdate(null)
    /**
     * Text color for the current component.
     */
    var color: Color? by refreshOnUpdate(null)
    /**
     * Text color for the current component given in hex format (write only).
     *
     * This property gives a convenient way to set the value of [color] property e.g.:
     *
     * c.colorHex = 0x00ff00
     *
     * The value read from this property is always null.
     */
    var colorHex: Int?
        get() = null
        set(value) {
            color = if (value != null) Color(value) else null
        }
    /**
     * Text color for the current component given with named constant (write only).
     *
     * This property gives a convenient way to set the value of [color] property e.g.:
     *
     * c.colorName = COLOR.GREEN
     *
     * The value read from this property is always null.
     */
    var colorName: COLOR?
        get() = null
        set(value) {
            color = if (value != null) Color(value) else null
        }
    /**
     * Opacity of the current component.
     */
    var opacity : Double? by refreshOnUpdate(null)
    /**
     * Background of the current component.
     */
    var background: Background? by refreshOnUpdate(null)


    private var snStyleCache: List<StringPair>? = null

    /**
     * @suppress
     * Internal function
     * Re-renders the current component.
     * @return current component
     */
    open fun refresh(): StyledComponent {
        snStyleCache = null
        return this
    }

    internal fun getSnStyleInternal(): List<StringPair> {
        return snStyleCache ?: {
            val s = getSnStyle()
            snStyleCache = s
            s
        }()
    }

    /**
     * Returns the list of String pairs defining CSS style attributes and their values.
     * @return the list of attributes and their values
     */
//    @Suppress("ComplexMethod", "LongMethod")
    protected open fun getSnStyle(): List<StringPair> {
        return valueMap.getValues().map{ (k,v)->
            val value = if (v is Pair<*,*> && v.isCssSize()){
                (v as CssSize).asString()
            } else if (v is Border){
                v.asString()
            } else if (v is Color){
                v.asString()
            } else v.toString()

            k.capitalsToHyphens() to value
        }
    }

}

open class Details(val nullable: Boolean, val onSet: (old: Any?, new: Any?) -> Boolean)

interface ValueMapProvider{
    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>) : ValueMap
}
class ValueMap(
        private val details: MutableMap<String, Details> = mutableMapOf(),
        private val values: MutableMap<String, Any> = mutableMapOf()
) {

    fun getValues() = values.toMap()
    fun getValues(vararg props: String) = props.map { it to getValue(it) }
    private inner class PropertyAdding(private val initialValue: Any?, private val detail: Details) : ValueMapProvider{
        override operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>) = this@ValueMap.also {
            details[prop.name] = detail
            if (initialValue != null) values[prop.name] = initialValue
        }
    }

    fun getValue(name: String) = values[name]
    fun getDetails(name: String) = details[name]
    inline operator fun <reified T> getValue(thisRef: kotlin.Any?, property: kotlin.reflect.KProperty<*>): T {
        val value = getValue(property.name)
        if (value != null) return value as T

        val details = getDetails(property.name) ?: throw NoSuchElementException()
        if (details.nullable) return null as T

        throw UninitializedPropertyAccessException()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        val detail = details[property.name] ?: throw NoSuchElementException()
        val shouldChange = detail.onSet(values[property.name], value)
        if (shouldChange) {
            if (value == null) values.remove(property.name)
            else values[property.name] = value
        }
    }

    fun addProperty(initialValue: Any?, property: Details) : ValueMapProvider = PropertyAdding(initialValue, property)
}