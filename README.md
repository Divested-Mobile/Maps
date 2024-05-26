GMaps WV
========

Overview
--------
GMaps WV is a WebView wrapper for using Google Maps without exposing your device.

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/us.spotco.maps/)

Features
--------
- Clears private data on close
- Blocks access to Google trackers and other third-party resources
- Restricts all network requests to HTTPS
- Allows toggling of location permission

Downsides
---------
- Navigation is not available, only turn-by-turn direction list
- WebRTC isn't blocked due to WebView limitations
- Cache isn't cleared due to resource/data considerations, however could allow tracking without other data (cookies)
  - Manually clear app cache if necessary, may be addressed in future

Credits
-------
- @woheller69 for discovering that page loaded resources weren't being blocked
- @woheller69 for adding proper location support
- @woheller69 for adding location sharing to other map apps
- @woheller69 for disabling WebView telemetry
- Diego Sanguinetti for the Spanish fastlane metadata
- Marcin Mikołajczak for the Polish fastlane metadata
- @ruanon for the Brazilian Portuguese fastlane metadata
- @lucasmz for the Portuguese fastlane metadata
- @lucasmz for Portuguese translation
- @gallegonovato for the Spanish translation
- R Raj for the sharing intent support
- Icons: Google/Android/AOSP, License: Apache 2.0, https://google.github.io/material-design-icons/

Donate
-------
- https://divested.dev/donate
