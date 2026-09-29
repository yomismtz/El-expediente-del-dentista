# Android release signing

The project supports secure release signing without storing the release keystore or passwords in Git.

## Required GitHub Actions secrets

Configure these repository secrets before distributing a production release:

- `ANDROID_RELEASE_KEYSTORE_BASE64` — Base64 representation of the release keystore.
- `ANDROID_RELEASE_STORE_PASSWORD` — keystore password.
- `ANDROID_RELEASE_KEY_ALIAS` — signing key alias.
- `ANDROID_RELEASE_KEY_PASSWORD` — signing key password.

When the keystore secret is present, the Android workflow restores it only for the build, passes signing values through environment variables, builds the release APK/AAB, and removes the temporary keystore afterward.

When the secrets are absent, normal development builds remain possible and the release outputs are not treated as production-signed distribution artifacts.

## Security rules

- Never commit a production `.keystore` or `.jks` file.
- Never place release passwords in Gradle files, workflow YAML, source code, issues, logs, or documentation.
- Keep an offline backup of the production signing key and its credentials.
- Use the same production signing identity for future updates distributed outside Play App Signing.
- If publishing through Google Play, configure Play App Signing according to the store release process and retain the upload key securely.

## Release checklist

1. Increase `versionCode` for every distributed update.
2. Update `versionName` when appropriate.
3. Confirm Android build and instrumentation checks pass.
4. Confirm all four signing secrets are configured.
5. Build release APK and AAB.
6. Verify the release artifact is signed before external distribution.
7. Archive the release commit/tag and preserve the signing-key backup separately.
