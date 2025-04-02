' ==========================================================================
' invis.vbs - Run commands invisibly without showing a command window
' ==========================================================================
'
' This script allows you to execute batch files or other scripts without
' displaying a command prompt window. Useful for background automation tasks.
'
' Usage: 
'   wscript.exe invis.vbs script.bat [arg1] [arg2] [...]
'
' Examples:
'   wscript.exe invis.vbs pause.bat
'   wscript.exe invis.vbs resume.bat
'   wscript.exe invis.vbs run_task.bat "param with spaces" other_param
' ==========================================================================

' Get command-line arguments
Set args = WScript.Arguments
num = args.Count

' Check if any arguments were provided
If num = 0 Then
    WScript.Echo "Usage: [CScript | WScript] invis.vbs script.bat [arg1] [arg2] [...]"
    WScript.Quit 1
End If

' Build argument string for all arguments after the first
sargs = ""
If num > 1 Then
    sargs = " "
    For k = 1 To num - 1
        anArg = args.Item(k)
        sargs = sargs & anArg & " "
    Next
End If

' Create shell object and run the command invisibly
Set WshShell = WScript.CreateObject("WScript.Shell")
WshShell.Run """" & WScript.Arguments(0) & """" & sargs, 0, False