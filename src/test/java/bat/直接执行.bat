@echo off
start "" /d "E:\java_idea\Apache24\bin\" "ab.exe" -n 5000 -c 500 -k http://localhost:8081/frame/login.action