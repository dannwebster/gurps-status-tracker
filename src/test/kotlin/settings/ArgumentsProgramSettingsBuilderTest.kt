package settings

import com.xenomachina.argparser.ArgParser
import org.junit.Assert.*
import org.junit.Test

/**
 */
class ArgumentsProgramSettingsBuilderTest {
    @Test fun shouldHaveDefaultValuesWhenArrayIsEmpty() {
        // given
        val args: Array<String> = arrayOf()
        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsProgramSettingsBuilder(argParser).build()

        // then
        assertEquals(null, subject.imageOutputFile?.toString())
        assertEquals(ArgumentsProgramSettingsBuilder.DEFAULT_SETTINGS_PATH, subject.campaignSettingsFile.toString())
        assertEquals(null, subject.characterSettingsFile)
        assertEquals(false, subject.verbose)
    }

    @Test fun shouldHaveValuesWhenUsingLongArgs() {
        // given
        val args: Array<String> = arrayOf(
                "--image", "image.png",
                "--settings", "settings.yaml",
                "--character", "character.yaml",
                "--verbose"
        )

        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsProgramSettingsBuilder(argParser).build()

        // then
        assertEquals("image.png", subject.imageOutputFile?.name)
        assertEquals("settings.yaml", subject.campaignSettingsFile.name)
        assertEquals("character.yaml", subject.characterSettingsFile?.name)
        assertEquals(true, subject.verbose)
    }

    @Test fun shouldHaveValuesWhenUsingShortArgs() {
        // given
        val args: Array<String> = arrayOf(
                "-i", "image.png",
                "-s", "settings.yaml",
                "-c", "character.yaml",
                "-v"
        )

        val argParser = ArgParser(args)

        // when
        val subject = ArgumentsProgramSettingsBuilder(argParser).build()

        // then
        assertEquals("image.png", subject.imageOutputFile?.name)
        assertEquals("settings.yaml", subject.campaignSettingsFile.name)
        assertEquals("character.yaml", subject.characterSettingsFile?.name)
        assertEquals(true, subject.verbose)
    }

}
