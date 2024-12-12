package au.com.alfie.ecomm.core.analytics.params

import android.os.Bundle
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val TEST_INT_KEY = "int_key"
private const val TEST_INT_VALUE = 123
private const val TEST_STRING_KEY = "string_key"
private const val TEST_STRING_VALUE = "str"

@ExtendWith(MockKExtension::class)
class AnalyticsParamsExtTest {

    @Test
    fun `test toBundle - int value`() {
        val expected = Bundle()
        expected.putInt(TEST_INT_KEY, TEST_INT_VALUE)

        val testIntParams = TestIntValueParams()
        val result = testIntParams.toBundle()

        assert(expected.size() == result.size())
        assert(expected.getInt(TEST_INT_KEY) == result.getInt(TEST_INT_KEY))
    }

    @Test
    fun `test toBundle - string value`() {
        val expected = Bundle()
        expected.putString(TEST_STRING_KEY, TEST_STRING_VALUE)

        val testStringParams = TestStringValueParams()
        val result = testStringParams.toBundle()

        assert(expected.size() == result.size())
        assert(expected.getString(TEST_STRING_KEY) == result.getString(TEST_STRING_VALUE))
    }

    @Test
    fun `test toBundle - both values`() {
        val expected = Bundle()
        expected.putInt(TEST_INT_KEY, TEST_INT_VALUE)
        expected.putString(TEST_STRING_KEY, TEST_STRING_VALUE)

        val testBothParams = TestBothValuesParams()
        val result = testBothParams.toBundle()

        assert(expected.size() == result.size())
        assert(expected.getInt(TEST_INT_KEY) == result.getInt(TEST_INT_KEY))
        assert(expected.getString(TEST_STRING_KEY) == result.getString(TEST_STRING_VALUE))
    }
}

private class TestIntValueParams : AnalyticsParams {
    override fun params(): HashMap<String, AnalyticsValues> = hashMapOf(
        TEST_INT_KEY to AnalyticsValues.IntValues(TEST_INT_VALUE)
    )
}

private class TestStringValueParams : AnalyticsParams {
    override fun params(): HashMap<String, AnalyticsValues> = hashMapOf(
        TEST_STRING_KEY to AnalyticsValues.StringValues(TEST_STRING_VALUE)
    )
}

private class TestBothValuesParams : AnalyticsParams {
    override fun params(): HashMap<String, AnalyticsValues> = hashMapOf(
        TEST_INT_KEY to AnalyticsValues.IntValues(TEST_INT_VALUE),
        TEST_STRING_KEY to AnalyticsValues.StringValues(TEST_STRING_VALUE)
    )
}
