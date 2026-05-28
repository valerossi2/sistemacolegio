# AGENTS.md — Lumina Academy

## Architecture

- **JavaFX 21 + Hibernate 6.5** school management desktop app.
- Entrypoint: `ui.LuminaAcademyFX` (configured in `pom.xml` javafx-maven-plugin). The file `src/Main.java` is a stale placeholder — ignore it.
- FXML views under `src/main/resources/fxml/`, controllers in `controller/` and `com.edugrade.controllers/` (note: `com.edugrade.controllers` with `s` vs `com.edugrade.controller` without — both exist).
- `MainController.handleNavigation()` loads FXML views or programmatic views into a `ScrollPane` wrapper via `setCenterView()`.

## Build & Run

```sh
$env:JAVA_HOME="C:\Users\angel\.jdks\temurin-21.0.3"; ./mvnw.cmd compile          # compile only
$env:JAVA_HOME="C:\Users\angel\.jdks\temurin-21.0.3"; ./mvnw.cmd javafx:run       # run the app
```

- Java 17 source, `release 21` target. JDK at `C:\Users\angel\.jdks\temurin-21.0.3`.
- No tests, no test framework, no CI found.

## Key State Singletons

- `ThemeManager.getInstance()` — shared dark/light state. Register listeners via `addListener(Runnable)`. Toggle with `toggle()`. **Never call `new ThemeManager()`** — all controllers must use the singleton.
- `LanguageManager.getInstance()` — i18n with ResourceBundle. Same listener pattern.

## Hibernate / Database

- Configured in `config.Hibernate_config`. Call `Hibernate_config.init()` before use.
- Connection: MySQL `localhost:3306/sis_colegio`, credentials from env vars `sis_colegio_user1` / `sis_colegio_pass1`.
- DDL mode: `validate` (schema must exist — no auto-create).
- Pool: c3p0 (min 3, max 10).
- 16 annotated model classes in `model/` package.

## Authentication

- BCrypt (`PasswordService`) + Google OAuth 2.0 (`GoogleOAuthService`).
- Google client secret must be at `src/main/resources/client_secret.json` (gitignored).

## Conventions

- Theme colors are applied programmatically via `ThemeManager` getters (e.g. `theme.bg()`), not just CSS. When adding new UI, call `theme.bg()`, `theme.card()`, etc. for the current theme.
- Language strings via `LanguageManager.getInstance().get("key")`.
- Navigation: `MainController.loadView("/fxml/Admin/SomeView.fxml")` or create a programmatic view and call `setCenterView(node)`.
- The `demo/` directory is stale — ignore.

## Package Boundary Quirk

`com.edugrade.controllers` (CursoController, MaestrosController, DetalleCursoController) and `com.edugrade.controller` (GradesController) are sibling packages with inconsistent naming. FXML for grades is at `/fxml/Mestros/Calificaciones.fxml`.
