package com.mindera.alfie.core.commons.util.sync

interface Syncable {

    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}
