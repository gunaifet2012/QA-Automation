# OrangeHRM Automation Framework

End-to-end test automation framework for the [OrangeHRM](https://opensource-demo.orangehrmlive.com) application, built for a Senior QA Automation Engineer technical assessment.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 23 |
| Test Runner | TestNG 7.9 |
| Browser Automation | Selenium 4.18 |
| Driver Management | WebDriverManager 5.7 |
| API Testing | RestAssured 5.4 |
| Reporting | ExtentReports 5.1 |
| Logging | Log4j2 |
| CI/CD | GitHub Actions |
| Performance | K6 |
| Build | Maven 3.x |

---

## Project Structure

```
AutomationFramework/
├── .github/
│   └── workflows/
│       └── ci.yml                      # GitHub Actions pipeline
├── performance/
│   └── k6/
│       ├── login-test.js               # K6 login performance test
│       └── employee-test.js            # K6 employee API performance test
├── src/
│   ├── main/java/com/orangehrm/
│   │   ├── config/
│   │   │   ├── ConfigManager.java      # Singleton config reader (env-based)
│   │   │   └── DriverManager.java      # ThreadLocal WebDriver management
│   │   ├── pageobjects/                # Locator classes (By objects only)
│   │   │   ├── LoginPageObjects.java
│   │   │   ├── DashboardPageObjects.java
│   │   │   ├── PIMPageObjects.java
│   │   │   └── EmployeePageObjects.java
│   │   ├── pages/                      # Action methods using page objects
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   ├── PIMPage.java
│   │   │   └── EmployeePage.java
│   │   ├── api/
│   │   │   ├── ApiClient.java          # RestAssured base client
│   │   │   └── EmployeeApiService.java # Employee API operations
│   │   ├── models/
│   │   │   └── Employee.java           # Employee POJO with Builder pattern
│   │   ├── listeners/
│   │   │   └── TestListener.java       # TestNG listener (screenshots, reports)
│   │   └── utils/
│   │       ├── WaitUtils.java          # Smart explicit wait strategies
│   │       ├── ScreenshotUtils.java    # Failure screenshot capture
│   │       ├── ReportManager.java      # ExtentReports thread-safe manager
│   │       ├── RetryAnalyzer.java      # TestNG retry logic (max 2 retries)
│   │       └── TestDataGenerator.java  # Random test data generation
│   └── test/
│       ├── java/com/orangehrm/
│       │   ├── base/
│       │   │   └── BaseTest.java       # Test lifecycle (@Before/@After)
│       │   └── tests/
│       │       ├── AuthenticationTest.java
│       │       ├── EmployeeCreationTest.java
│       │       ├── EmployeeUpdateTest.java
│       │       ├── EmployeeDeleteTest.java
│       │       ├── RoleBasedValidationTest.java
│       │       └── ApiVerificationTest.java
│       └── resources/
│           ├── config/
│           │   ├── config-dev.properties
│           │   └── config-staging.properties
│           ├── testdata/
│           │   └── employees.json
│           ├── log4j2.xml
│           ├── testng.xml              # Sequential full regression
│           ├── testng-smoke.xml        # Smoke tests only
│           └── testng-parallel.xml     # 3-thread parallel execution
└── pom.xml
```

---

## Setup Instructions

### Prerequisites

- Java 11+
- Maven 3.8+
- Google Chrome (latest stable)
- Git

### Clone & Build

```bash
git clone <your-repo-url>
cd AutomationFramework
mvn clean install -DskipTests
```

---

## Execution

### Run all tests (sequential)

```bash
mvn test -Denv=dev
```

### Run smoke tests only

```bash
mvn test -DsuiteFile=src/test/resources/testng-smoke.xml -Denv=dev
```

### Run in parallel (3 threads, headless)

```bash
mvn test -Pparallel
```

### Run against staging, headless

```bash
mvn test -Pstaging
```

### Run with specific browser

```bash
mvn test -Dbrowser=firefox -Denv=dev
```

### Run a specific test group

```bash
mvn test -Dgroups=auth
# Groups: auth | employee | rbac | api | smoke | regression
```

---

## CI/CD Pipeline (GitHub Actions)

The pipeline at `.github/workflows/ci.yml` runs:

| Job | Trigger | Description |
|---|---|---|
| `smoke-tests` | Every push/PR | Quick smoke gate — runs `testng-smoke.xml` headless |
| `regression-tests` | After smoke passes | 3-shard parallel matrix (auth / employee / rbac-api) |
| `performance-tests` | Nightly + manual | K6 login + employee API load tests |
| `aggregate-reports` | After all jobs | Collects and publishes all artifacts |

**Manual triggers** via `workflow_dispatch` allow selecting environment, suite, and browser.

---

## Reporting & Observability

| Feature | Implementation |
|---|---|
| HTML reports | ExtentReports (dark theme) — `test-output/reports/` |
| Screenshots on failure | Auto-captured via `TestListener` — `test-output/screenshots/` |
| Embedded screenshots | Base64 screenshots embedded in HTML report |
| Logs | Log4j2 rolling file — `test-output/logs/` |
| Tagging | TestNG `groups` — `@Test(groups={"smoke","regression"})` |
| Environment execution | `-Denv=dev` / `-Denv=staging` switches `config-{env}.properties` |

---

## Test Stability & Reliability

### Retry Logic
`RetryAnalyzer` implements `IRetryAnalyzer` — failed tests retry up to **2 times** automatically before being marked failed. Enabled per-test with `retryAnalyzer = RetryAnalyzer.class`.

### Smart Waiting
`WaitUtils` provides explicit waits only — **no `Thread.sleep()`**:
- `waitForVisible` — element visible before interaction
- `waitForClickable` — element interactive before click
- `waitForInvisible` — wait for spinners/loaders to clear
- `waitForUrlContains` — navigation confirmation
- `waitForPageLoad` — JS `document.readyState == complete`

### Screenshot on Failure
`TestListener.onTestFailure` captures screenshots automatically, embeds them in the ExtentReport, and saves them to `test-output/screenshots/`.

---

## Flaky Test Strategy

### Detection
- Run the same test suite 3× and compare pass/fail rates
- Flag any test that passes in some runs and fails in others
- Use CI history (GitHub Actions run matrix) to detect non-deterministic failures
- Add run metadata (timestamp, environment, retry count) to reports for correlation

### Mitigation
| Root Cause | Mitigation |
|---|---|
| Timing / race conditions | Replace implicit waits with `waitForClickable`, `waitForVisible` |
| Stale element exceptions | Re-locate element inside retry loop in `BasePage` |
| Data collisions | Use `TestDataGenerator` for unique random data per test run |
| Environment instability | `RetryAnalyzer` retries up to 2× before failing |
| Test order dependency | Each test creates its own data, uses `@BeforeMethod` for clean state |
| Slow network | Increase `explicit.wait.seconds` in environment config |

---

## Performance Tests (K6)

### Login API (`performance/k6/login-test.js`)
- Ramps to **10 VUs** over 2 minutes
- Thresholds: `p(95) < 3s`, error rate `< 5%`

### Employee API (`performance/k6/employee-test.js`)
- Tests GET list + POST create under **5 VUs**
- Thresholds: create `p(95) < 5s`, fetch `p(90) < 2s`

### Run locally
```bash
k6 run performance/k6/login-test.js
k6 run performance/k6/employee-test.js
```

---

## Key Design Decisions

1. **Separate `pageobjects/` and `pages/` packages** — locators (`By`) are kept strictly in `pageobjects/` classes; action methods live in `pages/`. This means selectors can be updated without touching business logic.

2. **ThreadLocal `DriverManager`** — each test thread gets its own `WebDriver` instance, enabling safe parallel execution across 3 threads with no shared state.

3. **Environment-based config** — `config-{env}.properties` loaded at runtime via `-Denv=`. Zero code changes to switch environments.

4. **Builder pattern for `Employee`** — test data stays readable and flexible; optional fields don't require constructor overloading.

5. **RestAssured API layer** — UI and API tests share the same `EmployeeApiService`. `ApiVerificationTest` crosses UI-created records with API reads to ensure consistency.

6. **ExtentReports + TestNG listener** — decoupled reporting; `TestListener` auto-attaches on any failure without test code changes.

7. **No `Thread.sleep()`** — all waits use `WebDriverWait` with `ExpectedConditions`, making tests fast and deterministic.

---

## Notes

- Target application: `https://opensource-demo.orangehrmlive.com` (public demo)
- Credentials: `Admin` / `admin123`
- Tests create and delete their own data using random suffixes to avoid collisions
- The demo instance is shared and may occasionally be slow — `explicit.wait.seconds=20` in dev config accounts for this
