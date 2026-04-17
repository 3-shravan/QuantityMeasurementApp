param(
    [string]$GatewayBase = "http://localhost:8080",
    [string]$Password = "Password123",
    [int]$RegisterRetryCount = 6,
    [int]$RegisterRetryDelaySec = 3
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$username = "gw_user_{0}" -f [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()
$email = "{0}@example.com" -f $username
$token = $null

$jsonHeaders = @{ "Content-Type" = "application/json" }

function Assert-ErrorContract {
    param(
        [object]$Body,
        [string]$ApiName
    )

    $required = @("timestamp", "status", "error", "message", "path")
    foreach ($field in $required) {
        if (-not ($Body.PSObject.Properties.Name -contains $field)) {
            throw "[$ApiName] Missing error field '$field'."
        }
    }
}

function Invoke-Api {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Path,
        [int[]]$ExpectedStatus,
        [object]$Body = $null,
        [hashtable]$Headers = $null,
        [switch]$ExpectErrorContract,
        [scriptblock]$Validate
    )

    $uri = "$GatewayBase$Path"
    $requestHeaders = @{}
    if ($Headers) {
        foreach ($k in $Headers.Keys) {
            $requestHeaders[$k] = $Headers[$k]
        }
    }

    try {
        if ($null -ne $Body) {
            $payload = if ($Body -is [string]) { $Body } else { $Body | ConvertTo-Json -Depth 8 }
            $response = Invoke-WebRequest -Uri $uri -Method $Method -Headers $requestHeaders -Body $payload -UseBasicParsing
        } else {
            $response = Invoke-WebRequest -Uri $uri -Method $Method -Headers $requestHeaders -UseBasicParsing
        }
        $statusCode = [int]$response.StatusCode
        $content = $response.Content
        if ($content -is [byte[]]) {
            $content = [System.Text.Encoding]::UTF8.GetString($content)
        }
    } catch {
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode.value__
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $content = $reader.ReadToEnd()
            $reader.Close()
            if ($content -is [byte[]]) {
                $content = [System.Text.Encoding]::UTF8.GetString($content)
            }
            if ([string]::IsNullOrWhiteSpace($content) -and $_.ErrorDetails -and $_.ErrorDetails.Message) {
                $content = $_.ErrorDetails.Message
            }
        } else {
            throw "[$Name] Request failed before receiving response: $($_.Exception.Message)"
        }
    }

    if ($ExpectedStatus -notcontains $statusCode) {
        throw "[$Name] Expected status $($ExpectedStatus -join ',') but got $statusCode. Body: $content"
    }

    $bodyObj = $null
    if ($content) {
        try {
            $bodyObj = $content | ConvertFrom-Json
        } catch {
            throw "[$Name] Response is not valid JSON. Body: $content"
        }
    }

    if ($ExpectErrorContract) {
        if ($null -eq $bodyObj) {
            throw "[$Name] Expected JSON error body but got empty body."
        }
        Assert-ErrorContract -Body $bodyObj -ApiName $Name
    }

    if ($Validate) {
        & $Validate $bodyObj $statusCode $content
    }

    Write-Host ("[PASS] {0} -> {1}" -f $Name, $statusCode) -ForegroundColor Green
    return [PSCustomObject]@{
        StatusCode = $statusCode
        Body = $bodyObj
        RawBody = $content
    }
}

Write-Host "[INFO] Testing gateway APIs at $GatewayBase"

Invoke-Api -Name "Gateway health" -Method GET -Path "/actuator/health" -ExpectedStatus @(200) -Validate {
    param($body)
    if (-not ($body.PSObject.Properties.Name -contains "status") -or $body.status -ne "UP") {
        throw "[Gateway health] Expected status UP"
    }
}

for ($i = 0; $i -lt $RegisterRetryCount; $i++) {
    $registerResult = Invoke-Api -Name "Register" -Method POST -Path "/api/v1/auth/register" -ExpectedStatus @(201, 503) -Headers $jsonHeaders -Body @{
        username = $username
        email = $email
        password = $Password
    } -ExpectErrorContract:($false)

    if ($registerResult.StatusCode -eq 201) {
        $token = $registerResult.Body.token
        break
    }

    if ($i -lt ($RegisterRetryCount - 1)) {
        Write-Host "[WARN] Register returned 503, retrying..." -ForegroundColor Yellow
        Start-Sleep -Seconds $RegisterRetryDelaySec
    }
}

if (-not $token) {
    throw "Could not register user after $RegisterRetryCount attempts."
}

$authHeaders = @{ "Content-Type" = "application/json"; "Authorization" = "Bearer $token" }

$loginByUsername = Invoke-Api -Name "Login username" -Method POST -Path "/api/v1/auth/login" -ExpectedStatus @(200) -Headers $jsonHeaders -Body @{
    username = $username
    password = $Password
}
$token = $loginByUsername.Body.token
$authHeaders.Authorization = "Bearer $token"


Invoke-Api -Name "Duplicate register" -Method POST -Path "/api/v1/auth/register" -ExpectedStatus @(409) -Headers $jsonHeaders -ExpectErrorContract -Body @{
    username = $username
    email = $email
    password = $Password
}

Invoke-Api -Name "Invalid login payload" -Method POST -Path "/api/v1/auth/login" -ExpectedStatus @(400) -Headers $jsonHeaders -ExpectErrorContract -Body @{
    password = $Password
}

Invoke-Api -Name "OAuth2 fake token" -Method POST -Path "/api/v1/auth/oauth2/google" -ExpectedStatus @(400, 401) -Headers $jsonHeaders -ExpectErrorContract -Body @{
    provider = "google"
    accessToken = "fake_google_access_token"
}

$quantityCompare = @{
    thisQuantityDTO = @{ value = 30.48; unit = "CENTIMETERS"; measurementType = "LengthUnit" }
    thatQuantityDTO = @{ value = 1; unit = "FEET"; measurementType = "LengthUnit" }
}
$quantityConvert = @{
    thisQuantityDTO = @{ value = 2; unit = "LITRE"; measurementType = "VolumeUnit" }
    thatQuantityDTO = @{ value = 0; unit = "MILLILITRE"; measurementType = "VolumeUnit" }
}
$quantityAdd = @{
    thisQuantityDTO = @{ value = 1; unit = "KILOGRAM"; measurementType = "WeightUnit" }
    thatQuantityDTO = @{ value = 500; unit = "GRAM"; measurementType = "WeightUnit" }
}
$quantitySubtract = @{
    thisQuantityDTO = @{ value = 3; unit = "FEET"; measurementType = "LengthUnit" }
    thatQuantityDTO = @{ value = 12; unit = "INCHES"; measurementType = "LengthUnit" }
}
$quantityDivide = @{
    thisQuantityDTO = @{ value = 2; unit = "LITRE"; measurementType = "VolumeUnit" }
    thatQuantityDTO = @{ value = 500; unit = "MILLILITRE"; measurementType = "VolumeUnit" }
}

Invoke-Api -Name "Quantity compare" -Method POST -Path "/api/v1/quantities/compare" -ExpectedStatus @(200) -Headers $authHeaders -Body $quantityCompare
Invoke-Api -Name "Quantity convert" -Method POST -Path "/api/v1/quantities/convert" -ExpectedStatus @(200) -Headers $authHeaders -Body $quantityConvert
Invoke-Api -Name "Quantity add" -Method POST -Path "/api/v1/quantities/add" -ExpectedStatus @(200) -Headers $authHeaders -Body $quantityAdd
Invoke-Api -Name "Quantity subtract" -Method POST -Path "/api/v1/quantities/subtract" -ExpectedStatus @(200) -Headers $authHeaders -Body $quantitySubtract
Invoke-Api -Name "Quantity divide" -Method POST -Path "/api/v1/quantities/divide" -ExpectedStatus @(200) -Headers $authHeaders -Body $quantityDivide
Invoke-Api -Name "Quantity history by operation" -Method GET -Path "/api/v1/quantities/history/operation/ADD" -ExpectedStatus @(200) -Headers @{ "Authorization" = "Bearer $token" }
Invoke-Api -Name "Quantity history by type" -Method GET -Path "/api/v1/quantities/history/type/LengthUnit" -ExpectedStatus @(200) -Headers @{ "Authorization" = "Bearer $token" }
Invoke-Api -Name "Quantity errored history" -Method GET -Path "/api/v1/quantities/history/errored" -ExpectedStatus @(200) -Headers @{ "Authorization" = "Bearer $token" }
Invoke-Api -Name "Quantity count by operation" -Method GET -Path "/api/v1/quantities/count/ADD" -ExpectedStatus @(200) -Headers @{ "Authorization" = "Bearer $token" }

Invoke-Api -Name "Guest quantity add" -Method POST -Path "/api/v1/quantities/add" -ExpectedStatus @(200) -Headers $jsonHeaders -Body @{
    thisQuantityDTO = @{ value = 1; unit = "FEET"; measurementType = "LengthUnit" }
    thatQuantityDTO = @{ value = 12; unit = "INCHES"; measurementType = "LengthUnit" }
}

Invoke-Api -Name "Guest errored history" -Method GET -Path "/api/v1/quantities/history/errored" -ExpectedStatus @(200) -Validate {
    param($body)
    if (-not ($body -is [System.Array])) {
        throw "[Guest errored history] Expected array response."
    }
}

Invoke-Api -Name "Quantity validation error" -Method POST -Path "/api/v1/quantities/compare" -ExpectedStatus @(400) -Headers $authHeaders -ExpectErrorContract -Body @{
    thisQuantityDTO = @{ value = 1; unit = "INVALID_UNIT"; measurementType = "LengthUnit" }
    thatQuantityDTO = @{ value = 1; unit = "FEET"; measurementType = "LengthUnit" }
}

Invoke-Api -Name "Logout without token" -Method POST -Path "/api/v1/auth/logout" -ExpectedStatus @(401) -Headers $jsonHeaders -ExpectErrorContract -Body @{}
Invoke-Api -Name "Logout with token" -Method POST -Path "/api/v1/auth/logout" -ExpectedStatus @(200) -Headers $authHeaders -Body @{}

Write-Host "[DONE] Gateway API suite passed." -ForegroundColor Cyan

