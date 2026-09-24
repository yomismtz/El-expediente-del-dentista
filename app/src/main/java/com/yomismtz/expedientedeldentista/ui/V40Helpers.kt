package com.yomismtz.expedientedeldentista.ui

internal fun toggleV40(list: MutableList<String>, item: String) {
    if (item in list) list.remove(item) else list.add(item)
}
