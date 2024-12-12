package au.com.alfie.ecomm.core.commons.util.sync

interface Syncable {

    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}
