$ErrorActionPreference = "Stop"

$ProjectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$OutDir = Join-Path $ProjectRoot "out\test"
New-Item -ItemType Directory -Force $OutDir | Out-Null

$MainFiles = Get-ChildItem (Join-Path $ProjectRoot "src\main\java") -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$TestFiles = Get-ChildItem (Join-Path $ProjectRoot "src\test\java") -Recurse -Filter *.java | ForEach-Object { $_.FullName }

javac -d $OutDir @($MainFiles + $TestFiles)
java -cp $OutDir com.zl.aicodereview.CodeReviewBotTest
