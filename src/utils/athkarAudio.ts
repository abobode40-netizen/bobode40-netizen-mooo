import { ThikrItem, AthkarCategory } from '../types';
import { AudioTrack } from '../contexts/AudioPlayerContext';
import { cleanTextForSpeech } from './speech';

/**
 * Known high-quality recitations for Quranic athkar
 */
const KNOWN_QURAN_ATHKAR_AUDIO: Record<string, string> = {
  // Ayat al-Kursi (2:255)
  m_ayat_kursi: 'https://everyayah.com/data/Alafasy_128kbps/002255.mp3',
  e_ayat_kursi: 'https://everyayah.com/data/Alafasy_128kbps/002255.mp3',
  sleep_ayat_kursi: 'https://everyayah.com/data/Alafasy_128kbps/002255.mp3',

  // Al-Ikhlas & Mu'awwidhat (Surahs 112, 113, 114)
  m_ikhlas_muawwidhat: 'https://server8.mp3quran.net/afs/112.mp3',
  e_ikhlas_muawwidhat: 'https://server8.mp3quran.net/afs/112.mp3',
  sleep_muawwidhat: 'https://server8.mp3quran.net/afs/112.mp3',

  // Khawateem Al-Baqarah (2:285-286)
  sleep_baqarah_end: 'https://everyayah.com/data/Alafasy_128kbps/002285.mp3',
  e_baqarah_end: 'https://everyayah.com/data/Alafasy_128kbps/002285.mp3',

  // Surah Al-Mulk (67)
  sleep_mulk: 'https://server8.mp3quran.net/afs/067.mp3',

  // Surah Al-Sajdah (32)
  sleep_sajdah: 'https://server8.mp3quran.net/afs/032.mp3',

  // Surah Al-Isra 110-111
  wake_last_isra: 'https://everyayah.com/data/Alafasy_128kbps/017110.mp3'
};

/**
 * Extracts a concise, dignified title for a thikr
 */
export function getThikrTitle(thikr: ThikrItem, categoryTitle?: string): string {
  const text = thikr.text.trim();

  if (thikr.id.includes('ayat_kursi') || text.includes('اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ')) {
    return 'آية الكرسي';
  }
  if (thikr.id.includes('ikhlas_muawwidhat') || text.includes('قُلْ هُوَ اللَّهُ أَحَدٌ')) {
    return 'سورة الإخلاص والمعوذتان';
  }
  if (thikr.id.includes('sayyid_istighfar') || text.includes('اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي')) {
    return 'سيد الاستغفار';
  }
  if (text.includes('أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ')) {
    return 'أصبحنا وأصبح الملك لله';
  }
  if (text.includes('أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ')) {
    return 'أمسينا وأمسى الملك لله';
  }
  if (text.includes('رَضِيتُ بِاللَّهِ رَبًّا')) {
    return 'رضيت بالله رباً وبالإسلام ديناً';
  }
  if (text.includes('بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ')) {
    return 'بسم الله الذي لا يضر مع اسمه شيء';
  }
  if (text.includes('سُبْحَانَ اللَّهِ وَبِحَمْدِهِ')) {
    return 'سبحان الله وبحمده عدد خلقه';
  }
  if (text.includes('يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ')) {
    return 'يا حي يا قيوم برحمتك أستغيث';
  }
  if (text.includes('اللَّهُمَّ بِكَ أَصْبَحْنَا')) {
    return 'اللهم بك أصبحنا وبك أمسينا';
  }
  if (text.includes('اللَّهُمَّ بِكَ أَمْسَيْنَا')) {
    return 'اللهم بك أمسينا وبك أصبحنا';
  }
  if (text.includes('أَصْبَحْنَا عَلَى فِطْرَةِ الْإِسْلَامِ')) {
    return 'أصبحنا على فطرة الإسلام';
  }
  if (text.includes('أَمْسَيْنَا عَلَى فِطْرَةِ الْإِسْلَامِ')) {
    return 'أمسينا على فطرة الإسلام';
  }
  if (text.includes('اللَّهُمَّ عَافِنِي فِي بَدَنِي')) {
    return 'اللهم عافني في بدني وسمعي وبصري';
  }
  if (text.includes('اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ')) {
    return 'اللهم إني أسألك العفو والعافية';
  }
  if (text.includes('حَسْبِيَ اللَّهُ لَا إِلَهَ إِلَّا هُوَ')) {
    return 'حسبي الله لا إله إلا هو عليه توكلت';
  }
  if (text.includes('لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ')) {
    return 'التهليل والتوحيد';
  }
  if (text.includes('أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ')) {
    return 'الاستغفار والتوبة';
  }
  if (text.includes('آمَنَ الرَّسُولُ بِمَا أُنْزِلَ إِلَيْهِ')) {
    return 'خواتيم سورة البقرة';
  }

  // Fallback: extract first 4-6 words cleanly
  const words = cleanTextForSpeech(text).split(/\s+/).filter(Boolean);
  if (words.length > 0) {
    const preview = words.slice(0, 5).join(' ');
    return preview.length > 35 ? preview.slice(0, 32) + '...' : preview;
  }

  return categoryTitle ? `ذكر من ${categoryTitle}` : 'ذكر مبارك';
}

/**
 * Splits Arabic text into small natural sentences/segments for reliable recitation
 */
function splitArabicTextForSpeech(text: string, maxChunkLength = 150): string[] {
  const cleaned = cleanTextForSpeech(text);
  if (!cleaned) return [];

  // Split by common Arabic delimiters (comma, semicolon, period, newline)
  const rawParts = cleaned.split(/([،؛.\n]+)/);
  const chunks: string[] = [];
  let current = '';

  for (let i = 0; i < rawParts.length; i++) {
    const part = rawParts[i].trim();
    if (!part) continue;

    if ((current + ' ' + part).length <= maxChunkLength) {
      current = current ? `${current} ${part}` : part;
    } else {
      if (current) chunks.push(current);
      if (part.length <= maxChunkLength) {
        current = part;
      } else {
        // Fallback: word by word split
        const words = part.split(/\s+/);
        let sub = '';
        for (const w of words) {
          if ((sub + ' ' + w).length <= maxChunkLength) {
            sub = sub ? `${sub} ${w}` : w;
          } else {
            if (sub) chunks.push(sub);
            sub = w;
          }
        }
        current = sub;
      }
    }
  }

  if (current) chunks.push(current);
  return chunks.length > 0 ? chunks : [cleaned.slice(0, maxChunkLength)];
}

/**
 * Builds high-quality audio recitation URLs for a given thikr
 */
export function getThikrAudioUrls(thikr: ThikrItem): string[] {
  // 1. If explicit known recitation exists
  if (KNOWN_QURAN_ATHKAR_AUDIO[thikr.id]) {
    return [KNOWN_QURAN_ATHKAR_AUDIO[thikr.id]];
  }

  // 2. If relatedVerse audioUrl exists
  if (thikr.relatedVerse?.audioUrl) {
    return [thikr.relatedVerse.audioUrl];
  }

  // 3. Generate high-clarity Arabic TTS audio segments
  const chunks = splitArabicTextForSpeech(thikr.text, 140);
  return chunks.map(chunk => 
    `https://translate.google.com/translate_tts?ie=UTF-8&tl=ar&client=tw-ob&q=${encodeURIComponent(chunk)}`
  );
}

/**
 * Creates an AudioTrack ready for the Global Audio Player
 */
export function createThikrAudioTrack(thikr: ThikrItem, categoryTitle?: string): AudioTrack {
  const title = getThikrTitle(thikr, categoryTitle);
  const subtitle = thikr.source ? `${thikr.source}` : (categoryTitle || 'حصن المسلم');
  const urls = getThikrAudioUrls(thikr);

  return {
    id: `thikr-${thikr.id}`,
    title,
    subtitle: `${subtitle} • تكرار ${thikr.repeatCount} مرات`,
    url: urls[0] || '',
    urls,
    imageUrl: '/jannat-icon.svg'
  };
}
