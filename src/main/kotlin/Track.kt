import settings.CampaignSettings
import settings.CharacterSettings

/**
 */

data class TrackRow(val info: List<String>, val range: IntProgression? = null) {
}

data class Track(val headers: List<String>, val rows: List<TrackRow>) {
}


data class StatusTracker(val campaignSettings: CampaignSettings, val characterSettings: CharacterSettings) {
    val characterName = characterSettings.characterName
    val hp = characterSettings.hp
    val wp = characterSettings.wp
    val fp = characterSettings.fp
    val ht = characterSettings.ht

    val hpTrack = hpTrack()
    val fpTrack = fpTrack()
    val wpTrack = wpTrack()

    fun koOrDieDesc(mod: Int) = "-${mod}*HP to -${mod+1}*HP"
    fun koEffect(mod: Int, ht: Int) = "Each Turn, roll vs. HT-${mod} (${ht-mod}) KO"
    fun dieEffect(mod: Int, ht: Int, hp: Int) = "At -${mod*hp} HP, roll vs. HT (${ht}) or Die"
    fun koOrDieEffect(mod: Int, ht: Int, hp: Int) = koEffect(mod, ht) + "\n" + dieEffect(mod, ht, hp)
    fun koOrDieRow (mod: Int, ht: Int, hp: Int) = TrackRow(listOf(koOrDieDesc(mod), "Dying", koOrDieEffect(mod, ht, hp)), -mod*hp downTo -(mod+1)*hp)

    fun hpTrack() : Track {
        return Track(
                listOf("Hit Point (HP) Tracking", "Range", "Description", "Effects"),
                listOf(
                        TrackRow(listOf("+HP to +1/3 HP", "OK", "None"), hp downTo (wp/3)),
                        TrackRow(listOf("+1/3 HP to 1", "Reeling", "1/2 BS, Move, Dodge"), (hp/3) downTo 1),
                        TrackRow(listOf("+0 to -HP", "Verge of Collapse", koEffect(0, hp)), 0 downTo (-1*hp)),
                        koOrDieRow(1, hp, ht),
                        koOrDieRow(2, hp, ht),
                        koOrDieRow(3, hp, ht),
                        koOrDieRow(4, hp, ht),
                        TrackRow(listOf("-5*HP\n-10*HP", "Instant Death\nTotal Destruction", "At -${5*hp}, Instant Death\nAt -${10*hp} Body Destroyed"))
                )
        )
    }

    fun fpTrack() : Track {
        return Track(
                listOf("Fatigue Point (FP) Tracking", "Range", "Description", "Effects"),
                listOf(
                        TrackRow(listOf("+FP to +1/3 FP", "OK", "None"), fp downTo fp/3),
                        TrackRow(listOf("+1/3 FP to 1", "Very Tired", "Halve MS, BS, ST"), fp/3 downTo 1),
                        TrackRow(listOf("0 to -FP", "Verge of Collapse", "Each Turn, roll vs HT or KO"), 0 downTo -fp),
                        TrackRow(listOf("-FP", "Unconscious", "At -${fp}, Unconscious"))

        ))

    }

    fun wpTrack() : Track {
        return Track(
                listOf("Will Point (HP) Tracking", "Range", "Will Mod", "Self Control", "Supernatural Interference"),
                listOf(
                        TrackRow(listOf("+Will to 2/3 Will", "-0", "No Effect", "None"), wp downTo ((2*wp)/3)),
                        TrackRow(listOf("+2/3 Will to 1/3 Will", "-2", "One Level Harder", "Mild"), ((2*wp)/3) downTo (wp/3)),
                        TrackRow(listOf("+1/3 Will to 1", "-5", "Auto-fail", "Major"), ((wp)/3) downTo 1),
                        TrackRow(listOf("+0 to -Will", "-7", "Compulsion", "Constant"), 0 downTo (-1*wp))
                )
        )

    }
}