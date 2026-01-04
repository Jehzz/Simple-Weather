# Simple Weather
Simple Weather is a modern Android application built with 100% Kotlin and Jetpack Compose, designed to provide a clean and straightforward weather forecast experience. It features a responsive UI, offline caching, and customizable home screen widgets.
#Features
Current & Forecast Weather: Get the current conditions and a 5-day / 3-hour forecast for any location via ZIP code.
Dynamic UI: The main interface animates based on weather conditions and supports both Metric and Imperial units.
Offline Caching: Forecasts are cached locally for up to 4 hours, reducing network usage and providing quick access to data.
Glance App Widgets:
Includes widgets for current weather, a horizontal forecast, and a theme-aware clock.
Widgets automatically adapt to the device's Light and Dark modes and use a custom color scheme.
# Tech Stack & Architecture
UI: Jetpack Compose & Glance for App Widgets.
Architecture: MVVM (Model-View-ViewModel) .
Asynchronous: Kotlin Coroutines for background operations.
Dependency Injection: Hilt.
Data Persistence: Room for offline caching.
Networking: Retrofit2 & OkHttp.
Image Loading: Coil.
Setup & Build
To build and run the project, you need an API key from OpenWeatherMap.
Clone the repository:
shell
git clone https://github.com/jessosborn/Simple-Weather.git
```

2.  **Get an API Key** from [OpenWeatherMap](https://home.openweathermap.org/users/sign_up).

3.  **Add your API Key:**
    -   Create a file named `apikey.properties` in the project's root directory.
    -   Add your key to the file: