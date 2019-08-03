package com.greendot.rewards.logger

import android.text.TextUtils
import com.greendot.rewards.logger.LogUtils.LogType.*
import timber.log.Timber

object LogUtils {

    private val MAX_LOG_LENGTH = 4000

    enum class LogType {
        V,
        D,
        I,
        W,
        E
    }

    enum class TAG_FILTER private constructor(// (Controller)


        public val key: String
    ) {

        // Feature
        SUP("SUP"), // (SuperCategories)
        MSG("MSG"), // (Message)
        UPR("UPR"), // (User Profile)
        ACC("ACC"), // (User Account)
        RCM("RCM"), // (Recommendations)
        CAR("CAR"), // (InCar)
        SDK("SDK"), // (SDK-related)
        SCH("SCH"), // (Search)
        SET("SET"), // (User Settings))
        CST("CST"), // (Casting)
        DOW("DOW"), // (Download)
        DEV("DEV"), // (Devices)
        REC("REC"), // (Audio Recovery)
        FAV("FAV"), // (Favorites)
        ART("ART"), // (Artist Radio)
        ALR("ALR"), // (Alerts)
        DLK("DLK"), // (DeepLinking)
        BYP("BYP"), // (Bypass)
        CAT("CAT"), // (Categories)
        PRX("PRX"), // (Proxy-related)
        KCH("KCH"), // (Kochava)
        UPN("UPN"), // (UpNext)
        V2_EDP("V2EDP"), //V2 EDP

        // Common
        AUD("AUD"), // (Audio)
        VID("VID"), // (Video)
        ANA("ANA"), // (Analytics)
        ANX("ANX"), // (Analytics, More Detail)
        NPL("NPL"), // (Now Playing)
        CSL("CSL"), // (Carousels)
        LOG("LOG"), // (Login/Logout)
        CCL("CCL"), // (Common-Core-Library related)
        FLT("FLT"), // (Fault)
        CHN("CHN"), // (Channel-List related)
        CFG("CFG"), // (User Configuration)
        NET("NET"), // (Network)
        SHT("SHT"), // (Shut down)
        STA("STA"), // (Status)
        SVC("SVC"), // (Service)
        ORI("ORI"), // (Orientation)

        // Utility
        WAR("WAR"), // (Warning Log)
        EXC("EXC"), // (Exception)
        APP("APP"), // (Universal tag, present on every logcat line)

        // Espresso Test
        ESPRESSO("ESPRESSO"),
        SWIPE("SWIPE"),
        UNIT_TEST("UNIT_TEST"),

        CTL("CTL")

    }

    private fun writer(logType: LogType, filterTags: FilterTags, message: String, throwable: Throwable) {
        writer(logType, null, filterTags, message, throwable)
    }

    private fun writer(
        logType: LogType,
        TAG: String?,
        filterTags: FilterTags,
        message: String,
        throwable: Throwable? = null
    ) {
        val messageHeaderString = getMessageHeader(TAG, filterTags)
        val messageHeaderLength = messageHeaderString.length
        val multiLineHeaderLength = 8 // "(dd/dd) " (8 characters)
        val maxMessageLength = MAX_LOG_LENGTH - messageHeaderLength - multiLineHeaderLength
        var chunkCount = message.length / maxMessageLength
        if (message.length % maxMessageLength > 0) {
            chunkCount += 1
        }
        for (i in 1..chunkCount) {
            val max = maxMessageLength * i
            if (max >= message.length) {
                when (logType) {
                    V -> Timber.tag(getFilterTagsString(TAG, filterTags)).v(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1))
                    )
                    D -> Timber.tag(getFilterTagsString(TAG, filterTags)).d(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1))
                    )
                    I -> Timber.tag(getFilterTagsString(TAG, filterTags)).i(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1))
                    )
                    W -> Timber.tag(getFilterTagsString(TAG, filterTags)).w(
                        "%s%s%s",
                        getMessageHeader(filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1))
                    )
                    E -> Timber.tag(getFilterTagsString(filterTags)).e(
                        throwable,
                        String.format(
                            "%s%s%s",
                            addBracketsToTagFilter(TAG_FILTER.EXC.key),
                            getMessageHeader(null, filterTags),
                            message.substring(maxMessageLength * (i - 1))
                        )
                    )
                }

            } else {
                when (logType) {
                    V -> Timber.tag(getFilterTagsString(TAG, filterTags)).v(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1), max)
                    )
                    D -> Timber.tag(getFilterTagsString(TAG, filterTags)).d(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1), max)
                    )
                    I -> Timber.tag(getFilterTagsString(TAG, filterTags)).i(
                        "%s%s%s",
                        getMessageHeader(TAG, filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1), max)
                    )
                    W -> Timber.tag(getFilterTagsString(TAG, filterTags)).w(
                        "%s%s%s",
                        getMessageHeader(filterTags),
                        multiLineHeader(i, chunkCount),
                        message.substring(maxMessageLength * (i - 1), max)
                    )
                    E -> Timber.tag(getFilterTagsString(filterTags)).e(
                        throwable,
                        String.format(
                            "%s%s%s",
                            addBracketsToTagFilter(TAG_FILTER.EXC.key),
                            getMessageHeader(null, filterTags),
                            message.substring(maxMessageLength * (i - 1), max)
                        )
                    )
                }

            }
        }
    }

    private fun multiLineHeader(i: Int, chunkCount: Int): String {
        return if (chunkCount == 1) {
            " "
        } else {
            "(" + (if (i < 10) "0$i" else i) + "/" + (if (chunkCount < 10) "0$chunkCount" else chunkCount) + ") "
        }
    }

    fun V(TAG: String, filterTags: FilterTags, message: String) {
        writer(V, TAG, filterTags, message)
    }

    fun D(TAG: String, filterTags: FilterTags, message: String) {
        writer(D, TAG, filterTags, message)
    }

    fun I(TAG: String, filterTags: FilterTags, message: String) {
        writer(I, TAG, filterTags, message)
    }

    fun LOG(logType: LogType, TAG: String, filterTags: FilterTags, message: String) {
        writer(logType, TAG, filterTags, message)
    }

    fun W(TAG: String, filterTags: FilterTags, message: String) {
        writer(W, TAG, filterTags, message)
    }

    fun E(filterTags: FilterTags, exception: Exception) {
        E(filterTags, " EXCEPTION:", exception)
    }

    fun E(filterTags: FilterTags, message: String, throwable: Throwable) {
        writer(E, filterTags, if (TextUtils.isEmpty(message)) " EXCEPTION:" else message, throwable)
    }

    class FilterTags internal constructor(args: Array<out TAG_FILTER>) {
        internal var args: Array<out TAG_FILTER>

        init { this.args = args }

        companion object {
            fun withTags(vararg args: TAG_FILTER): FilterTags {
                return FilterTags(args)
            }
        }
    }

    private fun getFilterTagsString(filterTags: FilterTags): String? {
        return getFilterTagsString(null, filterTags)
    }

    // filterTags is not used, but leave both parameters in case we want to use both some day
    private fun getFilterTagsString(TAG: String?, filterTags: FilterTags): String? {
        return TAG
    }

    // TAG is not used, but leave both parameters in case we want to use both some day
    private fun getMessageHeader(TAG: String?, filterTags: FilterTags): String {
        val tagBuffer = StringBuilder()
        tagBuffer.append(addBracketsToTagFilter(TAG_FILTER.APP.key))
        for (tagFilter in filterTags.args) {
            tagBuffer.append(addBracketsToTagFilter(tagFilter.name))
        }
        return tagBuffer.toString()
    }

    // warning message header
    private fun getMessageHeader(filterTags: FilterTags): String {
        val tagBuffer = StringBuilder()
        tagBuffer.append(addBracketsToTagFilter(TAG_FILTER.APP.key))
        tagBuffer.append(addBracketsToTagFilter(TAG_FILTER.WAR.key))
        for (tagFilter in filterTags.args) {
            tagBuffer.append(addBracketsToTagFilter(tagFilter.name))
        }
        return tagBuffer.toString()
    }

    private fun addBracketsToTagFilter(tagFilter: String): String {
        return "[$tagFilter]"
    }

}
