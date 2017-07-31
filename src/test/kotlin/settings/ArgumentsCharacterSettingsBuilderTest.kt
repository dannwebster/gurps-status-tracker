package settings

import com.xenomachina.argparser.ArgParser
import org.junit.Assert.*
import org.junit.Test
import settings.ArgumentsCharacterSettingsBuilder

/**
 */
class ArgumentsCharacterSettingsBuilderTest {
    @Test fun shouldHaveDefaultValuesWhenArrayIsEmpty() {
        // given
        val args: Array<String> = arrayOf()
        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsCharacterSettingsBuilder(argParser).build()

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

    @Test fun shouldHaveValuesWhenUsingLongArgs() {
        // given
        val args: Array<String> = arrayOf(
                "--name", "KNOWN Character",
                "--player", "KNOWN Player",

                "--st", "11",
                "--iq", "12",
                "--ht", "13",

                "--hp", "14",
                "--fp", "15",
                "--wp", "16",
                "--er", "17",

                "--currentHP", "18",
                "--currentFP", "19",
                "--currentWP", "20",
                "--currentER", "21"
        )

        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsCharacterSettingsBuilder(argParser).build()

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
