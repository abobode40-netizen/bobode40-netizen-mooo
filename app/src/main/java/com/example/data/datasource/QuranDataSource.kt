package com.example.data.datasource

import com.example.data.model.AyahData
import com.example.data.model.ReciterInfo
import com.example.data.model.SurahMeta

object QuranDataSource {

    val RECITERS_LIST: List<ReciterInfo> = listOf(
        ReciterInfo(
            id = "alafasy",
            name = "مشاري راشد العفاسي",
            subname = "تلاوة عذبة وشجية مرتلة",
            category = "مرتل",
            serverUrl = "https://server8.mp3quran.net/afs"
        ),
        ReciterInfo(
            id = "sudais",
            name = "عبد الرحمن السديس",
            subname = "إمام وخطيب المسجد الحرام",
            category = "أئمة الحرمين",
            serverUrl = "https://server11.mp3quran.net/sds"
        ),
        ReciterInfo(
            id = "shuraym",
            name = "سعود الشريم",
            subname = "إمام المسجد الحرام سابقاً",
            category = "أئمة الحرمين",
            serverUrl = "https://server7.mp3quran.net/shur"
        ),
        ReciterInfo(
            id = "muaiqly",
            name = "ماهر المعيقلي",
            subname = "إمام الحرم المكي الشريف",
            category = "أئمة الحرمين",
            serverUrl = "https://server12.mp3quran.net/maher"
        ),
        ReciterInfo(
            id = "dussary",
            name = "ياسر الدوسري",
            subname = "إمام المسجد الحرام - صوت مؤثر",
            category = "أئمة الحرمين",
            serverUrl = "https://server11.mp3quran.net/yasser"
        ),
        ReciterInfo(
            id = "hussary",
            name = "محمود خليل الحصري",
            subname = "شيخ عموم المقارئ المصرية",
            category = "مرتل",
            serverUrl = "https://server13.mp3quran.net/husr"
        ),
        ReciterInfo(
            id = "minshawi",
            name = "محمد صديق المنشاوي",
            subname = "الصوت الباكي - تلاوة خاشعة",
            category = "مرتل",
            serverUrl = "https://server10.mp3quran.net/minsh"
        ),
        ReciterInfo(
            id = "abdulbasit",
            name = "عبد الباسط عبد الصمد",
            subname = "صوت مكة - المصحف المرتل",
            category = "مجود",
            serverUrl = "https://server7.mp3quran.net/basit"
        ),
        ReciterInfo(
            id = "ajmi",
            name = "أحمد بن علي العجمي",
            subname = "نبرة مؤثرة وقوية وواضحة",
            category = "تلاوات خاشعة",
            serverUrl = "https://server10.mp3quran.net/ajm"
        ),
        ReciterInfo(
            id = "ghamadi",
            name = "سعد الغامدي",
            subname = "صوت هادئ يبعث السكينة",
            category = "مرتل",
            serverUrl = "https://server7.mp3quran.net/s_gmd"
        ),
        ReciterInfo(
            id = "qatam",
            name = "ناصر القطامي",
            subname = "تلاوات تهجد ومناجاة باكية",
            category = "تلاوات خاشعة",
            serverUrl = "https://server6.mp3quran.net/qtm"
        ),
        ReciterInfo(
            id = "shatri",
            name = "أبو بكر الشاطري",
            subname = "ترتيل عذب متناسق ومريح للنفس",
            category = "مرتل",
            serverUrl = "https://server11.mp3quran.net/shatri"
        ),
        ReciterInfo(
            id = "huthifi",
            name = "علي بن عبد الرحمن الحذيفي",
            subname = "إمام وخطيب المسجد النبوي",
            category = "أئمة الحرمين",
            serverUrl = "https://server9.mp3quran.net/hthfi"
        ),
        ReciterInfo(
            id = "abkar",
            name = "إدريس أبكر",
            subname = "تلاوة ندية محبرة ومؤثرة",
            category = "تلاوات خاشعة",
            serverUrl = "https://server10.mp3quran.net/abkr"
        ),
        ReciterInfo(
            id = "abbad",
            name = "فارس عباد",
            subname = "تلاوة رخيمة وعذبة جداً",
            category = "تلاوات خاشعة",
            serverUrl = "https://server8.mp3quran.net/frs_a"
        )
    )

    fun getSurahAudioUrl(reciterId: String, surahNumber: Int): String {
        val reciter = RECITERS_LIST.find { it.id == reciterId } ?: RECITERS_LIST.first()
        val formattedNum = String.format("%03d", surahNumber)
        return "${reciter.serverUrl}/$formattedNum.mp3"
    }

    val SURAHS_LIST: List<SurahMeta> = listOf(
        SurahMeta(1, "الفاتحة", "Al-Faatiha", "The Opening", "Meccan", 7, 1, 1),
        SurahMeta(2, "البقرة", "Al-Baqara", "The Cow", "Medinan", 286, 2, 1),
        SurahMeta(3, "آل عمران", "Aal-i-Imraan", "The Family of Imraan", "Medinan", 200, 50, 3),
        SurahMeta(4, "النساء", "An-Nisaa", "The Women", "Medinan", 176, 77, 4),
        SurahMeta(5, "المائدة", "Al-Maaida", "The Table", "Medinan", 120, 106, 6),
        SurahMeta(6, "الأنعام", "Al-An'aam", "The Cattle", "Meccan", 165, 128, 7),
        SurahMeta(7, "الأعراف", "Al-A'raaf", "The Heights", "Meccan", 206, 151, 8),
        SurahMeta(8, "الأنفال", "Al-Anfaal", "The Spoils of War", "Medinan", 75, 177, 9),
        SurahMeta(9, "التوبة", "At-Tawba", "The Repentance", "Medinan", 129, 187, 10),
        SurahMeta(10, "يونس", "Yunus", "Jonas", "Meccan", 109, 208, 11),
        SurahMeta(11, "هود", "Hud", "Hud", "Meccan", 123, 221, 11),
        SurahMeta(12, "يوسف", "Yusuf", "Joseph", "Meccan", 111, 235, 12),
        SurahMeta(13, "الرعد", "Ar-Ra'd", "The Thunder", "Medinan", 43, 249, 13),
        SurahMeta(14, "إبراهيم", "Ibrahim", "Abraham", "Meccan", 52, 255, 13),
        SurahMeta(15, "الحجر", "Al-Hijr", "The Rock", "Meccan", 99, 262, 14),
        SurahMeta(16, "النحل", "An-Nahl", "The Bee", "Meccan", 128, 267, 14),
        SurahMeta(17, "الإسراء", "Al-Israa", "The Night Journey", "Meccan", 111, 282, 15),
        SurahMeta(18, "الكهف", "Al-Kahf", "The Cave", "Meccan", 110, 293, 15),
        SurahMeta(19, "مريم", "Maryam", "Mary", "Meccan", 98, 305, 16),
        SurahMeta(20, "طه", "Taa-Haa", "Taa-Haa", "Meccan", 135, 312, 16),
        SurahMeta(21, "الأنبياء", "Al-Anbiyaa", "The Prophets", "Meccan", 112, 322, 17),
        SurahMeta(22, "الحج", "Al-Hajj", "The Pilgrimage", "Medinan", 78, 332, 17),
        SurahMeta(23, "المؤمنون", "Al-Muminoon", "The Believers", "Meccan", 118, 342, 18),
        SurahMeta(24, "النور", "An-Noor", "The Light", "Medinan", 64, 350, 18),
        SurahMeta(25, "الفرقان", "Al-Furqaan", "The Criterion", "Meccan", 77, 359, 18),
        SurahMeta(26, "الشعراء", "Ash-Shu'araa", "The Poets", "Meccan", 227, 367, 19),
        SurahMeta(27, "النمل", "An-Naml", "The Ant", "Meccan", 93, 377, 19),
        SurahMeta(28, "القصص", "Al-Qasas", "The Stories", "Meccan", 88, 385, 20),
        SurahMeta(29, "العنكبوت", "Al-Ankaboot", "The Spider", "Meccan", 69, 396, 20),
        SurahMeta(30, "الروم", "Ar-Room", "The Romans", "Meccan", 60, 404, 21),
        SurahMeta(31, "لقمان", "Luqman", "Luqman", "Meccan", 34, 411, 21),
        SurahMeta(32, "السجدة", "As-Sajda", "The Prostration", "Meccan", 30, 415, 21),
        SurahMeta(33, "الأحزاب", "Al-Ahzaab", "The Clans", "Medinan", 73, 418, 21),
        SurahMeta(34, "سبأ", "Saba", "Sheba", "Meccan", 54, 428, 22),
        SurahMeta(35, "فاطر", "Faatir", "The Originator", "Meccan", 45, 434, 22),
        SurahMeta(36, "يس", "Yaseen", "Ya-Sin", "Meccan", 83, 440, 22),
        SurahMeta(37, "الصافات", "As-Saaffaat", "Those in Ranks", "Meccan", 182, 446, 23),
        SurahMeta(38, "ص", "Saad", "Saad", "Meccan", 88, 453, 23),
        SurahMeta(39, "الزمر", "Az-Zumar", "The Groups", "Meccan", 75, 458, 23),
        SurahMeta(40, "غافر", "Ghafir", "The Forgiver", "Meccan", 85, 467, 24),
        SurahMeta(41, "فصلت", "Fussilat", "Explained in Detail", "Meccan", 54, 477, 24),
        SurahMeta(42, "الشورى", "Ash-Shura", "Consultation", "Meccan", 53, 483, 25),
        SurahMeta(43, "الزخرف", "Az-Zukhruf", "Ornaments of Gold", "Meccan", 89, 489, 25),
        SurahMeta(44, "الدخان", "Ad-Dukhaan", "The Smoke", "Meccan", 59, 496, 25),
        SurahMeta(45, "الجاثية", "Al-Jaathiya", "Crouching", "Meccan", 37, 499, 25),
        SurahMeta(46, "الأحقاف", "Al-Ahqaaf", "The Dunes", "Meccan", 35, 502, 26),
        SurahMeta(47, "محمد", "Muhammad", "Muhammad", "Medinan", 38, 507, 26),
        SurahMeta(48, "الفتح", "Al-Fath", "The Victory", "Medinan", 29, 511, 26),
        SurahMeta(49, "الحجرات", "Al-Hujuraat", "The Apartments", "Medinan", 18, 515, 26),
        SurahMeta(50, "ق", "Qaaf", "Qaaf", "Meccan", 45, 518, 26),
        SurahMeta(51, "الذاريات", "Adh-Dhaariyat", "The Winnowing Winds", "Meccan", 60, 520, 26),
        SurahMeta(52, "الطور", "At-Toor", "The Mount", "Meccan", 49, 523, 27),
        SurahMeta(53, "النجم", "An-Najm", "The Star", "Meccan", 62, 526, 27),
        SurahMeta(54, "القمر", "Al-Qamar", "The Moon", "Meccan", 55, 528, 27),
        SurahMeta(55, "الرحمن", "Ar-Rahmaan", "The Beneficent", "Medinan", 78, 531, 27),
        SurahMeta(56, "الواقعة", "Al-Waaqia", "The Inevitable", "Meccan", 96, 534, 27),
        SurahMeta(57, "الحديد", "Al-Hadid", "The Iron", "Medinan", 29, 537, 27),
        SurahMeta(58, "المجادلة", "Al-Mujaadila", "The Pleading Woman", "Medinan", 22, 542, 28),
        SurahMeta(59, "الحشر", "Al-Hashr", "The Exile", "Medinan", 24, 545, 28),
        SurahMeta(60, "الممتحنة", "Al-Mumtahana", "Examined", "Medinan", 13, 549, 28),
        SurahMeta(61, "الصف", "As-Saff", "The Ranks", "Medinan", 14, 551, 28),
        SurahMeta(62, "الجمعة", "Al-Jumu'a", "Friday", "Medinan", 11, 553, 28),
        SurahMeta(63, "المنافقون", "Al-Munaafiqoon", "The Hypocrites", "Medinan", 11, 554, 28),
        SurahMeta(64, "التغابن", "At-Taghaabun", "Mutual Disillusion", "Medinan", 18, 556, 28),
        SurahMeta(65, "الطلاق", "At-Talaaq", "Divorce", "Medinan", 12, 558, 28),
        SurahMeta(66, "التحريم", "At-Tahrim", "The Prohibition", "Medinan", 12, 560, 28),
        SurahMeta(67, "الملك", "Al-Mulk", "The Sovereignty", "Meccan", 30, 562, 29),
        SurahMeta(68, "القلم", "Al-Qalam", "The Pen", "Meccan", 52, 564, 29),
        SurahMeta(69, "الحاقة", "Al-Haaqqa", "The Reality", "Meccan", 52, 566, 29),
        SurahMeta(70, "المعارج", "Al-Ma'aarij", "The Ascending Stairways", "Meccan", 44, 568, 29),
        SurahMeta(71, "نوح", "Nooh", "Noah", "Meccan", 28, 570, 29),
        SurahMeta(72, "الجن", "Al-Jinn", "The Jinn", "Meccan", 28, 572, 29),
        SurahMeta(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", "Meccan", 20, 574, 29),
        SurahMeta(74, "المدثر", "Al-Muddaththir", "The Cloaked One", "Meccan", 56, 575, 29),
        SurahMeta(75, "القيامة", "Al-Qiyaama", "The Resurrection", "Meccan", 40, 577, 29),
        SurahMeta(76, "الإنسان", "Al-Insaan", "Man", "Medinan", 31, 578, 29),
        SurahMeta(77, "المرسلات", "Al-Mursalaat", "The Emissaries", "Meccan", 50, 580, 29),
        SurahMeta(78, "النبأ", "An-Naba", "The Tidings", "Meccan", 40, 582, 30),
        SurahMeta(79, "النازعات", "An-Naazi'aat", "Those Who Drag Forth", "Meccan", 46, 583, 30),
        SurahMeta(80, "عبس", "Abasa", "He Frowned", "Meccan", 42, 585, 30),
        SurahMeta(81, "التكوير", "At-Takwir", "The Overthrowing", "Meccan", 29, 586, 30),
        SurahMeta(82, "الانفطار", "Al-Infitaar", "The Cleaving", "Meccan", 19, 587, 30),
        SurahMeta(83, "المطففين", "Al-Mutaffifin", "Defrauding", "Meccan", 36, 587, 30),
        SurahMeta(84, "الانشقاق", "Al-Inshiqaaq", "The Splitting Open", "Meccan", 25, 589, 30),
        SurahMeta(85, "البروج", "Al-Burooj", "The Constellations", "Meccan", 22, 590, 30),
        SurahMeta(86, "الطارق", "At-Taariq", "The Morning Star", "Meccan", 17, 591, 30),
        SurahMeta(87, "الأعلى", "Al-A'laa", "The Most High", "Meccan", 19, 591, 30),
        SurahMeta(88, "الغاشية", "Al-Ghaashiya", "The Overwhelming", "Meccan", 26, 592, 30),
        SurahMeta(89, "الفجر", "Al-Fajr", "The Dawn", "Meccan", 30, 593, 30),
        SurahMeta(90, "البلد", "Al-Balad", "The City", "Meccan", 20, 594, 30),
        SurahMeta(91, "الشمس", "Ash-Shams", "The Sun", "Meccan", 15, 595, 30),
        SurahMeta(92, "الليل", "Al-Layl", "The Night", "Meccan", 21, 595, 30),
        SurahMeta(93, "الضحى", "Ad-Dhuhaa", "The Morning Hours", "Meccan", 11, 596, 30),
        SurahMeta(94, "الشرح", "Ash-Sharh", "The Relief", "Meccan", 8, 596, 30),
        SurahMeta(95, "التين", "At-Tin", "The Fig", "Meccan", 8, 597, 30),
        SurahMeta(96, "العلق", "Al-Alaq", "The Clot", "Meccan", 19, 597, 30),
        SurahMeta(97, "القدر", "Al-Qadr", "The Power", "Meccan", 5, 598, 30),
        SurahMeta(98, "البينة", "Al-Bayyina", "The Clear Proof", "Medinan", 8, 598, 30),
        SurahMeta(99, "الزلزلة", "Az-Zalzala", "The Earthquake", "Medinan", 8, 599, 30),
        SurahMeta(100, "العاديات", "Al-Aadiyaat", "The Courser", "Meccan", 11, 599, 30),
        SurahMeta(101, "القارعة", "Al-Qaari'a", "The Calamity", "Meccan", 11, 600, 30),
        SurahMeta(102, "التكاثر", "At-Takaathur", "Rivalry in World Increase", "Meccan", 8, 600, 30),
        SurahMeta(103, "العصر", "Al-Asr", "The Declining Day", "Meccan", 3, 601, 30),
        SurahMeta(104, "الهمزة", "Al-Humaza", "The Traducer", "Meccan", 9, 601, 30),
        SurahMeta(105, "الفيل", "Al-Fil", "The Elephant", "Meccan", 5, 601, 30),
        SurahMeta(106, "قريش", "Quraish", "Quraysh", "Meccan", 4, 602, 30),
        SurahMeta(107, "الماعون", "Al-Maa'oon", "Small Kindnesses", "Meccan", 7, 602, 30),
        SurahMeta(108, "الكوثر", "Al-Kawthar", "Abundance", "Meccan", 3, 602, 30),
        SurahMeta(109, "الكافرون", "Al-Kaafiroon", "The Disbelievers", "Meccan", 6, 603, 30),
        SurahMeta(110, "النصر", "An-Nasr", "The Divine Support", "Medinan", 3, 603, 30),
        SurahMeta(111, "المسد", "Al-Masad", "The Palm Fibre", "Meccan", 5, 603, 30),
        SurahMeta(112, "الإخلاص", "Al-Ikhlaas", "The Sincerity", "Meccan", 4, 604, 30),
        SurahMeta(113, "الفلق", "Al-Falaq", "The Daybreak", "Meccan", 5, 604, 30),
        SurahMeta(114, "الناس", "An-Naas", "Mankind", "Meccan", 6, 604, 30)
    )

    // Curated Ayat for offline rendering and browsing
    val POPULAR_SURAHS_AYAT: Map<Int, List<AyahData>> = mapOf(
        1 to listOf(
            AyahData(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001001.mp3", "ابتدأ بها كتاب الله تعالى مستعيناً بالاسم الأعظم."),
            AyahData(2, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001002.mp3", "الثناء والشكر الخالص لله رب كل شيء وخالقه."),
            AyahData(3, 3, "الرَّحْمَٰنِ الرَّحِيمِ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001003.mp3", "ذو الرحمة الواسعة لجميع خلقه والرحيم بالمؤمنين."),
            AyahData(4, 4, "مَالِكِ يَوْمِ الدِّينِ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001004.mp3", "المتصرف وحده في يوم القيامة والجزاء والحساب."),
            AyahData(5, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001005.mp3", "نخصك وحدك بالعبادة ونستعين بك وحدك في سائر أمورنا."),
            AyahData(6, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001006.mp3", "دلنا وأرشدنا وثبتنا على طريق الحق الواضح."),
            AyahData(7, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", 1, 1, 1, 1, "https://everyayah.com/data/Alafasy_128kbps/001007.mp3", "طريق النبيين والصديقين والشهداء والصالحين.")
        ),
        67 to listOf(
            AyahData(1, 5242, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", 29, 562, 57, 67, "https://everyayah.com/data/Alafasy_128kbps/067001.mp3", "تعاظم وكثر خير الله المتصرف في الكون كله."),
            AyahData(2, 5243, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", 29, 562, 57, 67, "https://everyayah.com/data/Alafasy_128kbps/067002.mp3", "ليختبركم أيكم أخلص وأصوب عملاً."),
            AyahData(3, 5244, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَّا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِن تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِن فُطُورٍ", 29, 562, 57, 67, "https://everyayah.com/data/Alafasy_128kbps/067003.mp3", "خلق السماوات متقنة بديعة لا خلل فيها ولا نقص.")
        ),
        112 to listOf(
            AyahData(1, 6222, "قُلْ هُوَ اللَّهُ أَحَدٌ", 30, 604, 60, 112, "https://everyayah.com/data/Alafasy_128kbps/112001.mp3", "قل أيها الرسول: هو الله المنفرد بالألوهية والربوبية والأسماء والصفات."),
            AyahData(2, 6223, "اللَّهُ الصَّمَدُ", 30, 604, 60, 112, "https://everyayah.com/data/Alafasy_128kbps/112002.mp3", "السيد الذي تصمد وتقصد إليه الخلائق في جميع حوائجها."),
            AyahData(3, 6224, "لَمْ يَلِدْ وَلَمْ يُولَدْ", 30, 604, 60, 112, "https://everyayah.com/data/Alafasy_128kbps/112003.mp3", "تنزه وتقدس عن الولد والوالد والكفء."),
            AyahData(4, 6225, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", 30, 604, 60, 112, "https://everyayah.com/data/Alafasy_128kbps/112004.mp3", "ليس له مماثل ولا نظير في ذاته أو صفاته أو أفعاله.")
        ),
        113 to listOf(
            AyahData(1, 6226, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", 30, 604, 60, 113, "https://everyayah.com/data/Alafasy_128kbps/113001.mp3", "أعتصم وأتحصن برب الصبح ونور الفجر."),
            AyahData(2, 6227, "مِن شَرِّ مَا خَلَقَ", 30, 604, 60, 113, "https://everyayah.com/data/Alafasy_128kbps/113002.mp3", "من شر كل مخلوق فيه شر وأذى."),
            AyahData(3, 6228, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", 30, 604, 60, 113, "https://everyayah.com/data/Alafasy_128kbps/113003.mp3", "من شر الليل إذا دخل بظلامه."),
            AyahData(4, 6229, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", 30, 604, 60, 113, "https://everyayah.com/data/Alafasy_128kbps/113004.mp3", "من شر السواحر اللاتي ينفثن في العقد للإضرار."),
            AyahData(5, 6230, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", 30, 604, 60, 113, "https://everyayah.com/data/Alafasy_128kbps/113005.mp3", "من شر من يتمنى زوال النعمة عن غيره.")
        ),
        114 to listOf(
            AyahData(1, 6231, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114001.mp3", "ألتجئ وأعتصم بخالق البشر ومربيهم."),
            AyahData(2, 6232, "مَلِكِ النَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114002.mp3", "المالك المتصرف في شؤونهم وسائر أمورهم."),
            AyahData(3, 6233, "إِلَٰهِ النَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114003.mp3", "معبودهم الحق الذي لا إله غيره."),
            AyahData(4, 6234, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114004.mp3", "من شر الشيطان الذي يوسوس عند الغفلة ويخنس عند ذكر الله."),
            AyahData(5, 6235, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114005.mp3", "يبث الشكوك والشرور والأوهام في الصدور."),
            AyahData(6, 6236, "مِنَ الْجِنَّةِ وَالنَّاسِ", 30, 604, 60, 114, "https://everyayah.com/data/Alafasy_128kbps/114006.mp3", "سواء كان الوسواس من شياطين الإنس أو الجن.")
        )
    )

    fun getAyahsForSurah(surahNumber: Int): List<AyahData> {
        val direct = POPULAR_SURAHS_AYAT[surahNumber]
        if (direct != null) return direct

        val meta = SURAHS_LIST.find { it.number == surahNumber } ?: return emptyList()
        // Generate placeholder ayat for browsing if not pre-seeded
        return (1..meta.numberOfAyahs).map { ayahNum ->
            AyahData(
                numberInSurah = ayahNum,
                numberInQuran = meta.number * 100 + ayahNum,
                text = "آية مباركة رقم $ayahNum من سورة ${meta.name}",
                juz = meta.juz,
                page = meta.startPage,
                hizbQuarter = (meta.juz * 2),
                surahNumber = meta.number,
                audioUrl = "https://everyayah.com/data/Alafasy_128kbps/${String.format("%03d%03d", meta.number, ayahNum)}.mp3",
                tafseer = "تفسير الآية الكريمة $ayahNum من سورة ${meta.name}."
            )
        }
    }
}
