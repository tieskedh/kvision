package pl.treksoft.kvision.form

import com.github.snabbdom.VNode
import pl.treksoft.kvision.core.Widget
import pl.treksoft.kvision.snabbdom.StringBoolPair
import pl.treksoft.kvision.snabbdom.StringPair

enum class CHECKINPUTTYPE(val type: String) {
    CHECKBOX("checkbox"),
    RADIO("radio")
}

open class CheckInput(type: CHECKINPUTTYPE = CHECKINPUTTYPE.CHECKBOX, override var value: Boolean = false,
                      name: String? = null, disabled: Boolean = false, id: String? = null,
                      extraValue: String? = null,
                      classes: Set<String> = setOf()) : Widget(classes), BoolFormField {

    init {
        this.id = id
        this.setInternalEventListener {
            click = {
                val v = getElementJQuery()?.prop("checked") as Boolean?
                value = (v == true)
            }
            change = {
                val v = getElementJQuery()?.prop("checked") as Boolean?
                value = (v == true)
            }
        }
    }

    @Suppress("LeakingThis")
    var startValue: Boolean = value
        set(value) {
            field = value
            this.value = value
            refresh()
        }
    var type: CHECKINPUTTYPE = type
        set(value) {
            field = value
            refresh()
        }
    var name: String? = name
        set(value) {
            field = value
            refresh()
        }
    override var disabled: Boolean = disabled
        set(value) {
            field = value
            refresh()
        }
    var extraValue: String? = extraValue
        set(value) {
            field = value
            refresh()
        }
    override var size: INPUTSIZE? = null
        set(value) {
            field = value
            refresh()
        }

    override fun render(): VNode {
        return kvh("input")
    }

    override fun getSnClass(): List<StringBoolPair> {
        val cl = super.getSnClass().toMutableList()
        size?.let {
            cl.add(it.className to true)
        }
        return cl
    }

    override fun getSnAttrs(): List<StringPair> {
        val sn = super.getSnAttrs().toMutableList()
        sn.add("type" to type.type)
        if (startValue) {
            sn.add("checked" to "true")
        }
        name?.let {
            sn.add("name" to it)
        }
        if (disabled) {
            sn.add("disabled" to "true")
        }
        extraValue?.let {
            sn.add("value" to it)
        }
        return sn
    }

    override fun afterInsert(node: VNode) {
        refreshCheckedState()
    }

    override fun afterPostpatch(node: VNode) {
        refreshCheckedState()
    }

    private fun refreshCheckedState() {
        val v = getElementJQuery()?.prop("checked") as Boolean?
        if (this.value != v) {
            getElementJQuery()?.prop("checked", this.value)
        }
    }
}