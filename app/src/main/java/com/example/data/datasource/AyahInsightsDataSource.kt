package com.example.data.datasource

import com.example.data.model.AyahFullInsight
import com.example.data.model.AyahWordMeaning

object AyahInsightsDataSource {

    val AYAH_INSIGHTS: Map<String, AyahFullInsight> = mapOf(
        "1:1" to AyahFullInsight(
            surahNumber = 1,
            surahName = "الفاتحة",
            ayahNumber = 1,
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            ibnKathirTafseer = "ابتدأ بها كتاب الله تعالى، وهي تشتمل على اسم الله الأعظم والرحمن والرحيم. ومعنى البسملة: أبدأ قراءتي وتلاوتي متبركاً ومستعيناً باسم الله الأجلّ الأكرم المستحق للعبادة وحده.",
            wordMeanings = listOf(
                AyahWordMeaning("بِسْمِ اللَّهِ", "أبدأ متبركاً ومستعيناً بالاسم الأعظم"),
                AyahWordMeaning("الرَّحْمَٰنِ", "ذو الرحمة الواسعة الشاملة لجميع الخلائق"),
                AyahWordMeaning("الرَّحِيمِ", "المتعطف برحمته الخاصة على عباده المؤمنين")
            ),
            asbabNuzul = "نزلت فاتحة الكتاب بمكة في قول جمهور العلماء، وهي أول سورة نزلت كاملة.",
            tadabburPoints = listOf(
                "افتتاح كل عمل بالبسملة يورث البركة والتوفيق ودوام الصلة بالله عز وجل.",
                "اقتران اسم الرحمن بالرحيم يبعث في قلب المؤمن الرجاء والأنس وسعة فضل الله."
            ),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001001.mp3"
        ),
        "1:2" to AyahFullInsight(
            surahNumber = 1,
            surahName = "الفاتحة",
            ayahNumber = 2,
            text = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            ibnKathirTafseer = "الحمد هو الثناء بالجميل الاختياري على الله على وجه التعظيم والمحبة، وهو أعم من الشكر؛ والرب هو السيد المالك المربي لجميع العالمين بنعمه وإيجاده.",
            wordMeanings = listOf(
                AyahWordMeaning("الْحَمْدُ لِلَّهِ", "الشكر والثناء الخالص لله وحده على نعمه وكماله"),
                AyahWordMeaning("رَبِّ", "المالك المتصرف المربي لخلقه بالنعم"),
                AyahWordMeaning("الْعَالَمِينَ", "جميع ما سوى الله من الإنس والجن والملائكة وسائر المخلوقات")
            ),
            tadabburPoints = listOf(
                "استحضار نعم الله يملأ القلب محبة وشكراً ورضا بما قسم الله.",
                "ربوبية الله للعالمين تقتضي إفراده وحده بالعبادة والخضوع."
            ),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001002.mp3"
        ),
        "112:1" to AyahFullInsight(
            surahNumber = 112,
            surahName = "الإخلاص",
            ayahNumber = 1,
            text = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            ibnKathirTafseer = "أي هو الواحد الأحد الذي لا نظير له ولا وزير ولا شبيه ولا كفء، ولا يطلق هذا اللفظ على أحد في الإثبات إلا على الله عز وجل لأنه الكامل في جميع صفاته وأفعاله.",
            wordMeanings = listOf(
                AyahWordMeaning("قُلْ", "قل يا رسول الله للناس معلناً"),
                AyahWordMeaning("هُوَ اللَّهُ", "المعبود الحق"),
                AyahWordMeaning("أَحَدٌ", "الواحد المتفرد الذي لا شريك له")
            ),
            asbabNuzul = "سأل المشركون رسول الله ﷺ: انسب لنا ربك، فأنزل الله تبارك وتعالى هذه السورة المباركة.",
            tadabburPoints = listOf(
                "سورة الإخلاص تعدل ثلث القرآن لاشتمالها على التوحيد الخالص لله وتنزيهه عن النقائص."
            ),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112001.mp3"
        )
    )

    fun getInsight(surahNumber: Int, ayahNumber: Int): AyahFullInsight {
        val key = "$surahNumber:$ayahNumber"
        val found = AYAH_INSIGHTS[key]
        if (found != null) return found

        val surah = QuranDataSource.SURAHS_LIST.find { it.number == surahNumber }
        val surahName = surah?.name ?: "السورة"
        return AyahFullInsight(
            surahNumber = surahNumber,
            surahName = surahName,
            ayahNumber = ayahNumber,
            text = "آية رقم $ayahNumber من سورة $surahName",
            ibnKathirTafseer = "تفسير ميسر للآية الكريمة رقم $ayahNumber من سورة $surahName المباركة: تضمنت الآية إرشادات ربانية وتوجيهات إيمانية عظيمة تزيد المؤمن إيماناً ويقيناً.",
            wordMeanings = listOf(
                AyahWordMeaning("معاني الكلمات", "بيان وتوضيح مفردات الآية الكريمة لتقريب فهم المعنى المراد")
            ),
            tadabburPoints = listOf(
                "التدبر في الآيات والعمل بمقتضاها هو غاية إنزال القرآن العظيم."
            ),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/${String.format("%03d%03d", surahNumber, ayahNumber)}.mp3"
        )
    }
}
