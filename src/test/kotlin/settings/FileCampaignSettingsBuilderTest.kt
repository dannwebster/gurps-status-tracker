package settings

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.InputStream

/**
 */
class FileCampaignSettingsBuilderTest {
    @Test fun shouldHaveDefaultValuesWhenFileNull() {
        // given
        val file: File? = null

        // when
        val subject = FileCampaignSettingsBuilder().build(file)

        // then
        assertEquals(false, subject.showER)
        assertEquals(false, subject.showWP)
    }

    @Test fun shouldHaveDefaultReadValuesWhenFileIsAvailable() {
        // given
        val inputStream: InputStream = this::class.java.getResourceAsStream("/settings.yaml")

        // when
        val subject = FileCampaignSettingsBuilder().build(inputStream)

        // then
        assertEquals(true, subject.showER)
        assertEquals(true, subject.showWP)
    }
}