# 🎮 TeamMate: Intelligent Team Formation System for University Gaming Club

![Java](https://img.shields.io/badge/Language-Java-orange) ![OOP](https://img.shields.io/badge/Concepts-Object--Oriented--Programming-blue) ![Concurrency](https://img.shields.io/badge/Concurrency-Multi--Threaded-green) ![License](https://img.shields.io/badge/License-Academic-blue)

---

## 📝 Overview

**TeamMate** is a **Java-based, object-oriented application** designed to help a university gaming club **automatically form balanced teams** for tournaments, friendly matches, or inter-university events.  

The system uses **survey data**, **participant preferences**, and **advanced OO design** to create **diverse, well-rounded teams** based on:

- 🎮 Game/Sport type (Valorant, Dota, FIFA, Basketball, Badminton)  
- ⚡ Skill level  
- 🛡 Preferred playing role (Defender, Strategist, etc.)  
- 🧠 Personality traits (Leader, Balanced, Thinker)  

It demonstrates **key OOP concepts**, including:

- Classes & objects with attributes, constructors, and access specifiers  
- Encapsulation, inheritance, polymorphism, and abstraction  
- File I/O, exception handling, and concurrency  
- UML-based design: Class, Sequence, Activity, and Use Case diagrams  

---

## ✨ Features

### 1️⃣ Survey & Input
- Participants complete a **short personality & interest survey**:
  - 5 personality questions  
  - Game preference selection  
  - Preferred role selection  

- Personality classification based on scores:  
  - 🏆 **Leader:** 90–100  
  - ⚖️ **Balanced:** 70–89  
  - 🤔 **Thinker:** 50–69  

### 2️⃣ Team Formation
- Automatically creates **balanced teams** with:  
  - 🎯 Interest diversity (variety of games)  
  - 🛡 Role variety (at least one of each role per team)  
  - 🧩 Mixed personality types for strong team dynamics  

- Uses **multi-threading** to form teams concurrently for faster processing.

### 3️⃣ File Handling
- 📂 Load participants from a CSV file  
- 💾 Save formed teams to `resources/formed_teams.csv`  
- ⚠️ Handles missing/invalid inputs and file errors  

### 4️⃣ Concurrency
- Survey responses are processed concurrently  
- Teams are formed in parallel using **threads**  

---

# 📂 Folder & File Structure
<pre>
TeamMate/
├── resources/
│   └── participants_sample.csv      # Sample input
├── src/
│   ├── BalancedStrategy.java
│   ├── BalancedTeamWorker.java
│   ├── FileHandler.java
│   ├── GameRegistry.java
│   ├── GamingClubSystem.java
│   ├── Interest.java
│   ├── Logger.java
│   ├── MatchingStrategy.java
│   ├── Organizer.java
│   ├── Participant.java
│   ├── Personality.java
│   ├── PersonalityClassifier.java
│   ├── Role.java
│   ├── Survey.java
│   ├── SurveyController.java
│   ├── SurveyResponse.java
│   ├── SurveyWorker.java
│   ├── Team.java
│   ├── TeamMateApp.java              # Main entry point
│   ├── User.java
│   └── Validation.java
└── .gitignore
</pre>

## 🚀 Getting Started

### Prerequisites
- Java 17 or above  
- Java IDE (IntelliJ, Eclipse, VSCode) or command-line compiler  

### Installation
```bash
git clone https://github.com/SeyedRumaiz/gaming-club-system.git
cd gaming-club-system
