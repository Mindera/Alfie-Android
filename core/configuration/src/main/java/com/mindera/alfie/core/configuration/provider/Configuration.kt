package com.mindera.alfie.core.configuration.provider

import com.mindera.alfie.core.configuration.dto.FeatureData

internal interface Configuration {

    fun getBoolean(key: String): Boolean?

    fun getString(key: String): String?

    fun getStringList(key: String): List<String>?

    fun getData(key: String): FeatureData?
}
