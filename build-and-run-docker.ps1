$csvFolder = "$env:USERPROFILE\Desktop\csv"

if (!(Test-Path $csvFolder))
{
    Write-Host "CSV-Verzeichnis nicht gefunden: $csvFolder"
    exit 1
}

docker stop bistro 2> $null
docker rm bistro 2> $null

mvn clean package
docker build -t bistro:latest .

docker run -d `
  -p 8080:8080 `
  --name bistro `
  -v "${csvFolder}:/data/input" `
  -e CSV_INPUT_DIR=/data/input `
  bistro:latest
