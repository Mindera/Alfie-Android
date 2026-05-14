package com.mindera.alfie.core.ui.event

typealias ClickEvent = () -> Unit
typealias ClickEventOneArg<T> = (T) -> Unit
typealias ClickEventTwoArg<T, L> = (T, L) -> Unit
