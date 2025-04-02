; ==========================================================================
; Plex Stream Monitor - Auto-Pauses qBittorrent when Plex is streaming
; ==========================================================================
;
; This script monitors Plex Media Server for active streams and pauses
; or resumes qBittorrent accordingly to manage bandwidth usage.
;
; Requirements:
; - qBittorrent with WebUI enabled
; - Plex Media Server with API access
; - wget for Windows
; - wscript.exe and invis.vbs
; 
; Configuration:
; - Edit config.ini to set your Plex token and qBittorrent credentials
; ==========================================================================

#NoEnv  ; Recommended for performance and compatibility
#Warn   ; Enable warnings to assist with detecting common errors
SendMode Input  ; Recommended for new scripts due to its superior speed
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory

#Persistent

; Read configuration from config.ini
IniRead, PlexToken, config.ini, Plex, Token, YOUR_PLEX_TOKEN
IniRead, QbtUser, config.ini, qBittorrent, Username, admin
IniRead, QbtPass, config.ini, qBittorrent, Password, password
IniRead, QbtUrl, config.ini, qBittorrent, Url, http://localhost:8080
IniRead, ScriptPath, config.ini, Paths, Scripts, %A_ScriptDir%

; Set variables
URL := "http://127.0.0.1:32400/status/sessions?X-Plex-Token=" . PlexToken
pause = qbt torrent --username %QbtUser% --password %QbtPass% --url %QbtUrl% pause ALL
resume = qbt torrent --username %QbtUser% --password %QbtPass% --url %QbtUrl% resume ALL
state = start

; Repeat CheckStreaming function every 2500 milliseconds
SetTimer, CheckStreaming, 2500

; Pause qBittorrent when Plex starts streaming and resume it when it's done
CheckStreaming:	
    ; Parse plex server for current activity 
    WinGet, pid, pid, Plex Media Server
    RunWait, %comspec% /c wget -qO- %URL% > plex.txt, , Hide
    FileRead, file, plex.txt
    
    ; Check if plex server is playing video
    If (InStr(file, "playing"))
    {
        ; If plex playing video and torrents are not paused, pause torrents
        If (state != "paused")
        {
            ; Pause qBittorrent
            RunWait, wscript.exe %ScriptPath%\invis.vbs pause.bat `%*, , Hide
            FileDelete, plex.txt
            state = paused
        }
        
        ; Restart loop, skipping resume
        return
    }
    
    ; Resume if not currently resumed
    If (state != "unpaused")
    {
        RunWait, wscript.exe %ScriptPath%\invis.vbs resume.bat `%*, , Hide
        FileDelete, plex.txt
        state = unpaused
    }
    
    ; Restart CheckStreaming
    return