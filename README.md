# Customer Service Booking App

A modern Android application built with Jetpack Compose that allows users to discover, book, and manage professional services.

# Key Features

*   Service List: Browse available services with real-time search, filtering, and pull-to-refresh.
*   Service Detailed View: View service descriptions, providers, ratings, and select preferred dates and time slots.
*   Booking System: Streamlined booking process with form validation and real-time conflict handling.
*   Booking Management: Track bookings in the "My Bookings" section with status badges and detailed history.
*   Mock Integration: Fully functional mock API simulating network latency and edge cases.

# Tech Stack

*   Language: Kotlin
*   UI: Jetpack Compose
*   Architecture: MVVM (ViewModel, StateFlow)
*   Navigation: Compose Navigation
*   Asynchrony: Coroutines
*   Dependency Injection: Manual DI (Simulated)

# Documentation

*   [Setup Guide](docs/setup.md)
*   [Architecture Overview](docs/architecture.md)
*   [API Contract](docs/api-contract.md)
*   [Architecture Decisions (ADR)](docs/decisions.md)

# Testing Mock Scenarios

The app uses a `MockApiService` to simulate various real-world scenarios to test various states.