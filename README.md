GMaps WV
========

Overview
--------
GMaps WV is a WebView wrapper for using Google Maps without exposing your device.

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/us.spotco.maps/)
[<img src="https://hosted.weblate.org/widget/divestos/maps/287x66-grey.png"
     alt="Translation status"
     height="66">](https://hosted.weblate.org/engage/divestos/)

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
- R Raj for the sharing intent support
- Icons: Google/Android/AOSP, License: Apache 2.0, https://google.github.io/material-design-icons/

Translations
------------
- Arabic: jonnysemon
- Bulgarian: trunars
- Chinese (Simplified): Crit
- Croatian: lukapiplica
- Czech: Fjuro
- Estonian: Priit Jõerüüt
- Finnish: huuhaa
- French: cultrarius
- Galician: josé m
- German: thereisnoanderson
- Indonesian: Adrien N
- Italian: Dark Space
- Polish: Eryk Michalak, Marcin Mikołajczak
- Portuguese (Brazil): lucasmz-dev, ruanon
- Russian: maz1lovo, XblateX
- Spanish: Diego Sanguinetti, gallegonovato
- Ukrainian: Fqwe1

Donate
-------
- https://divested.dev/donate
