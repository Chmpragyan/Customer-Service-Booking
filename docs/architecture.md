# Project Architecture - Customer Service Booking

This project follows the **MVVM (Model-View-ViewModel)** architectural pattern, leveraging modern Android libraries and best practices.

## Architecture Layers

### 1. Presentation Layer (`presentation/`)
Responsible for displaying data and handling user interaction. Built entirely with **Jetpack Compose**.
- **Screens**: Each feature has its own package containing the Composable screen and its corresponding **ViewModel**.
- **ViewModels**: Manage UI state using `StateFlow`. They interact with the Repository layer to fetch and update data.
- **Components**: Contains reusable UI elements (e.g., `BookingToolbar`, `StatusBadge`) and the central `AppNavigation` logic.

### 2. Domain Layer (Current State)
*Note: Currently integrated within the model and repository layers.*
- **Models**: Plain Kotlin data classes representing the core business entities (`Service`, `Booking`, `TimeSlot`).

### 3. Data Layer (`data/`)
Handles data operations from various sources.
- **Repository**: Acts as a single source of truth for the ViewModels. It abstracts the data source (API vs Mock) from the rest of the app.
- **Mock API**: A simulated network layer that implements the `ApiInterface` to provide realistic data, latency, and error scenarios.

## Core Technologies & Libraries

- **UI Framework**: Jetpack Compose (Material 3)
- **Asynchronous Work**: Kotlin Coroutines & Flow for non-blocking data streams.
- **Navigation**: Jetpack Compose Navigation for type-safe routing.
- **State Management**: `StateFlow` and `MutableStateFlow` for predictable UI states.

## Folder Structure

```text
com.example.customerservicebooking
├── data
│   ├── mock (Mock API implementation)
│   ├── network (API interfaces)
│   └── repository (Repository implementations)
├── model
│   └── (Data entities / DTOs)
├── presentation
│   ├── components (Shared UI & Navigation)
│   ├── screens
│   │   ├── service_list
│   │   ├── service_detail
│   │   ├── booking
│   │   ├── my_bookings
│   │   └── booking_detail
│   └── theme (Material 3 styling)
└── utils
    └── (Enums & API helper classes)
```

---

## 🔄 Data Flow

1. User interacts with a **Screen** (View).
2. The View calls a method on the **ViewModel**.
3. The ViewModel requests data from the **Repository**.
4. The Repository fetches data from the **Mock API**.
5. Data flows back via **ApiResponseEvent** (Success/Error).
6. ViewModel updates the **UI State** (StateFlow).
7. The Screen observes the state change and **re-composes** itself.
