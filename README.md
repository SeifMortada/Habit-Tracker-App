# Habit Tracker App

## Overview
The Habit Tracker app is an Android application designed to help users track their daily habits. Users can add habits, mark them as completed for specific days, and review their past progress. The app follows Clean Architecture principles and is built using Kotlin, Room Database, Flows, and XML for the UI.

## Features
### Key Features
- **Add Habits**: Users can add habits they want to track.
- **Select Any Day**: Users can pick any date to view and manage habits.
- **Track Progress**: Users can mark habits as completed for a specific day.
- **View History**: Users can see past days when a habit was completed.
- **Smooth UI**: Built with XML to ensure a clean and user-friendly experience.

## Tech Stack
### Technologies Used
- **Kotlin**: The primary programming language.
- **Room Database**: For local data storage and persistence.
- **Flows**: Used for handling asynchronous data streams efficiently.
- **XML (UI)**: For designing the user interface.
- **Clean Architecture**: Ensures modularity and maintainability.

## Architecture
The app follows the Clean Architecture pattern with an MVVM (Model-View-ViewModel) approach:
- **UI Layer**: XML-based views interacting with ViewModel.
- **Domain Layer**: Contains abstract repository and business logic.
- **Data Layer**: Handles Room Database operations and repository pattern.
