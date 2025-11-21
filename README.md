# MapleDeathArena

-   RGP Game -- Neo Financial Take-Home Assignment THA

**Candidate:** Adalberto de Oliveira Santos

**Email:** adalbertoliveira@gmail.com

**Github:** https://github.com/AdeOSantos/MapleDeathArena

**Date:** 21 November 2025

**Stack:** Java 21 • Spring Boot 3.3 • Maven • Lombok • OpenAPI •
Actuator

A clean, fast, in-memory RPG character & battle API --- **100%
compliant** with Neo's specification.\
No database. Everything runs in RAM. Thread‑safe operations.
Deterministic tests. Hidden speed re‑rolls. Exact battle log formatting.

## API Usage Examples

### 1. Create Characters

#### Create a Warrior

``` bash
curl -X POST http://localhost:8080/api/characters \
  -H "Content-Type: application/json" \
  -d '{"name":"Strong_War","job":"WARRIOR"}'
```

#### Create a Thief

``` bash
curl -X POST http://localhost:8080/api/characters \
  -H "Content-Type: application/json" \
  -d '{"name":"Quick_Thief","job":"THIEF"}'
```

#### Create a Mage

``` bash
curl -X POST http://localhost:8080/api/characters \
  -H "Content-Type: application/json" \
  -d '{"name":"Magic_User","job":"MAGE"}'
```

### 2. List All Characters

``` bash
curl http://localhost:8080/api/characters
```

### 3. Get Character Details

``` bash
curl http://localhost:8080/api/characters/1
curl http://localhost:8080/api/characters/2
```

### 4. Start a Battle

``` bash
curl -X POST http://localhost:8080/api/characters/battle \
  -H "Content-Type: application/json" \
  -d '{"attackerId":"1","defenderId":"2"}'
```

### Bonus: Full Flow Example

``` bash
curl -X POST http://localhost:8080/api/characters -d '{"name":"CalgaryFlames","job":"WARRIOR"}' -H "Content-Type: application/json"
curl -X POST http://localhost:8080/api/characters -d '{"name":"SnowNinja","job":"THIEF"}' -H "Content-Type: application/json"
curl http://localhost:8080/api/characters
curl -X POST http://localhost:8080/api/characters/battle -d '{"attackerId":"1","defenderId":"2"}' -H "Content-Type: application/json"
```

------------------------------------------------------------------------

## ✅ Features (F1--F4)

| Feature                  | Status | Notes                                                                 |
|--------------------------|--------|-----------------------------------------------------------------------|
| **F1 – Character Creation** | Done   | Name 4–15 chars (letters + underscore), 3 jobs, strict validation      |
| **F2 – List Characters**   | Done   | Shows name, job, alive/dead                                           |
| **F3 – Character Details** | Done   | Full stats, current/max HP, attack & speed modifiers                  |
| **F4 – Battle System**     | Done   | Speed tie → **hidden re-roll**, exact log format, winner keeps HP    |

**Note:** Speed re‑rolls are performed internally but **never printed in logs**, per spec.

---

## 🚀 Quick Start

``` bash
./mvnw spring-boot:run
```

-   **Swagger UI:** http://localhost:8080/swagger-ui.html\
-   **Actuator:** http://localhost:8080/actuator

------------------------------------------------------------------------

## 📡 API Endpoints

| Method | URL                        | Description               |
|--------|-----------------------------|---------------------------|
| POST   | `/api/characters`          | Create new character      |
| GET    | `/api/characters`          | List characters           |
| GET    | `/api/characters/{id}`     | Character details         |
| POST   | `/api/characters/battle`   | Run battle                |

## 🧪 Example Session

``` bash
curl -X POST http://localhost:8080/api/characters -H "Content-Type: application/json" -d '{"name":"MapleWarrior","job":"WARRIOR"}'
curl -X POST http://localhost:8080/api/characters -H "Content-Type: application/json" -d '{"name":"SnowThief","job":"THIEF"}'
curl -X POST http://localhost:8080/api/characters/battle -H "Content-Type: application/json" -d '{"attackerId":"1","defenderId":"2"}'
```

------------------------------------------------------------------------

## 📊 Job Stats & Formulas

| Job     | HP | STR | DEX | INT | Attack Modifier                      | Speed Modifier          |
|---------|----|-----|-----|-----|--------------------------------------|--------------------------|
| Warrior | 20 | 10  | 5   | 5   | 80% STR + 20% DEX                    | 60% DEX + 20% INT        |
| Thief   | 15 | 4   | 10  | 4   | 25% STR + 100% DEX + 25% INT         | 80% DEX                  |
| Mage    | 12 | 5   | 6   | 10  | 20% STR + 20% DEX + 120% INT         | 40% DEX + 10% STR        |



All rolls use:

```
Math.floor(random * modifier)
```

---




## 🔧 Technical Highlights

-   In-memory store with `ConcurrentHashMap`
-   Deterministic tests using fixed Random seed
-   Hidden speed rerolls
-   Global exception handler
-   Swagger + Actuator enabled

------------------------------------------------------------------------

## 🍁 Final Notes

This project is optimized for clarity, correctness, determinism, and
strict adherence to the Neo Financial assignment specification.
