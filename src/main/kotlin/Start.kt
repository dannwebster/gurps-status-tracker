/**
 */

import com.xenomachina.argparser.ArgParser
import settings.*


class Start() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val programSettings = ArgumentsProgramSettingsBuilder(ArgParser(args)).build()

            val campaignSettings = campaignSettings(ArgParser(args), programSettings)
            val characterSettings = characterSettings(ArgParser(args), programSettings)

            val statusTracker = StatusTracker(campaignSettings, characterSettings)
            val img = ImageVisualization(statusTracker)
            img.exportImage(programSettings)
        }

        private fun characterSettings(argParser: ArgParser, programSettings: ProgramSettings): CharacterSettings {
            val fileCharacterSettings = FileCharacterSettingsBuilder().build(programSettings.characterSettingsFile)
            val characterSettings = ArgumentsCharacterSettingsBuilder(argParser, fileCharacterSettings).build()
            return characterSettings
        }

        private fun campaignSettings(argParser: ArgParser, programSettings: ProgramSettings): CampaignSettings {
            val fileCampaignSettings = FileCampaignSettingsBuilder().build(programSettings.campaignSettingsFile)
            val campaignSettings = ArgumentsCampaignSettingsBuilder(argParser, fileCampaignSettings).build()
            return campaignSettings
        }
    }

}

