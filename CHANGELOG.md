# Changelog

All notable changes to this project will be documented in this file atleast once a day (if there are any changes).

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Session History card on Home screen
- Set Type selection for sets
- Monochrome launcher icon on Android 12+
- Allow adding a new exercise directly when it cannot be found in the list
- Clean up empty plans from the Plans screen via confirmation dialog
- Enable predictive back navigation

### Changed
- Removed Bottom navigation bar, added user icon to top bar
- Updated Day Switcher component styling for clarity
- Hide Lifts card when there are no lifts

### Fixed
- Select Plan button alignment
- Prevent unintended translation for Turkish app name
- Session list now shows most recent first
- Lifts card not showing even when lifts existed
- Sets from past sessions not shown when the exercise was removed from the corresponding plan

## [1.3.0] - 2025-01-17

### Added
- Drag text field in "Add Set"
- Double tap to edit "set info"
- History Icon (You can check last week's session if it exists)
- Support for Monochrome icon on Android 12+
- Text animation on Onboarding
- Safer way to delete Sets / Exercises / Plans
- New Font for headings

### Changed
- Targets Android 15
- Onboarding screen
- Default theme for new users
- Sorting of muscle groups chips
- Always save plan on going back
- Color in Profile
- Home Screen and On-boarding Screen
- Some buttons and UI elements

### Fixed
- Save button not visible
- Two `Default` theme in Settings
- Scrolling on `Select Exercise` Sheet
- Performance issues on `Add Set` Sheet
- Weird line in the setting wave
- Crash on deleting plan
- On boarding not completing
- Loads of performance improvements

### Removed
- Gradient in settings

## [1.2.0] - 2024-05-26

### Added
- Support for isometric exercises
- Deleting Sets / Exercises / Plans

### Changed
- Error message height
- Chips type in `Select Exercise`

### Fixed
- Navigation to same page again
- Double back presses
- Swipe gesture on reps and weight text field
- Elements squashing on small screens
- Empty exercises
- Invalid reference
- False reference icon

## [1.1.1] - 2024-05-19

### Fixed
- Navigation from home screen
- Annoying animations on home page
- Plan Edit Page
- Back button on all pages

## [1.1.0] - 2024-05-19

### Added
- New Home Page
- Back button on Exercises Page
- Option to open References from workout page(if added)

### Changed
- Splash Screen Image to reduce dependency on `NonFreeNet`
- Whole Plan card is clickable

### Fixed
- APK dependency tree encryption
- Color of icons on some buttons
- `Zestful` Color Palettes
- Crash when using invalid reference
- UI/UX for Exercises Page
- Some navigation crashes

## [1.0.0] - 2024-05-12

### Added
- Initial Release
