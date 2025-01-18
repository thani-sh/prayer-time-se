package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import me.thanish.prayers.se.R

/**
 * PrayerTimeCity is the city of a prayer time.
 */
enum class PrayerTimeCity(private val key: Int) {
    alingsas(R.string.prayers_city_alingsås),
    amal(R.string.prayers_city_åmål),
    angelholm(R.string.prayers_city_ängelholm),
    avesta(R.string.prayers_city_avesta),
    bengtsfors(R.string.prayers_city_bengtsfors),
    boden(R.string.prayers_city_boden),
    bollnas(R.string.prayers_city_bollnäs),
    boras(R.string.prayers_city_borås),
    borlange(R.string.prayers_city_borlänge),
    eksjo(R.string.prayers_city_eksjö),
    enkoping(R.string.prayers_city_enköping),
    eskilstuna(R.string.prayers_city_eskilstuna),
    eslov(R.string.prayers_city_eslöv),
    falkenberg(R.string.prayers_city_falkenberg),
    falkoping(R.string.prayers_city_falköping),
    filipstad(R.string.prayers_city_filipstad),
    flen(R.string.prayers_city_flen),
    gallivare(R.string.prayers_city_gällivare),
    gavle(R.string.prayers_city_gävle),
    gislaved(R.string.prayers_city_gislaved),
    gnosjo(R.string.prayers_city_gnosjö),
    goteborg(R.string.prayers_city_göteborg),
    halmstad(R.string.prayers_city_halmstad),
    haparanda(R.string.prayers_city_haparanda),
    harnosand(R.string.prayers_city_härnösand),
    hassleholm(R.string.prayers_city_hässleholm),
    helsingborg(R.string.prayers_city_helsingborg),
    hogsby(R.string.prayers_city_högsby),
    horby(R.string.prayers_city_hörby),
    hudiksvall(R.string.prayers_city_hudiksvall),
    hultsfred(R.string.prayers_city_hultsfred),
    jokkmokk(R.string.prayers_city_jokkmokk),
    jonkoping(R.string.prayers_city_jönköping),
    kalmar(R.string.prayers_city_kalmar),
    kalrshamn(R.string.prayers_city_kalrshamn),
    karlskoga(R.string.prayers_city_karlskoga),
    karlskrona(R.string.prayers_city_karlskrona),
    karlstad(R.string.prayers_city_karlstad),
    katrineholm(R.string.prayers_city_katrineholm),
    kiruna(R.string.prayers_city_kiruna),
    koping(R.string.prayers_city_köping),
    kristianstad(R.string.prayers_city_kristianstad),
    kristinehamn(R.string.prayers_city_kristinehamn),
    laholm(R.string.prayers_city_laholm),
    landskrona(R.string.prayers_city_landskrona),
    lessebo(R.string.prayers_city_lessebo),
    lidkoping(R.string.prayers_city_lidköping),
    linkoping(R.string.prayers_city_linköping),
    ludvika(R.string.prayers_city_ludvika),
    lulea(R.string.prayers_city_luleå),
    lund(R.string.prayers_city_lund),
    lysekil(R.string.prayers_city_lysekil),
    malmo(R.string.prayers_city_malmö),
    mariestad(R.string.prayers_city_mariestad),
    marsta(R.string.prayers_city_märsta),
    mellerud(R.string.prayers_city_mellerud),
    mjolby(R.string.prayers_city_mjölby),
    monsteras(R.string.prayers_city_mönsterås),
    munkedal(R.string.prayers_city_munkedal),
    nassjo(R.string.prayers_city_nässjö),
    norrkoping(R.string.prayers_city_norrköping),
    norrtalje(R.string.prayers_city_norrtälje),
    nybro(R.string.prayers_city_nybro),
    nykoping(R.string.prayers_city_nyköping),
    nynashamn(R.string.prayers_city_nynäshamn),
    orebro(R.string.prayers_city_örebro),
    ornskoldsvik(R.string.prayers_city_örnsköldsvik),
    oskarshamn(R.string.prayers_city_oskarshamn),
    ostersund(R.string.prayers_city_östersund),
    oxelosund(R.string.prayers_city_oxelösund),
    pajala(R.string.prayers_city_pajala),
    pitea(R.string.prayers_city_piteå),
    ronneby(R.string.prayers_city_ronneby),
    saffle(R.string.prayers_city_säffle),
    sala(R.string.prayers_city_sala),
    savsjo(R.string.prayers_city_sävsjö),
    simrishamn(R.string.prayers_city_simrishamn),
    skara(R.string.prayers_city_skara),
    skelleftea(R.string.prayers_city_skellefteå),
    skovde(R.string.prayers_city_skövde),
    soderhamn(R.string.prayers_city_söderhamn),
    sodertalje(R.string.prayers_city_södertälje),
    solleftea(R.string.prayers_city_sollefteå),
    solvesborg(R.string.prayers_city_sölvesborg),
    stockholm(R.string.prayers_city_stockholm),
    strangnas(R.string.prayers_city_strängnäs),
    sundsvall(R.string.prayers_city_sundsvall),
    tierp(R.string.prayers_city_tierp),
    tranemo(R.string.prayers_city_tranemo),
    trelleborg(R.string.prayers_city_trelleborg),
    trollhattan(R.string.prayers_city_trollhättan),
    uddevalla(R.string.prayers_city_uddevalla),
    ulricehamn(R.string.prayers_city_ulricehamn),
    umea(R.string.prayers_city_umeå),
    uppsala(R.string.prayers_city_uppsala),
    vanersborg(R.string.prayers_city_vänersborg),
    varberg(R.string.prayers_city_varberg),
    varnamo(R.string.prayers_city_värnamo),
    vasteras(R.string.prayers_city_västerås),
    vastervik(R.string.prayers_city_västervik),
    vaxjo(R.string.prayers_city_växjö),
    vetlanda(R.string.prayers_city_vetlanda),
    vimmerby(R.string.prayers_city_vimmerby),
    visby(R.string.prayers_city_visby),
    ystad(R.string.prayers_city_ystad);

    /**
     * getLabel returns the name of the city with i18n.
     */
    fun getLabel(context: Context): String {
        return context.getString(key)
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * STORE_KEY is the MMKV key for storing the current prayer city.
         */
        private val STORE_KEY = intPreferencesKey("PrayerTimeCity")


        /**
         * set sets the current prayer city.
         */
        fun set(context: Context, city: PrayerTimeCity) {
            setIntegerSync(context, STORE_KEY, city.ordinal)
        }

        /**
         * get returns the current prayer city.
         */
        fun get(context: Context): PrayerTimeCity {
            val index = getIntegerSync(context, STORE_KEY, stockholm.ordinal)
            return entries[index]
        }
    }
}
