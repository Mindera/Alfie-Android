package com.mindera.alfie.data.bag

import app.cash.turbine.test
import com.mindera.alfie.repository.bag.BagProduct
import com.mindera.alfie.repository.result.RepositoryResult
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class BagRepositoryImplTest {

    private val shirt = BagProduct(productId = "linen-shirt", variantSku = "0283/764")
    private val jeans = BagProduct(productId = "wide-leg-jeans", variantSku = "0273/234")

    @Test
    fun `addToBag - stores one entry per unit so the same variant can repeat`() = runTest {
        val subject = BagRepositoryImpl()

        subject.addToBag(shirt)
        subject.addToBag(shirt)

        subject.getBag().test {
            assertEquals(listOf(shirt, shirt), awaitItem().dataOrNull())
        }
    }

    @Test
    fun `removeFromBag - drops a single unit and leaves the rest`() = runTest {
        val subject = BagRepositoryImpl()
        repeat(3) { subject.addToBag(shirt) }

        subject.removeFromBag(shirt)

        subject.getBag().test {
            assertEquals(listOf(shirt, shirt), awaitItem().dataOrNull())
        }
    }

    @Test
    fun `removeAllFromBag - drops every unit of the variant and keeps the others`() = runTest {
        val subject = BagRepositoryImpl()
        subject.addToBag(shirt)
        subject.addToBag(jeans)
        subject.addToBag(shirt)
        subject.addToBag(shirt)

        subject.removeAllFromBag(shirt)

        subject.getBag().test {
            assertEquals(listOf(jeans), awaitItem().dataOrNull())
        }
    }

    @Test
    fun `removeAllFromBag - WHEN the variant is not in the bag THEN nothing is dropped`() = runTest {
        val subject = BagRepositoryImpl()
        subject.addToBag(jeans)

        subject.removeAllFromBag(shirt)

        subject.getBag().test {
            assertEquals(listOf(jeans), awaitItem().dataOrNull())
        }
    }

    @Test
    fun `addAllToBag - inserts every unit at the index in one write`() = runTest {
        val subject = BagRepositoryImpl()
        subject.addToBag(jeans)

        subject.getBag().test {
            assertEquals(listOf(jeans), awaitItem().dataOrNull())

            subject.addAllToBag(shirt, quantity = 3, index = 0)

            assertEquals(listOf(shirt, shirt, shirt, jeans), awaitItem().dataOrNull())
            expectNoEvents()
        }
    }

    @Test
    fun `addAllToBag - undoes removeAllFromBag without moving the line`() = runTest {
        val subject = BagRepositoryImpl()
        subject.addToBag(shirt)
        subject.addToBag(jeans)
        subject.addToBag(shirt)

        subject.removeAllFromBag(shirt)
        subject.addAllToBag(shirt, quantity = 2, index = 0)

        subject.getBag().test {
            assertEquals(listOf(shirt, shirt, jeans), awaitItem().dataOrNull())
        }
    }

    @Test
    fun `addAllToBag - WHEN the index is out of bounds THEN it is clamped`() = runTest {
        val subject = BagRepositoryImpl()
        subject.addToBag(jeans)

        subject.addAllToBag(shirt, quantity = 1, index = 5)

        subject.getBag().test {
            assertEquals(listOf(jeans, shirt), awaitItem().dataOrNull())
        }
    }

    private fun RepositoryResult<List<BagProduct>>.dataOrNull(): List<BagProduct> {
        assertIs<RepositoryResult.Success<List<BagProduct>>>(this)
        return data
    }
}
