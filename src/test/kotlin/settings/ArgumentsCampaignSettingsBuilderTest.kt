package settings

import com.xenomachina.argparser.ArgParser
import org.junit.Assert.*
import org.junit.Test
import settings.ArgumentsCampaignSettingsBuilder

/**
 */
class ArgumentsCampaignSettingsBuilderTest {
    @Test fun shouldHaveDefaultValuesWhenArrayIsEmpty() {
        // given
        val args: Array<String> = arrayOf()
        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsCampaignSettingsBuilder(argParser).build()

        // then
        assertEquals(false, subject.showER)
        assertEquals(false, subject.showWP)
    }

    @Test fun shouldHaveValuesWhenUsingLongArgs() {
        // given
        val args: Array<String> = arrayOf(
                "--showER", "--showWP"
        )

        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsCampaignSettingsBuilder(argParser).build()

        // then
        assertEquals(true, subject.showER)
        assertEquals(true, subject.showWP)
    }
}
