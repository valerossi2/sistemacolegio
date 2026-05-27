$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$JDK = "C:\Users\angel\.jdks\temurin-21.0.3"
$JavaFXVersion = "21.0.3"
$JmodsDir = "$ProjectRoot\target\jmods"
$InputDir = "$ProjectRoot\target\jpackage-input"
$DistDir = "$ProjectRoot\target\dist"
$FatJar = "sistema_colegio-1.0.0-jar-with-dependencies.jar"
$IconPath = "$ProjectRoot\src\main\resources\icon.ico"

# Step 1: Build fat jar
Write-Host "=== Building fat JAR ==="
& "$ProjectRoot\mvnw.cmd" clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Host "Build failed"; exit 1 }

# Step 2: Prepare staging directory (only jars, not the whole target/)
Remove-Item -Path $InputDir -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path $InputDir -Force | Out-Null
Copy-Item "$ProjectRoot\target\$FatJar" -Destination $InputDir
Copy-Item "$ProjectRoot\target\sistema_colegio-1.0.0.jar" -Destination $InputDir

# Step 3: Download JavaFX jmods (if not present)
if (-not (Test-Path "$JmodsDir\javafx.base.jmod")) {
    Write-Host "=== Downloading JavaFX jmods ==="
    $JmodsUrl = "https://download2.gluonhq.com/openjfx/$JavaFXVersion/openjfx-${JavaFXVersion}_windows-x64_bin-jmods.zip"
    $ZipPath = "$env:TEMP\javafx-jmods.zip"
    Invoke-WebRequest -Uri $JmodsUrl -OutFile $ZipPath
    Expand-Archive -Path $ZipPath -DestinationPath "$env:TEMP\javafx-jmods" -Force
    New-Item -ItemType Directory -Path $JmodsDir -Force | Out-Null
    Get-ChildItem "$env:TEMP\javafx-jmods\javafx-jmods-$JavaFXVersion\*" -Filter "*.jmod" | Move-Item -Destination $JmodsDir
    Remove-Item "$env:TEMP\javafx-jmods" -Recurse -Force
    Remove-Item $ZipPath -Force
}

# Step 4: Create portable app image (standalone directory with .exe)
Write-Host "=== Creating portable app image ==="
Remove-Item -Path $DistDir -Recurse -Force -ErrorAction SilentlyContinue
& $JDK\bin\jpackage.exe --type app-image `
    --input $InputDir `
    --name LuminaAcademy `
    --main-class ui.LuminaAcademyFX `
    --main-jar $FatJar `
    --module-path $JmodsDir `
    --add-modules javafx.controls,javafx.fxml,java.sql `
    --icon $IconPath `
    --vendor "Lumina Academy" `
    --app-version "1.0.0" `
    --dest $DistDir
if ($LASTEXITCODE -ne 0) { Write-Host "app-image creation failed"; exit 1 }

# Step 5: Create installer EXE (requires WiX Toolset)
$WixFound = (Get-Command "light.exe" -ErrorAction SilentlyContinue) -or (Test-Path "${env:ProgramFiles(x86)}\WiX Toolset*")
if ($WixFound) {
    Write-Host "=== Creating installer EXE ==="
    & $JDK\bin\jpackage.exe --type exe `
        --input $InputDir `
        --name LuminaAcademy `
        --main-class ui.LuminaAcademyFX `
        --main-jar $FatJar `
        --module-path $JmodsDir `
        --add-modules javafx.controls,javafx.fxml,java.sql `
        --icon $IconPath `
        --vendor "Lumina Academy" `
        --app-version "1.0.0" `
        --dest $DistDir `
        --win-menu `
        --win-shortcut
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Installer: $DistDir\LuminaAcademy-1.0.0.exe"
    } else {
        Write-Host "installer EXE creation failed (WiX issue?)" -ForegroundColor Yellow
    }
} else {
    Write-Host "=== Skipping installer EXE (WiX Toolset not found) ===" -ForegroundColor Yellow
    Write-Host "Install WiX from https://wixtoolset.org to create installer exe"
}

Write-Host "=== Done! ==="
Write-Host "Portable app: $DistDir\LuminaAcademy\LuminaAcademy.exe"
