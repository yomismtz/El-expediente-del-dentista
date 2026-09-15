#!/usr/bin/env bash
set -euo pipefail

APK="app/build/outputs/apk/preview/app-preview.apk"
PACKAGE="com.yomismtz.expedientedeldentista.preview"
ACTIVITY="com.yomismtz.expedientedeldentista.MainActivity"
LOG="startup-logcat.txt"

capture_log() {
  adb logcat -d -v threadtime > "$LOG" 2>/dev/null || true
}
trap capture_log EXIT

echo "Waiting for Android framework services..."
ready=0
for _ in $(seq 1 90); do
  package_service="$(adb shell service check package 2>/dev/null || true)"
  activity_service="$(adb shell service check activity 2>/dev/null || true)"
  if grep -q "found" <<<"$package_service" && grep -q "found" <<<"$activity_service"; then
    ready=1
    break
  fi
  sleep 2
done

if [[ "$ready" -ne 1 ]]; then
  echo "::error::Android framework services never became ready."
  adb shell service list | head -n 100 || true
  exit 1
fi

echo "Installing Preview APK..."
adb install -r "$APK"
adb shell pm clear "$PACKAGE" || true
adb logcat -c

echo "Launching $PACKAGE/$ACTIVITY"
START_OUTPUT="$(adb shell am start -W -n "$PACKAGE/$ACTIVITY" 2>&1 || true)"
echo "$START_OUTPUT"
sleep 12

PID="$(adb shell pidof "$PACKAGE" 2>/dev/null | tr -d '\r' || true)"
if [[ -z "$PID" ]]; then
  echo "::error::The Preview process died during startup."
  capture_log
  grep -E 'expedientedeldentista|FATAL EXCEPTION|AndroidRuntime|Process:|Caused by:' "$LOG" | tail -n 400 || true
  exit 1
fi

RESUMED="$(adb shell dumpsys activity activities 2>/dev/null | grep -E 'mResumedActivity|topResumedActivity' | grep "$PACKAGE" || true)"
if [[ -z "$RESUMED" ]]; then
  echo "::warning::Preview process is alive, but MainActivity is not reported as resumed."
  adb shell dumpsys activity activities | grep -E 'mResumedActivity|topResumedActivity|expedientedeldentista' | head -n 60 || true
else
  echo "Preview process is alive and resumed after launch: PID=$PID"
  echo "$RESUMED"
fi
