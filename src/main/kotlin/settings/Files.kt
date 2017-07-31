package settings

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 */

fun <T> InputStream.yaml( callback : (reader: Reader, ObjectMapper) -> T): T {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerKotlinModule()
    this.reader().use { reader -> return callback(reader, mapper) }
}

class FileCampaignSettingsBuilder(val defaults: CampaignSettings = CampaignSettings.DEFAULT) {

    fun build(inputStream: InputStream) =
            inputStream.yaml { reader, mapper ->
                val root = mapper.readTree(reader)
                val showER = root["showER"].asBoolean()
                val showWP = root["showWP"].asBoolean()
                CampaignSettings(showER ?: defaults.showER, showWP ?: defaults.showWP)
            }

    fun build(campaignSettingsFile: File?) =
            if (campaignSettingsFile == null || !campaignSettingsFile.exists())
                defaults
            else campaignSettingsFile.inputStream().use { build(it) }
}

class FileCharacterSettingsBuilder(val defaults: CharacterSettings = CharacterSettings.DEFAULT) {

    fun build(characterSettingsFile: File?) =
            if (characterSettingsFile == null)
                defaults
            else characterSettingsFile.inputStream().use { build(it) }

    fun build(inputStream: InputStream) =
            inputStream.yaml { reader, mapper ->
                val root = mapper.readTree(reader)
                CharacterSettings(
                        root["characterName"].asText(defaults.characterName),
                        root["playerName"].asText(defaults.playerName),

                        // primary stats
                        root["ht"].asInt(defaults.ht),
                        root["st"].asInt(defaults.st),
                        root["iq"].asInt(defaults.iq),

                        // drived stats
                        root["hp"].asInt(defaults.hp),
                        root["fp"].asInt(defaults.fp),
                        root["wp"].asInt(defaults.wp),
                        root["er"].asInt(defaults.er),

                        // tracked stats
                        root["currentHP"].asInt(defaults.currentHP),
                        root["currentFP"].asInt(defaults.currentFP),
                        root["currentWP"].asInt(defaults.currentWP),
                        root["currentER"].asInt(defaults.currentER))
            }
}