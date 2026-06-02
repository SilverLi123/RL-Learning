$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$OutDir = Join-Path $ProjectRoot "out\main"
New-Item -ItemType Directory -Force $OutDir | Out-Null

$MainFiles = Get-ChildItem (Join-Path $ProjectRoot "src\main\java") -Recurse -Filter *.java | ForEach-Object { $_.FullName }

javac -d $OutDir @($MainFiles)
java -cp $OutDir com.zl.aicodereview.App @args
