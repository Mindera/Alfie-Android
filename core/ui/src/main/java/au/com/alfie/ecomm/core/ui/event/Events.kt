package au.com.alfie.ecomm.core.ui.event

typealias ClickEvent = () -> Unit
typealias ClickEventOneArg<T> = (T) -> Unit
typealias ClickEventTwoArg<T, L> = (T, L) -> Unit
