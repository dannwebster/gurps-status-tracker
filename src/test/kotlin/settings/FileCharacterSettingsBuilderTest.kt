package settings

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.InputStream

/**
 */
class FileCharacterSettingsBuilderTest {
    @Test fun shouldHaveDefaultValuesWhenFileNull() {
        // given
        val file: File? = null

        // when
        val subject = FileCharacterSettingsBuilder().build(file)

        // then
        assertEquals("Unknown Character", subject.characterName)
        assertEquals("Unknown Player", subject.playerName)
        assertEquals(10, subject.st)
        assertEquals(10, subject.iq)
        assertEquals(10, subject.ht)

        assertEquals(10, subject.hp)
        assertEquals(10, subject.fp)
        assertEquals(10, subject.wp)
        assertEquals(10, subject.er)

        assertEquals(10, subject.currentHP)
        assertEquals(10, subject.currentFP)
        assertEquals(10, subject.currentWP)
        assertEquals(10, subject.currentER)
    }

    @Test fun shouldHaveDefaultReadValuesWhenFileIsAvailable() {
        // given
        val inputStream: InputStream = this::class.java.getResourceAsStream("/character.yaml")

        // when
        val subject = FileCharacterSettingsBuilder().build(inputStream)

        // then
        assertEquals("KNOWN Character", subject.characterName)
        assertEquals("KNOWN Player", subject.playerName)

        assertEquals(11, subject.st)
        assertEquals(12, subject.iq)
        assertEquals(13, subject.ht)

        assertEquals(14, subject.hp)
        assertEquals(15, subject.fp)
        assertEquals(16, subject.wp)
        assertEquals(17, subject.er)

        assertEquals(18, subject.currentHP)
        assertEquals(19, subject.currentFP)
        assertEquals(20, subject.currentWP)
        assertEquals(21, subject.currentER)
    }
}