# ToDo App

A modern Android ToDo application built with Jetpack Compose and Clean Architecture. The app helps users manage tasks efficiently with reminders, notifications, search, theme customization, and persistent local storage.

## Features

### Task Management

* Create tasks
* Edit tasks
* Delete tasks
* Mark tasks as completed
* Undo task deletion
* Search tasks

### Organization

* Priority levels (High, Medium, Low)
* Due date support
* Task completion tracking

### Reminders & Notifications

* Schedule task reminders
* WorkManager-based background scheduling
* Notification reminders
* Notification deep linking
* Automatic scroll to the relevant task
* Task highlight animation when opened from a notification

### User Experience

* Material 3 UI
* Jetpack Compose
* Light Theme
* Dark Theme
* System Theme support
* Theme persistence using DataStore
* Swipe-to-delete gesture

## Tech Stack

### UI

* Jetpack Compose
* Material 3
* Navigation 3

### Architecture

* Clean Architecture
* MVVM
* Repository Pattern
* Use Cases

### Dependency Injection

* Hilt

### Local Storage

* Room Database
* DataStore Preferences

### Background Processing

* WorkManager

### Asynchronous Programming

* Kotlin Coroutines
* StateFlow
* Flow

## Architecture

```text
presentation/
│
├── task/
├── navigation/
├── theme/
│
domain/
│
├── model/
├── repository/
├── usecase/
│
data/
│
├── local/
├── repository/
├── reminder/
└── preferences/
```

## Screens

* Task List Screen
* Add/Edit Task Screen
* Settings Screen

## Project Highlights

* Clean Architecture implementation
* Dependency Injection using Hilt
* Offline-first local storage
* Reminder scheduling with WorkManager
* Notification navigation and task highlighting
* Persistent theme preferences using DataStore

## Version History

### v0.8

* Notification navigation
* Auto-scroll to reminder task
* Task highlight animation

### v0.7

* Settings screen
* Theme persistence
* System/Light/Dark mode support

### v0.6

* Hilt Dependency Injection

### v0.5

* Search optimization and code improvements

### v0.4

* WorkManager integration

### v0.3

* Due dates and reminders

### v0.2

* Clean Architecture refactor

### v0.1

* Basic CRUD functionality

## Future Improvements

* Statistics dashboard
* Data export/import
* Backup & restore
* Home screen widgets
* Multi-module architecture
* Cloud synchronization

## Author

Nownitya Sharma

Android Developer
