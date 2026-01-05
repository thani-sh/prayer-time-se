# CD Setup Instructions

This repository is configured to use **Fastlane** and **GitHub Actions** to automatically deploy the Android and iOS apps.

## GitHub Secrets

You must configure the following secrets in your GitHub repository settings (`Settings` -> `Secrets and variables` -> `Actions`).

### Android Secrets

| Secret Name | Description |
| :--- | :--- |
| `ANDROID_KEYSTORE_BASE64` | The contents of your release keystore file, base64 encoded. Run `base64 -i my-release-key.keystore` to generate this string. |
| `ANDROID_STORE_PASSWORD` | The password for your keystore. |
| `ANDROID_KEY_ALIAS` | The alias of the key in your keystore. |
| `ANDROID_KEY_PASSWORD` | The password for the key alias. |
| `GOOGLE_PLAY_JSON_KEY` | The full content of the Google Play Service Account JSON key file. |

**Google Play Setup:**
1.  Go to the Google Play Console -> Setup -> API access.
2.  Create a new Service Account or link an existing one.
3.  Grant the Service Account "Admin" permissions (or at least Release Manager).
4.  Generate a JSON key for the Service Account in Google Cloud Console.
5.  Paste the content of the JSON file into the `GOOGLE_PLAY_JSON_KEY` secret.

### iOS Secrets

| Secret Name | Description |
| :--- | :--- |
| `APPLE_ID` | Your Apple ID email address (e.g. `dev@example.com`). |
| `FASTLANE_APPLE_APPLICATION_SPECIFIC_PASSWORD` | An app-specific password generated from appleid.apple.com. Used to upload to TestFlight/App Store. |
| `MATCH_GIT_URL` | The HTTPS URL of your private GitHub repository used for `match` (certificates storage). |
| `MATCH_PASSWORD` | The encryption password you set when initializing `match`. |
| `MATCH_GIT_BASIC_AUTHORIZATION` | (Optional) Base64 encoded `username:token` for accessing the match repo if not using SSH key setup in CI. |

**iOS Code Signing (Match) Setup:**
This setup assumes you are using [fastlane match](https://docs.fastlane.tools/actions/match/) to manage code signing.

1.  Create a **private** GitHub repository to store your certificates.
2.  On your local machine, run `fastlane match init` in `apps/iphone` and follow instructions to point to the repo.
3.  Run `fastlane match appstore` and `fastlane match development` to generate certificates and profiles.
4.  Set the `MATCH_GIT_URL` and `MATCH_PASSWORD` secrets in GitHub.

## Workflows

### Android
*   **Beta:** Pushing to `main` triggers a build and upload to the **Internal Test Track** on Google Play.
*   **Production:** Publishing a GitHub Release triggers a build and upload to the **Production Track** on Google Play.

### iOS
*   **Beta:** Pushing to `main` triggers a build and upload to **TestFlight**.
*   **Production:** Publishing a GitHub Release triggers a build and upload to the **App Store**.

## First Run
On the first run, Fastlane might fail if the app doesn't exist on the store yet. Ensure you have created the app entries in both Google Play Console and App Store Connect manually and uploaded at least one build manually if required (though Fastlane usually handles subsequent updates).
