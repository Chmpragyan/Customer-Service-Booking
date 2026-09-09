# Architecture Decision Records (ADR)

This document records the key technical decisions made during the development of the Customer Service Booking app.

## 1. MVVM Architecture & Repository Pattern
- **Decision**: Use MVVM with a Repository layer.
- **Rationale**: Provides a clear separation of concerns. ViewModels manage UI state using `StateFlow`, while Repositories abstract the data source, making it easy to swap the Mock API for a real network client (like Retrofit) later.

## 2. API-First Development with Mock Data
- **Decision**: Define the API contract (`API_CONTRACT.md`) and implement a `MockApiService` before finalizing the UI.
- **Rationale**: Allows the app to be developed and tested against realistic scenarios (conflicts, errors) without waiting for a backend to be ready.

## 3. Manual Dependency Injection
- **Decision**: Use Manual DI in `MainActivity` instead of a framework like Hilt or Koin for the initial version.
- **Rationale**: Keeps the project lightweight with fewer build-time dependencies. The current scope is manageable with manual wiring.

## 4. UI Framework: Jetpack Compose (Material 3)
- **Decision**: Build the UI entirely with Jetpack Compose.
- **Rationale**: Modern declarative UI approach reduces boilerplate code and ensures the app follows the latest Android design standards.

## 5. Feature-Based Package Structure
- **Decision**: Organize the `presentation/screens/` folder by feature (e.g., `service_list`, `booking_form`) rather than by component type.
- **Rationale**: Improves maintainability by keeping the Screen, ViewModel, and State related to a specific feature in one place.

## 6. Shared Component Strategy
- **Decision**: Extract generic UI elements (Toolbar, Badges, Detail Rows) into a centralized `components` folder.
- **Rationale**: Reduces code duplication and ensures a consistent look and feel throughout the application.
