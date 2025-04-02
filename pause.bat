@echo off
REM pause.bat - Pauses all torrents in qBittorrent
REM Configure credentials in config.ini before using

REM Load credentials from config file
for /f "tokens=1,2 delims==" %%a in (config.ini) do (
    if "%%a"=="username" set USERNAME=%%b
    if "%%a"=="password" set PASSWORD=%%b
    if "%%a"=="url" set URL=%%b
)

qbt torrent --username %USERNAME% --password %PASSWORD% --url %URL% pause ALL