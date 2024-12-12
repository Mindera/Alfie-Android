package au.com.alfie.ecomm.core.sync.worker

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DelegatingWorkerTest {

    companion object {
        private const val WORKER_CLASS_NAME = "WORKER_CLASS_NAME"
    }

    @Test
    fun testDelegateData() {
        val data = SyncWorker::class.delegatedData()
        val workerName = data.getString(WORKER_CLASS_NAME)
        val expectedName = SyncWorker::class.qualifiedName

        assertEquals(expectedName, workerName)
    }
}
