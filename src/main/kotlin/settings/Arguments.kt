package settings

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File

/**
 */

class ArgumentsProgramSettingsBuilder(val parser: ArgParser) {
    companion object {
        val DEFAULT_SETTINGS_PATH = "~/.gurps/settings.yaml"
    }
    val verbose by parser.flagging("-v", "--verbose", help = "verbose mode")

    val imageOutputFile by parser.storing("-i", "--image", help = "Image Output Path")
        { File(this) }.default(File("/tmp/character.png"))

    val campaignSettingsFile by parser.storing("-s", "--settings", help = "Campaign settings file ")
        { File(this) }.default(File(DEFAULT_SETTINGS_PATH))

    val characterSettingsFile by parser.storing("-c", "--character", help = "Character file")
        { File(this) }.default(null)

    fun build() = ProgramSettings(verbose, imageOutputFile, campaignSettingsFile, characterSettingsFile)
}

class ArgumentsCampaignSettingsBuilder(val parser: ArgParser, val defaults: CampaignSettings = CampaignSettings.DEFAULT) {
    // flags
    val showER by parser.flagging("--showER", help = "").default(defaults.showER)
    val showWP by parser.flagging("--showWP", help = "").default(defaults.showWP)

    fun build() = CampaignSettings(showER, showWP)
}

class ArgumentsCharacterSettingsBuilder(val parser: ArgParser, val defaults: CharacterSettings = CharacterSettings.DEFAULT) {
    // meta data
    val characterName by parser.storing("--name", help = "Character name. overrides values in file").default(defaults.characterName)
    val playerName by parser.storing("--player", help = "Player name. overrides values in file").default(defaults.playerName)

    // primary stats
    val ht by parser.storing("--ht", help = "Character's Health (HT). overrides values in file"){ toInt()}.default(defaults.ht)
    val st by parser.storing("--st", help = "Character's Strength (ST). overrides values in file"){ toInt()}.default(defaults.st)
    val iq by parser.storing("--iq", help = "Character's Intelligence (IQ). overrides values in file"){ toInt()}.default(defaults.iq)

    // drived stats
    val hp by parser.storing("--hp", help = "Character's Hit Points (HP). overrides values in file"){ toInt()}.default(defaults.hp)
    val fp by parser.storing("--fp", help = "Character's Fatigue Points (FP). overrides values in file"){ toInt()}.default(defaults.fp)
    val wp by parser.storing("--wp", help = "Character's Will Points (WP). overrides values in file"){ toInt() }.default(defaults.wp)
    val er by parser.storing("--er", help = "Character's Energy Reserve (ER). overrides values in file"){ toInt() }.default(defaults.er)

    // tracked stats
    val currentHP by parser.storing("--currentHP", help = "Character's Current Hit Points (HP). overrides values in file")
    { toInt() }
            .default(defaults.currentHP)
    val currentFP by parser.storing("--currentFP", help = "Character's Current Fatigue Points (FP). overrides values in file")
    { toInt() }
            .default(defaults.currentFP)
    val currentWP by parser.storing("--currentWP", help = "Character's Current Will Points (WP). overrides values in file")
    { toInt() }
            .default(defaults.currentWP)
    val currentER by parser.storing("--currentER", help = "Character's Current Energy Reserve (ER). overrides values in file")
    { toInt() }
            .default(defaults.currentER)

    fun build() = CharacterSettings(
            characterName,
            playerName,

            // primary stats
            ht,
            st,
            iq,

            // drived stats
            hp,
            fp,
            wp,
            er,

            // tracked stats
            currentHP,
            currentFP,
            currentWP,
            currentER)

}