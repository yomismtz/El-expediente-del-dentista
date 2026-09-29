# Active UI architecture

This project contains version-suffixed Kotlin files because the UI evolved incrementally. The suffix does not mean a file is obsolete.

## Active root chain

`MainActivity`
→ `AppRootV19` — record selection/import/export and settings overlay
→ `AppRootV7` — global intake/settings bar and clinical overlay navigation
→ `AdaptiveBaseRootV19` — eight-section record navigation and primary screen routing

## Cleanup rule

Do not delete or rename a `Vxx` file based only on its suffix. A versioned file may still be part of the active dependency chain.

Before removing a versioned component:
1. Confirm it is not referenced by the active root chain or another active screen.
2. Confirm no instrumentation/runtime workflow depends on its visible text or behavior.
3. Build the Android app after the change.
4. Run the runtime smoke/instrumentation checks.

Future cleanup should migrate one component family at a time to stable names rather than perform a repository-wide rename.
