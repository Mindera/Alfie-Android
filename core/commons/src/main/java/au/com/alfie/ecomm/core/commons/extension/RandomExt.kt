package au.com.alfie.ecomm.core.commons.extension

import kotlin.random.Random

fun Random.nextFloat(from: Float, until: Float): Float = from + nextFloat() * (until - from)
