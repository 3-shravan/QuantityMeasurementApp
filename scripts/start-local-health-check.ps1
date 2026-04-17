param(
    [string]$WorkspaceRoot = (Split-Path -Parent $PSScriptRoot),
    [int]$EurekaPort = 8761,
    [int]$AuthPort = 8081,
    [int]$QuantityPort = 8082,
    [int]$GatewayPort = 8080,
    [int]$ServiceHealthTimeoutSec = 180,
    [int]$RegistrationTimeoutSec = 120,
    [switch]$DryRun
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Start-ServiceProcess {
    param(
        [string]$ServiceName,
        [string]$ModuleFolder
    )

    $modulePath = Join-Path $WorkspaceRoot $ModuleFolder
    if (-not (Test-Path $modulePath)) {
        throw "Module path not found for ${ServiceName}: $modulePath"
    }

    $command = "Push-Location '$modulePath'; mvn spring-boot:run; Pop-Location"
    Write-Host "[START] $ServiceName -> $modulePath"

    if ($DryRun) {
        Write-Host "[DRY-RUN] powershell.exe -NoExit -Command $command"
        return
    }

    Start-Process -FilePath "powershell.exe" -ArgumentList "-NoExit", "-Command", $command | Out-Null
}

function Wait-ForHealthUp {
    param(
        [string]$ServiceName,
        [string]$HealthUrl,
        [int]$TimeoutSec
    )

    Write-Host "[WAIT] $ServiceName health: $HealthUrl (timeout ${TimeoutSec}s)"
    $deadline = (Get-Date).AddSeconds($TimeoutSec)

    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-RestMethod -Uri $HealthUrl -Method Get -TimeoutSec 5
            if ($null -ne $response -and $response.status -eq "UP") {
                Write-Host "[OK] $ServiceName health is UP"
                return $true
            }
        } catch {
            # Service might still be booting. Keep polling.
        }

        Start-Sleep -Seconds 3
    }

    Write-Host "[FAIL] Timed out waiting for $ServiceName health"
    return $false
}

function Wait-ForEurekaRegistration {
    param(
        [string]$ServiceName,
        [string]$EurekaAppsUrl,
        [int]$TimeoutSec
    )

    Write-Host "[WAIT] Eureka registration for $ServiceName (timeout ${TimeoutSec}s)"
    $deadline = (Get-Date).AddSeconds($TimeoutSec)
    $needle = $ServiceName.ToUpperInvariant()

    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $EurekaAppsUrl -Method Get -UseBasicParsing -TimeoutSec 5
            if ($null -ne $response -and $response.Content.ToUpperInvariant().Contains($needle)) {
                Write-Host "[OK] Eureka has registered $ServiceName"
                return $true
            }
        } catch {
            # Eureka may still be starting.
        }

        Start-Sleep -Seconds 3
    }

    Write-Host "[FAIL] Timed out waiting for Eureka registration for $ServiceName"
    return $false
}

$eurekaHealthUrl = "http://localhost:$EurekaPort/actuator/health"
$authHealthUrl = "http://localhost:$AuthPort/actuator/health"
$quantityHealthUrl = "http://localhost:$QuantityPort/actuator/health"
$gatewayHealthUrl = "http://localhost:$GatewayPort/actuator/health"
$eurekaAppsUrl = "http://localhost:$EurekaPort/eureka/apps"

Write-Host "[INFO] Workspace root: $WorkspaceRoot"
Write-Host "[INFO] Startup order: eureka-server -> auth-service -> quantity-service -> api-gateway"

Start-ServiceProcess -ServiceName "EUREKA-SERVER" -ModuleFolder "eureka-server"
if (-not $DryRun -and -not (Wait-ForHealthUp -ServiceName "EUREKA-SERVER" -HealthUrl $eurekaHealthUrl -TimeoutSec $ServiceHealthTimeoutSec)) {
    exit 1
}

Start-ServiceProcess -ServiceName "AUTH-SERVICE" -ModuleFolder "auth-service"
if (-not $DryRun -and -not (Wait-ForHealthUp -ServiceName "AUTH-SERVICE" -HealthUrl $authHealthUrl -TimeoutSec $ServiceHealthTimeoutSec)) {
    exit 1
}
if (-not $DryRun -and -not (Wait-ForEurekaRegistration -ServiceName "AUTH-SERVICE" -EurekaAppsUrl $eurekaAppsUrl -TimeoutSec $RegistrationTimeoutSec)) {
    exit 1
}

Start-ServiceProcess -ServiceName "QUANTITY-SERVICE" -ModuleFolder "quantity-service"
if (-not $DryRun -and -not (Wait-ForHealthUp -ServiceName "QUANTITY-SERVICE" -HealthUrl $quantityHealthUrl -TimeoutSec $ServiceHealthTimeoutSec)) {
    exit 1
}
if (-not $DryRun -and -not (Wait-ForEurekaRegistration -ServiceName "QUANTITY-SERVICE" -EurekaAppsUrl $eurekaAppsUrl -TimeoutSec $RegistrationTimeoutSec)) {
    exit 1
}

Start-ServiceProcess -ServiceName "API-GATEWAY" -ModuleFolder "api-gateway"
if (-not $DryRun -and -not (Wait-ForHealthUp -ServiceName "API-GATEWAY" -HealthUrl $gatewayHealthUrl -TimeoutSec $ServiceHealthTimeoutSec)) {
    exit 1
}
if (-not $DryRun -and -not (Wait-ForEurekaRegistration -ServiceName "API-GATEWAY" -EurekaAppsUrl $eurekaAppsUrl -TimeoutSec $RegistrationTimeoutSec)) {
    exit 1
}

Write-Host "[DONE] Local services are up and registered."
Write-Host "[INFO] Gateway URL: http://localhost:$GatewayPort"
Write-Host "[INFO] Auth URL:    http://localhost:$AuthPort"
Write-Host "[INFO] Quantity URL:http://localhost:$QuantityPort"
Write-Host "[INFO] Eureka URL:  http://localhost:$EurekaPort"


