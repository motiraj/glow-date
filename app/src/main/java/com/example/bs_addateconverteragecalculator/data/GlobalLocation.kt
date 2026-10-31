package com.example.bs_addateconverteragecalculator.data

data class LocationData(
    val name: String,
    val lat: Double,
    val lon: Double,
    val tzOffset: Double // UTC offset in hours
)

object GlobalLocation {
    val locations = listOf(
        // Capital & Metro Cities
        LocationData("Kathmandu, Nepal", 27.7172, 85.3240, 5.75),
        LocationData("Lalitpur (Patan), Nepal", 27.6667, 85.3167, 5.75),
        LocationData("Bhaktapur, Nepal", 27.6710, 85.4298, 5.75),
        LocationData("Pokhara, Kaski, Nepal", 28.2096, 83.9856, 5.75),
        LocationData("Butwal, Rupandehi, Nepal", 27.7006, 83.4484, 5.75),
        LocationData("Siddharthanagar (Bhairahawa), Nepal", 27.5000, 83.4500, 5.75),
        LocationData("Bharatpur, Chitwan, Nepal", 27.6833, 84.4333, 5.75),
        LocationData("Biratnagar, Morang, Nepal", 26.4525, 87.2718, 5.75),
        LocationData("Dharan, Sunsari, Nepal", 26.8125, 87.2833, 5.75),
        LocationData("Itahari, Sunsari, Nepal", 26.6667, 87.2833, 5.75),
        LocationData("Birgunj, Parsa, Nepal", 27.0000, 84.8667, 5.75),
        LocationData("Hetauda, Makwanpur, Nepal", 27.4167, 85.0333, 5.75),
        LocationData("Janakpur, Dhanusha, Nepal", 26.7167, 85.9167, 5.75),
        LocationData("Nepalgunj, Banke, Nepal", 28.0500, 81.6167, 5.75),
        LocationData("Dhangadhi, Kailali, Nepal", 28.6833, 80.6000, 5.75),
        LocationData("Birendranagar, Surkhet, Nepal", 28.6000, 81.6333, 5.75),
        LocationData("Ghorahi, Dang, Nepal", 28.0333, 82.3000, 5.75),
        LocationData("Tulsipur, Dang, Nepal", 28.1333, 82.3000, 5.75),
        LocationData("Mahendranagar, Kanchanpur, Nepal", 28.9667, 80.1833, 5.75),

        // Lumbini & Western Nepal
        LocationData("Tansen, Palpa, Nepal", 27.8667, 83.5500, 5.75),
        LocationData("Tamghas, Gulmi, Nepal", 28.0667, 83.2500, 5.75),
        LocationData("Sandhikharka, Arghakhanchi, Nepal", 27.9667, 83.1333, 5.75),
        LocationData("Taulihawa, Kapilvastu, Nepal", 27.5500, 83.0500, 5.75),
        LocationData("Parasi, Nawalparasi, Nepal", 27.5333, 83.6667, 5.75),
        LocationData("Kawasoti, Nawalpur, Nepal", 27.6500, 84.1333, 5.75),

        // Gandaki Province
        LocationData("Putalibazar, Syangja, Nepal", 28.1000, 83.8667, 5.75),
        LocationData("Damauli, Tanahun, Nepal", 27.9667, 84.2833, 5.75),
        LocationData("Gorkha Bazaar, Nepal", 28.0000, 84.6333, 5.75),
        LocationData("Besisahar, Lamjung, Nepal", 28.2333, 84.3833, 5.75),
        LocationData("Baglung Bazaar, Nepal", 28.2667, 83.6000, 5.75),
        LocationData("Kusma, Parbat, Nepal", 28.2333, 83.6833, 5.75),
        LocationData("Beni, Myagdi, Nepal", 28.3500, 83.5667, 5.75),
        LocationData("Jomsom, Mustang, Nepal", 28.7833, 83.7333, 5.75),
        LocationData("Chame, Manang, Nepal", 28.5500, 84.2333, 5.75),

        // Koshi / Eastern Nepal
        LocationData("Birtamod, Jhapa, Nepal", 26.6333, 87.9833, 5.75),
        LocationData("Bhadrapur, Jhapa, Nepal", 26.5500, 88.0833, 5.75),
        LocationData("Ilam Bazaar, Nepal", 26.9100, 87.9200, 5.75),
        LocationData("Phidim, Panchthar, Nepal", 27.1500, 87.7667, 5.75),
        LocationData("Taplejung Bazaar, Nepal", 27.3500, 87.6667, 5.75),
        LocationData("Dhankuta Bazaar, Nepal", 26.9833, 87.3333, 5.75),
        LocationData("Bhojpur Bazaar, Nepal", 27.1667, 87.0500, 5.75),
        LocationData("Myanglung, Terhathum, Nepal", 27.1333, 87.5333, 5.75),
        LocationData("Khandbari, Sankhuwasabha, Nepal", 27.3667, 87.2167, 5.75),
        LocationData("Salleri, Solukhumbu, Nepal", 27.5000, 86.5833, 5.75),
        LocationData("Diktel, Khotang, Nepal", 27.2000, 86.7833, 5.75),
        LocationData("Okhaldhunga Bazaar, Nepal", 27.3167, 86.5000, 5.75),
        LocationData("Gaighat, Udayapur, Nepal", 26.7833, 86.7000, 5.75),

        // Madhesh Province
        LocationData("Rajbiraj, Saptari, Nepal", 26.5333, 86.7500, 5.75),
        LocationData("Lahan, Siraha, Nepal", 26.7167, 86.4833, 5.75),
        LocationData("Jaleshwar, Mahottari, Nepal", 26.6500, 85.8000, 5.75),
        LocationData("Malangwa, Sarlahi, Nepal", 26.8500, 85.5500, 5.75),
        LocationData("Gaur, Rautahat, Nepal", 26.7667, 85.2833, 5.75),
        LocationData("Kalaiya, Bara, Nepal", 27.0333, 85.0000, 5.75),

        // Bagmati Province
        LocationData("Dhading Besi, Nepal", 27.8500, 84.9000, 5.75),
        LocationData("Bidur, Nuwakot, Nepal", 27.9167, 85.1500, 5.75),
        LocationData("Dhunche, Rasuwa, Nepal", 28.1167, 85.3000, 5.75),
        LocationData("Chautara, Sindhupalchok, Nepal", 27.7833, 85.7167, 5.75),
        LocationData("Dhulikhel, Kavre, Nepal", 27.6167, 85.5500, 5.75),
        LocationData("Charikot, Dolakha, Nepal", 27.6667, 86.0667, 5.75),
        LocationData("Manthali, Ramechhap, Nepal", 27.3833, 86.0500, 5.75),
        LocationData("Kamalamai, Sindhuli, Nepal", 27.2167, 85.9167, 5.75),

        // Karnali & Sudurpashchim
        LocationData("Pyuthan Khalanga, Nepal", 28.1000, 82.8667, 5.75),
        LocationData("Liwang, Rolpa, Nepal", 28.3000, 82.6333, 5.75),
        LocationData("Salyan Khalanga, Nepal", 28.3667, 82.1667, 5.75),
        LocationData("Musikot, Rukum West, Nepal", 28.6333, 82.4667, 5.75),
        LocationData("Dailekh Bazaar, Nepal", 28.8333, 81.7167, 5.75),
        LocationData("Jajarkot Khalanga, Nepal", 28.7000, 82.2000, 5.75),
        LocationData("Dunai, Dolpa, Nepal", 28.9333, 82.9167, 5.75),
        LocationData("Jumla Khalanga, Nepal", 29.2750, 82.1833, 5.75),
        LocationData("Manma, Kalikot, Nepal", 29.1500, 81.6167, 5.75),
        LocationData("Gamgadhi, Mugu, Nepal", 29.5500, 82.1667, 5.75),
        LocationData("Simikot, Humla, Nepal", 29.9667, 81.8167, 5.75),
        LocationData("Martadi, Bajura, Nepal", 29.4500, 81.3000, 5.75),
        LocationData("Chainpur, Bajhang, Nepal", 29.5500, 81.2000, 5.75),
        LocationData("Mangalsen, Achham, Nepal", 29.1333, 81.2667, 5.75),
        LocationData("Silgadhi, Doti, Nepal", 29.2667, 80.9833, 5.75),
        LocationData("Dadeldhura Bazaar, Nepal", 29.3000, 80.5833, 5.75),
        LocationData("Dasharathchand, Baitadi, Nepal", 29.5167, 80.4167, 5.75),
        LocationData("Darchula Khalanga, Nepal", 29.8500, 80.5333, 5.75),

        // Global International Cities
        LocationData("New Delhi, India", 28.6139, 77.2090, 5.5),
        LocationData("Dubai, UAE", 25.2048, 55.2708, 4.0),
        LocationData("Doha, Qatar", 25.2854, 51.5310, 3.0),
        LocationData("Riyadh, Saudi Arabia", 24.7136, 46.6753, 3.0),
        LocationData("London, UK", 51.5074, -0.1278, 0.0),
        LocationData("New York, USA", 40.7128, -74.0060, -5.0),
        LocationData("Los Angeles, USA", 34.0522, -118.2437, -8.0),
        LocationData("Toronto, Canada", 43.6510, -79.3470, -5.0),
        LocationData("Sydney, Australia", -33.8688, 151.2093, 10.0),
        LocationData("Melbourne, Australia", -37.8136, 144.9631, 10.0),
        LocationData("Tokyo, Japan", 35.6762, 139.6503, 9.0),
        LocationData("Seoul, South Korea", 37.5665, 126.9780, 9.0),
        LocationData("Singapore", 1.3521, 103.8198, 8.0),
        LocationData("Kuala Lumpur, Malaysia", 3.1390, 101.6869, 8.0)
    )

    fun getLocationByName(name: String): LocationData {
        return locations.find { it.name == name } ?: locations.first() // Defaults to Kathmandu
    }
}
