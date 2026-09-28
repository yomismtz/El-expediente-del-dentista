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

dump_ui() {
  adb shell uiautomator dump /sdcard/window.xml >/dev/null
  adb pull /sdcard/window.xml /tmp/window.xml >/dev/null
}

find_text_coords() {
  local query="$1"
  QUERY="$query" python3 -c 'import os,re,xml.etree.ElementTree as E; q=os.environ["QUERY"].lower(); root=E.parse("/tmp/window.xml").getroot(); n=next((x for x in root.iter("node") if q in (x.attrib.get("text","")+" "+x.attrib.get("content-desc","")).lower()),None); print("" if n is None else " ".join(map(str,[(lambda p:(p[0]+p[2])//2)(list(map(int,re.findall(r"\d+",n.attrib["bounds"])))),(lambda p:(p[1]+p[3])//2)(list(map(int,re.findall(r"\d+",n.attrib["bounds"]))))])))'
}

wait_for_text() {
  local query="$1"
  local attempts="${2:-60}"
  local i
  for ((i=1;i<=attempts;i++)); do
    if adb shell pidof "$PKG" >/dev/null; then
      dump_ui || true
      if QUERY="$query" python3 -c 'import os,xml.etree.ElementTree as E,sys; q=os.environ["QUERY"].lower(); root=E.parse("/tmp/window.xml").getroot(); sys.exit(0 if any(q in (x.attrib.get("text","")+" "+x.attrib.get("content-desc","")).lower() for x in root.iter("node")) else 1)'; then
        return 0
      fi
    else
      echo "App process died while waiting for UI text: $query"
      exit 1
    fi
    sleep 1
  done
  echo "Timed out waiting for UI text: $query"
  dump_ui || true
  cat /tmp/window.xml || true
  exit 1
}

tap_text() {
  local query="$1"
  wait_for_text "$query"
  dump_ui
  local coords
  coords=$(QUERY="$query" python3 -c 'import os,re,xml.etree.ElementTree as E; q=os.environ["QUERY"].lower(); root=E.parse("/tmp/window.xml").getroot(); n=next(x for x in root.iter("node") if q in (x.attrib.get("text","")+" "+x.attrib.get("content-desc","")).lower()); p=list(map(int,re.findall(r"\d+",n.attrib["bounds"]))); print((p[0]+p[2])//2,(p[1]+p[3])//2)')
  adb shell input tap $coords
  sleep 1
}

assert_alive() {
  local stage="$1"
  if ! adb shell pidof "$PKG" >/dev/null; then
    echo "App process died during: $stage"
    exit 1
  fi
}

adb install -r "$APK"
adb logcat -c
adb shell am force-stop "$PKG"
adb shell am start -W -n "$PKG/.MainActivity"

wait_for_text "NUEVO EXPEDIENTE"
tap_text "NUEVO EXPEDIENTE"
tap_text "Iniciales"
adb shell input text ABC
adb shell input keyevent 4
tap_text "Edad"
adb shell input text 25
adb shell input keyevent 4
tap_text "Femenino"
tap_text "Crear y abrir expediente"

wait_for_text "Abrir expediente"
assert_alive "record creation"

tap_text "Abrir expediente"
wait_for_text "Secciones del expediente"
assert_alive "opening the record sections"

adb shell input keyevent 4
wait_for_text "Abrir expediente"
assert_alive "closing the record sections"

echo "Create/open/close record smoke test passed."
