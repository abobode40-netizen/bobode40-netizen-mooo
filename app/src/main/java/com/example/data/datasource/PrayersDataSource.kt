package com.example.data.datasource

import com.example.data.model.OneMinuteDeed

data class HabitDefinition(
    val id: String,
    val title: String,
    val category: String, // "prayer" | "athkar" | "quran" | "sunnah" | "charity"
    val iconName: String
)

object PrayersDataSource {

    val DAILY_HABITS_DEFINITIONS: List<HabitDefinition> = listOf(
        HabitDefinition("fajr", "صلاة الفجر في وقتها", "prayer", "mosque"),
        HabitDefinition("dhuhr", "صلاة الظهر", "prayer", "mosque"),
        HabitDefinition("asr", "صلاة العصر", "prayer", "mosque"),
        HabitDefinition("maghrib", "صلاة المغرب", "prayer", "mosque"),
        HabitDefinition("isha", "صلاة العشاء", "prayer", "mosque"),
        HabitDefinition("morning_athkar", "أذكار الصباح", "athkar", "wb_sunny"),
        HabitDefinition("evening_athkar", "أذكار المساء", "athkar", "nights_stay"),
        HabitDefinition("sleep_athkar", "أذكار النوم", "athkar", "bedtime"),
        HabitDefinition("quran_ward", "الورد القرآني اليومي", "quran", "auto_stories"),
        HabitDefinition("duha_prayer", "صلاة الضحى (صلاة الأوابين)", "sunnah", "wb_sunny"),
        HabitDefinition("witr_prayer", "صلاة الوتر وقيام الليل", "sunnah", "nightlight"),
        HabitDefinition("sadaqah", "صدقة أو تفريج كربة", "charity", "volunteer_activism")
    )

    val ONE_MINUTE_DEEDS: List<OneMinuteDeed> = listOf(
        OneMinuteDeed(
            id = "om1",
            title = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            targetCount = 100,
            categoryName = "ورد التسبيح",
            rewardFadl = "تُحط بها الخطايا وإن كانت مثل زبد البحر، وتُغرس بها نخلة في الجنة."
        ),
        OneMinuteDeed(
            id = "om2",
            title = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            targetCount = 70,
            categoryName = "ورد الاستغفار",
            rewardFadl = "سبب لمغفرة الذنوب، وسعة الرزق، ونزول الغيث، وتفريج الهموم."
        ),
        OneMinuteDeed(
            id = "om3",
            title = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
            targetCount = 20,
            categoryName = "الصلاة على النبي ﷺ",
            rewardFadl = "من صلى عليّ صلاة صلى الله عليه بها عشراً وحُطت عنه عشر خطيئات."
        ),
        OneMinuteDeed(
            id = "om4",
            title = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            targetCount = 40,
            categoryName = "كنز الجنة",
            rewardFadl = "كنز من كنوز الجنة، وباب من أبواب تفويض الأمر ودفع المشاق."
        ),
        OneMinuteDeed(
            id = "om5",
            title = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            targetCount = 10,
            categoryName = "ورد التوحيد",
            rewardFadl = "تعدل عتق رقاب وتكتب له مئة حسنة وتُمحى عنه مئة سيئة."
        ),
        OneMinuteDeed(
            id = "om6",
            title = "سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ",
            targetCount = 25,
            categoryName = "الباقيات الصالحات",
            rewardFadl = "أحب الكلام إلى الله تعالى، ومكفرات للذنوب، ومغارس الجنان."
        )
    )
}
