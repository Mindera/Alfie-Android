package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.configuration.dto.FeatureData

internal interface Configuration {

    fun getBoolean(key: String): Boolean?

    fun getString(key: String): String?

    fun getStringList(key: String): List<String>?

    fun getData(key: String): FeatureData?
}
