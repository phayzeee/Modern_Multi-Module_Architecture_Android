# 🏗️ Modern Android Multi-Module Architecture

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AGP-8.10.0-green.svg)](https://developer.android.com/studio/releases/gradle-plugin)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-orange.svg)](https://developer.android.com/about/versions/nougat)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A production-ready Android application showcasing **Clean Architecture**, **MVI pattern**, and **modern development practices** with a fully modularized structure.

## 🎯 What Makes This Special?

This isn't just another demo app. It's a **senior-level reference architecture** that demonstrates:

- ✅ **7 modules** organized for maximum scalability
- ✅ **Offline-first** architecture with Room database
- ✅ **MVI pattern** for predictable state management
- ✅ **Zero God classes** - every file has a single responsibility
- ✅ **Production-ready** error handling and edge cases
- ✅ **Type-safe** navigation with Compose
- ✅ **100% Kotlin** with modern coroutines and Flow

Perfect for developers who want to understand how to build **real-world, maintainable Android apps**.

---

## 🏛️ Architecture

### The Big Picture

```
┌─────────────────────────────────────────────┐
│              Presentation Layer              │
│   (UI - Jetpack Compose + ViewModels)       │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│               Domain Layer                   │
│   (Business Logic - Use Cases + Models)     │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│                Data Layer                    │
│   (Repositories - API + Database)           │
└─────────────────────────────────────────────┘
```

### Module Structure

```
📦 app                    → Application entry point
📦 core
 ├─ common               → Shared utilities (Result, UseCase)
 ├─ ui                   → Reusable UI components & theme
 ├─ network              → Retrofit, API interfaces
 └─ database             → Room database, DAOs
📦 feature
 ├─ home                 → Product list screen
 └─ details              → Product details screen
```

**Why this structure?**
- ⚡ Features are **completely independent** - add/remove without touching others
- 🔄 Core modules are **reusable** across features
- 🧪 Each module is **testable in isolation**
- 📦 Build times improve with **parallel compilation**

---

## 🔥 Key Features

### 🎨 Modern UI
- **Jetpack Compose** for declarative UI
- **Material 3** design system
- **Dark mode** support
- Smooth animations and transitions

### 🧠 Smart Architecture
- **MVI Pattern** - Unidirectional data flow
- **Clean Architecture** - Separation of concerns
- **Offline-First** - Works without internet
- **Reactive** - Auto-updating UI with Flow

### 🛠️ Developer Experience
- **Hilt** for dependency injection
- **Type-safe navigation** with Compose Navigation
- **Version catalogs** for dependency management
- **KSP** for faster builds (vs KAPT)

### 📊 Data Flow

```kotlin
User Action
    ↓
Intent (sealed class)
    ↓
ViewModel processes
    ↓
Use Case executes
    ↓
Repository coordinates
    ↓
API/Database ← Offline-first!
    ↓
State updates
    ↓
UI recomposes automatically ✨
```

---

## 🚀 Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Kotlin 2.0.21 |
| **UI** | Jetpack Compose, Material 3 |
| **Architecture** | Clean Architecture, MVI |
| **DI** | Hilt |
| **Networking** | Retrofit, OkHttp, Moshi |
| **Database** | Room |
| **Async** | Coroutines, Flow |
| **Navigation** | Compose Navigation |
| **Image Loading** | Coil |
| **Build** | KSP, Version Catalogs |

---

## 📱 Features Walkthrough

### Home Screen
- 📋 **Product List** - Displays all products with images
- 🔍 **Search** - Real-time filtering (no API calls!)
- 🔄 **Pull-to-Refresh** - Fetch fresh data
- 💾 **Offline Mode** - Cached data displayed instantly
- ⚡ **Loading States** - Skeleton screens, shimmer effects
- ❌ **Error Handling** - Retry functionality

### Details Screen  
- 🖼️ **Full-Screen Image** - High-quality product images
- 💰 **Pricing Info** - Clear, formatted pricing
- ⭐ **Ratings** - User ratings and reviews count
- 📝 **Description** - Full product details
- 🔙 **Navigation** - Smooth back transitions

---

## 🎓 What You'll Learn

By studying this project, you'll understand:

### Architecture Patterns
- ✅ How to structure a **multi-module app**
- ✅ Implementing **Clean Architecture** in Android
- ✅ **MVI pattern** vs MVVM (and why MVI wins)
- ✅ **Offline-first** architecture strategies

### Android Best Practices
- ✅ **Dependency injection** with Hilt
- ✅ **Repository pattern** for data management
- ✅ **Use Cases** for business logic
- ✅ **Mappers** between layers (DTO → Entity → Domain)

### Modern Development
- ✅ **Jetpack Compose** best practices
- ✅ **Kotlin Coroutines** and **Flow**
- ✅ **Type-safe navigation**
- ✅ **State management** with StateFlow

---

## 📂 Project Structure

```
├── app/                          # Application module
│   ├── ModernArchApp.kt         # @HiltAndroidApp
│   ├── MainActivity.kt          # Entry point
│   └── navigation/              # Navigation graph
│
├── core/
│   ├── common/                  # Shared utilities
│   │   ├── result/Result.kt    # Type-safe error handling
│   │   ├── base/UseCase.kt     # Base use case classes
│   │   └── extensions/         # Kotlin extensions
│   │
│   ├── ui/                      # UI components
│   │   ├── theme/              # Material 3 theme
│   │   └── components/         # Reusable composables
│   │
│   ├── network/                 # Networking
│   │   ├── api/                # Retrofit interfaces
│   │   ├── model/              # DTOs
│   │   └── di/                 # Network DI module
│   │
│   └── database/                # Local storage
│       ├── dao/                # Room DAOs
│       ├── entity/             # Database entities
│       └── di/                 # Database DI module
│
└── feature/
    ├── home/                    # Home feature
    │   ├── domain/             # Business logic
    │   ├── data/               # Repository impl
    │   ├── HomeScreen.kt       # UI
    │   ├── HomeViewModel.kt    # State management
    │   └── HomeContract.kt     # MVI contract
    │
    └── details/                 # Details feature
        └── (same structure)
```

---

## 🎯 MVI Pattern Explained

### The Contract
```kotlin
// State - What the UI looks like
data class State(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Intent - What the user can do
sealed interface Intent {
    object Refresh : Intent
    data class ProductClicked(val id: Int) : Intent
}

// Effect - One-time events
sealed interface Effect {
    data class NavigateToDetails(val id: Int) : Effect
    data class ShowToast(val message: String) : Effect
}
```

### The Flow
```kotlin
// 1. User pulls to refresh
viewModel.handleIntent(Intent.Refresh)

// 2. ViewModel processes
_state.update { it.copy(isRefreshing = true) }
refreshProductsUseCase()

// 3. Data updates
repository.refreshProducts()

// 4. State emits
_state.update { it.copy(products = newData, isRefreshing = false) }

// 5. UI automatically recomposes! ✨
```

---

## 🔄 Offline-First Strategy

```kotlin
// 1. Read from database immediately (fast!)
database.observeProducts()
    .collect { cachedProducts ->
        show(cachedProducts) // Instant display
    }

// 2. Fetch from API in background
launch {
    val freshProducts = api.getProducts()
    database.save(freshProducts) // Update cache
}

// 3. Database observer emits new data
// 4. UI updates automatically
```

**Benefits:**
- ⚡ Instant data display
- 📱 Works offline
- 🔄 Always up-to-date
- 🎯 Single source of truth

---

## 🚧 Roadmap

- [ ] Unit tests (ViewModels, UseCases)
- [ ] UI tests (Compose testing)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Add to cart functionality
- [ ] User authentication
- [ ] Favorites/Wishlist
- [ ] Categories filter
- [ ] Sort & filter options
- [ ] Pagination
- [ ] Share product feature

---

## 📄 License

```
MIT License

Copyright (c) 2026 [Your Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---
