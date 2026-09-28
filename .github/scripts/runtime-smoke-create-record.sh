#!/usr/bin/env bash
set -euo pipefail

PKG="com.yomismtz.expedientedeldentista"
APK="app/build/outputs/apk/debug/app-debug.apk"

dump_logs() {
  echo "=== Process state ==="
  adb shell pidof "$PKG" || true
  echo "=== AndroidRuntime ==="
  adb logcat -d -v threadtime AndroidRuntime:E '*:S' || true
  echo "=== Relevant app/logcat tail ==="
  adb logcat -d -v threadtime | tail -n 500 || true
}
trap dump_logs EXIT

tap_text() {
  local query="$1"
  adb shell uiautomator dump /sdcard/window.xml >/dev/null
  adb pull /sdcard/window.xml /tmp/window.xml >/dev/null
  local coords
  coords=$(QUERY="$query" python3 -c 'import os,re,xml.etree.ElementTree as E; q=os.environ["QUERY"].lower(); root=E.parse("/tmp/window.xml").getroot(); n=next((x for x in root.iter("node") if q in (x.attrib.get("text","")+" "+x.attrib.get("content-desc","")).lower()),None); assert n is not None, "missing UI text: "+q; p=list(map(int,re.findall(r"\d+",n.attrib["bounds"]))); print((p[0]+p[2])//2,(p[1]+p[3])//2)')
  adb shell input tap $coords
  sleep 1
}

adb install -r "$APK"
adb logcat -c
adb shell am force-stop "$PKG"
adb shell am start -W -n "$PKG/.MainActivity"
sleep 3

tap_text "NUEVO EXPEDIENTE"
tap_text "Iniciales"
adb shell input text ABC
adb shell input keyevent 4
sleep 1
tap_text "Edad"
adb shell input text 25
adb shell input keyevent 4
sleep 1
tap_text "Femenino"
sleep 1
tap_text "Crear y abrir expediente"
sleep 5

if ! adb shell pidof "$PKG" >/dev/null; then
  echo "App process died after creating a record."
  exit 1
fi

adb shell uiautomator dump /sdcard/after.xml >/dev/null
adb pull /sdcard/after.xml /tmp/after.xml >/dev/null
if ! grep -Eq "El expediente|Abrir expediente|Nota de ingreso" /tmp/after.xml; then
  echo "App stayed alive but expected record UI was not found."
  cat /tmp/after.xml
  exit 1
fi

echo "Record creation smoke test passed."
