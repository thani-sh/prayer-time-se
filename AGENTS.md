# Prayer Times for Sweden - Developer Documentation

## Introduction

This repository provides Islamic prayer times for Sweden through multiple applications and platforms. The project delivers prayer time information using data scraped from Islamiska Förbundet (islamiskaforbundet.se) for various Swedish cities. The repository includes a core JavaScript library, a web API, a website, and native mobile applications for both iOS and Android platforms.

## Repository Structure

```
prayer-time-se/
├── apps/                     # Application implementations
│   ├── android/              # Android native app (Kotlin + Jetpack Compose)
│   ├── iphone/               # iOS/iPadOS/macOS native app (Swift + SwiftUI)
│   ├── webapi/               # REST API service (Cloudflare Workers)
│   └── website/              # Web application (SvelteKit)
├── libs/                     # Shared libraries
│   ├── prayer-time-se/       # Core JavaScript library for prayer times
│   └── update-times/         # Data synchronization utility
├── data/                     # Prayer time datasets (JSON files)
└── .vscode/                  # VS Code configuration
```

## Libraries and Tools

### Core Libraries

- **`@thani-sh/prayer-time-se`** - Core JavaScript/TypeScript library providing prayer time calculations and data access for Swedish cities

### Web Technologies

- **SvelteKit** - Full-stack framework for the website application
- **Vite** - Build tool and development server
- **Tailwind CSS** - Utility-first CSS framework for styling
- **DaisyUI** - Component library for Tailwind CSS
- **Hono** - Lightweight web framework for the API service
- **Wrangler** - Development and deployment tool for Cloudflare Workers

### Mobile Development

- **Jetpack Compose** - Modern UI toolkit for Android
- **SwiftUI** - Declarative UI framework for iOS/iPadOS/macOS
- **Kotlin** - Primary language for Android development
- **Swift** - Primary language for iOS development

### Data Processing

- **Cheerio** - HTML parsing for data scraping
- **date-fns** - Date utility library
- **Lodash** - JavaScript utility library

### Development Tools

- **TypeScript** - Type-safe JavaScript development
- **Prettier** - Code formatting
- **Vitest** - Unit testing framework

## Web APIs

### External API
- **Islamiska Förbundet (islamiskaforbundet.se)** - Source for prayer time data scraping

### Internal REST API
- **Prayer Times API (v1)** - RESTful API providing prayer time data for Swedish cities, hosted on Cloudflare Workers with R2 bucket storage

## Applications

### Web Application (`apps/website`)

**Technology**: SvelteKit + Tailwind CSS + DaisyUI

**Capabilities**:
- Display daily prayer times for selected Swedish cities
- Show current date in Gregorian and Hijri calendars
- Select prayer time calculation methods
- Select Swedish cities
- Progressive Web App (PWA) with service worker support
- Settings configuration
- Documentation page
- Responsive design for mobile and desktop
- Qibla compass (coming soon)

**Missing Features**:
- Qibla compass (placeholder only)
- 12/24 hour format switch

### iOS/iPadOS/macOS Application (`apps/iphone`)

**Technology**: Swift + SwiftUI

**Capabilities**:
- Display daily prayer times with pageable date navigation
- Hijri calendar display with adjustable offset
- Qibla compass with real-time heading and location services
- Prayer time notifications with configurable offsets
- Prayer time method selection
- City selection for Swedish locations
- Location-based services integration
- Adaptive UI for iPhone, iPad, and Mac
- Dark mode support
- Background notification scheduling

**Missing Features**:
- Home widget
- 12/24 hour format switch

### Android Application (`apps/android`)

**Technology**: Kotlin + Jetpack Compose + Material Design 3

**Capabilities**:
- Display daily prayer times
- Material Design 3 UI
- Navigation with bottom bar
- Background notification scheduling (WorkManager)
- Location services integration (Google Play Services)
- Home widget support (Glance)
- Settings configuration
- Qibla compass
- 12/24 hour format toggle

**Missing Features**:
- None identified

### Web API (`apps/webapi`)

**Technology**: Hono + Cloudflare Workers + R2 Storage

**Capabilities**:
- RESTful API endpoints for prayer times
- List available calculation methods
- List available Swedish cities
- Retrieve prayer times for specific city and date
- Retrieve full year prayer times dataset
- CORS support for cross-origin requests
- ETag caching
- Request logging

**Endpoints**:
- `GET /v1/ping` - Health check
- `GET /v1/version` - API version information
- `GET /v1/methods` - List calculation methods
- `GET /v1/method/:method/cities` - List cities for method
- `GET /v1/method/:method/city/:city/times` - Get full year times
- `GET /v1/method/:method/city/:city/times/:date` - Get times for specific date
