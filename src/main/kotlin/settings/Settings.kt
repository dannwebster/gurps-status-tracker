package settings

import java.io.File

/**
 */
data class ProgramSettings(
        val verbose: Boolean = false,
        val imageOutputFile: File?,
        val campaignSettingsFile: File,
        val characterSettingsFile: File?) {
}

data class CampaignSettings(val showER: Boolean, val showWP: Boolean) {
    companion object {
        val DEFAULT = CampaignSettings(false, false)
    }
}

data class CharacterSettings(
        // meta data
        val characterName: String,
        val playerName: String,

        // primary stats
        val ht: Int,
        val st: Int,
        val iq: Int,

        // drived stats
        val hp: Int,
        val fp: Int,
        val wp: Int,
        val er: Int,

        // tracked stats
        val currentHP: Int,
        val currentFP: Int,
        val currentWP: Int,
        val currentER: Int
) {
    companion object {
        val DEFAULT = CharacterSettings("Unknown Character", "Unknown Player",
                10, 10, 10,
                10, 10, 10, 10,
                10, 10, 10, 10
        )
    }
}
