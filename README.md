# Scheme Eligibility Checker

A clean, extensible Spring Boot 3 REST API that matches Indian citizens with central and state government welfare schemes based on their demographic profile.

## Architecture Explanation

The project follows a strict 3-tier layered architecture combined with the **Strategy** and **Factory** design patterns for rule evaluation:

1. **Controller Layer (`controller`)**: Handles HTTP requests, triggers `@Valid` input validation, and serializes responses (`EligibilityController`, `SchemeController`).
2. **Service Layer (`service`)**: Contains the core orchestration logic (`SchemeService`). It fetches schemes from the repository, delegates rule instantiation to `RuleFactory`, and ensures a scheme is returned **only if all of its rules pass** (`Stream.allMatch`).
3. **Repository Layer (`repository`)**: Uses Spring Data JPA (`SchemeRepository`) to persist and load `Scheme` and `SchemeRule` entities from H2 (or PostgreSQL).
4. **Rule Engine (`rule`)**:
   - `Rule` (Strategy Interface): Declares `boolean isSatisfiedBy(UserProfile user)`.
   - Concrete Strategies (`AgeRule`, `IncomeRule`, `StateRule`, `CategoryRule`, `OccupationRule`): Encapsulate field-specific parsing, fail-fast constructor validation, and operator evaluation (`GTE`, `LTE`, `EQ`, `IN`, `ALL`).
   - `RuleFactory`: Maps database `SchemeRule` rows (`field`, `operator`, `value`) into executable `Rule` objects using a functional registry map.
5. **Centralized Exception Handling (`exception`)**:
   - `GlobalExceptionHandler` intercepts validation failures (`MethodArgumentNotValidException`), not found resources (`ResourceNotFoundException`), and bad input formats (`HttpMessageNotReadableException`, `IllegalArgumentException`), returning clear JSON error payloads with HTTP 400 or 404 status codes.

## How to Run

### Prerequisites
- Java 17+ (supports Java 17, 21, and 25+)

### Commands
```bash
# Run tests using the included Maven wrapper
./mvnw clean test

# Start the application (runs on http://localhost:8080)
./mvnw spring-boot:run
```

---

## API Endpoints

### 1. Check Citizen Scheme Eligibility
```bash
curl -X POST http://localhost:8080/eligibility/check \
  -H "Content-Type: application/json" \
  -d '{
    "age": 26,
    "annualIncome": 150000,
    "state": "Maharashtra",
    "category": "SC",
    "occupation": "Student"
  }'
```

**Sample Response:**
```json
[
  {
    "id": 1,
    "name": "Atal Pension Yojana (APY)",
    "description": "Guaranteed minimum pension scheme for unorganized sector workers.",
    "benefit": "Monthly pension of Rs 1,000 to Rs 5,000 after age 60",
    "applyLink": "https://enps.nsdl.com",
    "sourceUrl": "https://www.npscra.nsdl.co.in/scheme-details.php",
    "rules": [
      { "id": 1, "field": "AGE", "operator": "GTE", "value": "18" },
      { "id": 2, "field": "AGE", "operator": "LTE", "value": "40" },
      { "id": 3, "field": "STATE", "operator": "EQ", "value": "ALL" }
    ]
  },
  {
    "id": 5,
    "name": "Post Matric Scholarship for SC Students",
    "description": "Financial assistance to Scheduled Caste students studying at post-matriculation stage.",
    "benefit": "Tuition fee reimbursement and academic allowance",
    "applyLink": "https://scholarships.gov.in",
    "sourceUrl": "https://socialjustice.gov.in/schemes/25",
    "rules": [
      { "id": 11, "field": "CATEGORY", "operator": "EQ", "value": "SC" },
      { "id": 12, "field": "INCOME", "operator": "LTE", "value": "250000" }
    ]
  }
]
```

### 2. Scheme Management (CRUD)

* **List All Schemes**:
  ```bash
  curl -X GET http://localhost:8080/schemes
  ```

* **Get Scheme by ID**:
  ```bash
  curl -X GET http://localhost:8080/schemes/1
  ```

* **Create New Scheme**:
  ```bash
  curl -X POST http://localhost:8080/schemes \
    -H "Content-Type: application/json" \
    -d '{
      "name": "PM Kisan Samman Nidhi",
      "description": "Income support of Rs 6,000 per year for farmer families.",
      "benefit": "Rs 6,000 annually in 3 installments",
      "applyLink": "https://pmkisan.gov.in",
      "sourceUrl": "https://pmkisan.gov.in",
      "rules": [
        { "field": "OCCUPATION", "operator": "EQ", "value": "Farmer" },
        { "field": "STATE", "operator": "EQ", "value": "ALL" }
      ]
    }'
  ```

* **Delete Scheme**:
  ```bash
  curl -X DELETE http://localhost:8080/schemes/1
  ```

---

## How to Add a New Rule Type

Adding a new rule type (e.g. `GenderRule` or `DisabilityRule`) requires **zero changes** to `SchemeService`, controllers, or database schema:

1. **Create one new class** implementing `Rule` in `com.example.scheme.rule`:
   ```java
   package com.example.scheme.rule;
   import com.example.scheme.dto.UserProfile;

   public class GenderRule implements Rule {
       private final String operator;
       private final String targetGender;

       public GenderRule(String operator, String value) {
           this.operator = operator.trim().toUpperCase();
           this.targetGender = value.trim();
       }

       @Override
       public boolean isSatisfiedBy(UserProfile user) {
           return "EQ".equalsIgnoreCase(operator) && targetGender.equalsIgnoreCase(user.gender());
       }
   }
   ```
2. **Add one factory entry** in `RuleFactory.java`:
   ```java
   private final Map<String, BiFunction<String, String, Rule>> registry = Map.of(
       "AGE", AgeRule::new,
       "INCOME", IncomeRule::new,
       "STATE", StateRule::new,
       "CATEGORY", CategoryRule::new,
       "OCCUPATION", OccupationRule::new,
       "GENDER", GenderRule::new // <-- Added entry
   );
   ```
