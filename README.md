# Glow Date — Nepali Patro, Panchanga & Date Converter
### ग्लोडेट: नेपाली क्यालेन्डर, वैदिक पञ्चाङ्ग, व्यक्तिगत कुण्डली तथा मिति रूपान्तरण

<p align="center">
  <img src="app/src/main/res/drawable/app_logo.png" alt="Glow Date Logo" width="120" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white" alt="Language" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose%20%26%20M3-4285F4?style=flat&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-FF6F00?style=flat" alt="Architecture" />
  <img src="https://img.shields.io/badge/Precision-Swiss%20Ephemeris%20%2F%20Lahiri-orange?style=flat" alt="Vedic Engine" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue?style=flat" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Target%20SDK-36-green?style=flat" alt="Target SDK" />
</p>

---

## 🌟 Overview (परिचय)

**Glow Date (ग्लोडेट)** is an advanced, privacy-first Android application that combines authentic Vedic Astrology, high-precision astronomical calculations, and a comprehensive Nepali Patro into a modern, elegant **Jetpack Compose** interface. 

It provides seamless bidirectional conversion between **Bikram Sambat (BS)** and **Gregorian (AD)** dates, real-time astronomical Panchanga based on the user's geographical latitude and longitude, personalized birth chart (**Janma Kundali**) generation with micro-transit (*Gochara*) analysis, and full coverage of Nepali festivals and auspicious Muhurtas (**वि.सं. २०८३ सालका सम्पूर्ण चाडपर्वहरू सहित**).

---

## ✨ Key Features (मुख्य विशेषताहरू)

### 1. 📅 Nepali Patro & Interactive Calendar (नेपाली पात्रो)
- **Comprehensive Calendar Engine:** Complete monthly calendar views with dual date representations (BS & AD) along with Nepal Sambat (*नेपाल संवत्*).
- **2083 BS Official Festivals & Holidays:** Fully updated with all religious festivals, national holidays, tithis, and auspicious Muhurtas (विवाह, ब्रतबन्ध, अन्नप्राशन, व्यापार प्रारम्भ साइतहरू) across all 12 months.
- **Tithi with Paksha Clarification:** Clear labeling of Shukla Paksha (*पूर्णिमा पक्ष*) and Krishna Paksha (*औंसी पक्ष*) with exact end times and Ghadi-Pala.
- **Day Details Dialog:** Instant access to day-specific astronomical attributes, events, and Shraddha timings.

### 2. ☀️ Authentic Vedic Panchanga (दैनिक पञ्चाङ्ग)
- **Five Limbs of Panchanga:** Live calculation of **Tithi**, **Vara**, **Nakshatra**, **Yoga**, and **Karana** using high-precision astronomical algorithms (Lahiri / Chitrapaksha Ayanamsha).
- **Accurate Sun & Moon Timings:** Precise local sunrise, sunset, and solar midday calculated dynamically for any location worldwide.
- **Vedic Time Systems:** Real-time **Ghadi-Pala (घडी-पला)** tracking and live Panchanga clock.
- **Choghadiya & Hora Schedules:** Accurate calculation of day/night Choghadiya (*अमृत, शुभ, लाभ, चर, काल, रोग, उद्वेग*) and planetary Hora schedules for auspicious decision making.
- **Auspicious & Inauspicious Muhurtas:** Clear indicators for **Abhijit Muhurta**, **Rahu Kaal**, **Yamaghanta**, and **Gulika Kaal**.

### 3. 🔮 Personal Kundali & Horoscope (व्यक्तिगत कुण्डली तथा राशिफल)
- **Birth Chart Generation:** Instant generation of **D-1 Lagna Chart** and **Chandra Chart** based on birth date, time, and geographical location.
- **Vedic Astrological Parameters:** Accurate determination of Lagna, Janma Rashi, Nakshatra Pada, Namakshar, Nadi, Gana, Ishtakaal, Bhayat, and Bhabhog.
- **Vimshottari Mahadasha:** Complete calculation of Janmakaleen planetary periods (दशा भुक्ति).
- **Daily Micro Gochar (सूक्ष्म गोचर):** Real-time planetary transits overlaid against the user's birth chart for personalized daily horoscope insights.
- **Local & Private:** Kundali profiles are saved exclusively on the device with full privacy, toggle controls, and easy management.

### 4. 🔄 Date Converter & Age Calculator (मिति रूपान्तरण र उमेर गणना)
- **Bidirectional Conversion:** Instant and accurate conversion from **BS to AD** and **AD to BS** across the entire 20th and 21st century range.
- **Age Calculator:** Detailed calculation of age in years, months, days, total hours, and next upcoming birthday.
- **Event Countdown:** Live day countdown for upcoming Nepali festivals and personal milestones.

### 5. 📱 Home Screen Widgets (होमस्क्रिन विजेटहरू)
- **Nepali Date & Clock Widget:** Displays current Nepali date, day, and live clock on the home screen.
- **Panchanga & Festival Widget:** Quick at-a-glance view of today's Tithi, sunrise, and upcoming festivals.
- **Daily Rashifal Widget:** Instant daily horoscope updates without opening the app.

### 6. 🔒 100% Privacy-First Architecture
- **Zero Cloud Upload:** All astrological and birth profile data is processed and stored 100% locally on the device (Room Database).
- **No Sign-In Required:** Fully functional offline without requiring an account, phone number, or email.

---

## 🛠️ Tech Stack & Libraries

- **Language:** [Kotlin](https://kotlinlang.org/) (100%)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Asynchronous & Reactive:** Kotlin Coroutines & `StateFlow`
- **Local Storage:** Android Jetpack Room Database & DataStore Preferences
- **Widgets:** Android AppWidget Provider & Jetpack Glance
- **Background Tasks:** Android Jetpack WorkManager & AlarmManager
- **Vedic Ephemeris Engine:** Mathematical trigonometric astronomical algorithms adhering to Swiss Ephemeris / Drik-Ganita standards

---

## 📂 Project Structure

```text
app/src/main/
├── AndroidManifest.xml
├── java/com/example/bs_addateconverteragecalculator/
│   ├── MainActivity.kt               # Main application container & navigation
│   ├── GlowDateApplication.kt       # Application class & initialization
│   ├── calculator/                   # Age calculation logic & models
│   ├── converter/                    # Bidirectional BS <-> AD date converter engine
│   ├── data/                         # Panchanga, Ephemeris, Astrological data & calculations
│   │   ├── BsCalendarData.kt         # Calendar months mapping & solar year data
│   │   ├── HolidayData.kt            # Curated festivals & public holidays (2083 BS updated)
│   │   ├── PanchangaCalculator.kt    # Core Vedic Panchanga astronomical engine
│   │   ├── TithiCalculator.kt        # Exact tithi transitions and paksha calculations
│   │   ├── AstroCalculator.kt        # Planetary longitude, Gochar & Lagna calculation
│   │   ├── CheenaCalculatorEngine.kt # Kundali generation (D-1, Chandra, Dasha, Ishtakaal)
│   │   └── ...
│   ├── ui/                           # Jetpack Compose UI Screens & ViewModels
│   │   ├── panchanga/                # Panchanga dashboard, Choghadiya & Hora widgets
│   │   ├── calendar/                 # Monthly Patro view, day details & festival lists
│   │   ├── rashifal/                 # Daily Rashifal, Kundali charts & personal horoscope
│   │   ├── converter/                # BS-AD Date Converter & Age Calculator screens
│   │   ├── components/               # Reusable Compose components (Charts, AppBars, Dialogs)
│   │   └── theme/                    # Color palette, Typography & Material 3 theming
│   ├── widget/                       # Android Home Screen Widgets
│   └── worker/                       # WorkManager & Alarm receivers for Vedic notifications
└── res/                              # Drawables, layouts, localized strings (en, ne, hi)
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio:** Ladybug (2024.2.1+) or newer
- **JDK:** Version 17 or higher
- **Android SDK:**
  - `minSdk = 24` (Android 7.0 Nougat)
  - `targetSdk = 36` (Android 16 Ready)

### Clone & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/motiraj/glow-date.git
   cd glow-date
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync dependencies.
4. Connect an Android device (or launch an Emulator) with USB Debugging enabled.
5. Click **Run** (`Shift + F10`) or execute:
   ```bash
   ./gradlew installDebug
   ```

---

## 📜 Privacy Policy & Google Play Compliance

Glow Date is designed with strict adherence to Google Play Console User Data Policies.
- [Read Privacy Policy (Markdown)](PRIVACY_POLICY.md)
- [Web-hosted Privacy Policy (HTML)](privacy-policy.html)
- Web URL for Google Play Console: `https://motiraj.github.io/glow-date/`

---

## 👨‍💻 Author & Credits

- **Developer:** Moti Raj Gautam (MotiKrishna)
- **App Name:** Glow Date (`com.glowdate.app`)
- **Location:** Kathmandu, Nepal
- **Email:** [motikrishnag@gmail.com](mailto:motikrishnag@gmail.com)

---

## 📄 License

```text
Copyright (c) 2026 Moti Raj Gautam. All rights reserved.
```
