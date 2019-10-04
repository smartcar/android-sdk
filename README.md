# Smartcar Android Auth SDK [![Build Status][ci-image]][ci-url] [![Coverage][coverage-image]][coverage-url] [![Download][bintray-image]][bintray-url]

The SmartcarAuth Android SDK makes it easy to integrate with Smartcar Connect from Android.

## Reference Documentation

- [JavaDoc Reference Documentation](https://smartcar.github.io/android-sdk)

## Installation

The recommended method for obtaining the SDK is via Gradle or Maven.

### Gradle

```groovy
compile "com.smartcar.sdk:smartcar-auth:3.0.0"
```

### Maven

```xml
<dependency>
    <groupId>com.smartcar.sdk</groupId>
    <artifactId>smartcar-auth</artifactId>
    <version>3.0.0</version>
</dependency>
```

## Redirect URI Setup

Your application must register a custom URI scheme in order to receive Connect's response. Smartcar requires the custom URI scheme to be in the format of `"sc" + clientId + "://" + hostname`. This URI must also be registered in [Smartcar's developer portal](https://developer.smartcar.com) for your application. You may append an optional path component or TLD. For example, a redirect uri could be: 

```
sc4a1b01e5-0497-417c-a30e-6df6ba33ba46://myapp.com/callback
```

You will then need to register the URI in the `AndroidManifest.xml` by inserting a new activity with an intent filter. More information on the [data element](https://developer.android.com/guide/topics/manifest/data-element.html).

```xml
<activity android:name="com.smartcar.sdk.SmartcarCodeReceiver">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="sc4a1b01e5-0497-417c-a30e-6df6ba33ba46"
            android:host="myapp.com"
            android:path="/callback" />
    </intent-filter>
</activity>
```

### Usage

1. Instantiate a new [`SmartcarAuth`](https://smartcar.github.io/android-sdk/com/smartcar/sdk/SmartcarAuth.html) instance with a [`SmartcarCallback`](https://smartcar.github.io/android-sdk/com/smartcar/sdk/SmartcarCallback.html) handler:

```java
import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;
import com.smartcar.sdk.SmartcarResponse;

SmartcarAuth smartcarAuth = new SmartcarAuth(
    "your-client-id",
    "your-redirect-uri",
    new String[] {"read_vehicle_info", "read_odometer"},

    // Create a callback to handle the redirect response
    new SmartcarCallback() {
        @Override
        public void handleResponse(SmartcarResponse smartcarResponse) {
            // Retrieve the authorization code
            Log.d("SmartcarAuth", "Authorization code: " + smartcarResponse.getCode());
        }
});
```

2. Launch the Smartcar Connect flow:

```java
smartcarAuth.launchAuthFlow(getApplicationContext());
```

Alternatively, add a click handler to any button to launch the Smartcar Connect flow.

```java
Button connectButton = findViewById(R.id.connect_button);
smartcarAuth.addClickHandler(getApplicationContext(), connectButton);
```

3. Use the [`AuthUrlBuilder`](https://smartcar.github.io/android-sdk/com/smartcar/sdk/SmartcarAuth.AuthUrlBuilder.html) for additional configuration:

```java
String authUrl = smartcarAuth.new AuthUrlBuilder()
    .setState("foo")
    .setForcePrompt(true)
    .setMakeBypass("TESLA")
    .build();

// Launch Smartcar Connect with the configured parameters
smartcarAuth.launchAuthFlow(getApplicationContext(), authUrl);

// Alternatively attach a click handler
Button connectButton = (Button) findViewById(R.id.connect_button);
smartcarAuth.addClickHandler(getApplicationContext(), button, authUrl);
```

## Contributing

Please use Android Studio 3.5 to develop on the Smartcar Android SDK.

[ci-image]: https://travis-ci.com/smartcar/android-sdk.svg?token=6Yrkze1DNb8WHnHxrCy6&branch=master
[ci-url]: https://travis-ci.com/smartcar/android-sdk
[coverage-image]: https://codecov.io/gh/smartcar/android-sdk/branch/master/graph/badge.svg?token=RhacvrisiW
[coverage-url]: https://codecov.io/gh/smartcar/android-sdk
[bintray-image]: https://api.bintray.com/packages/smartcar/library/smartcar-auth/images/download.svg
[bintray-url]: https://bintray.com/smartcar/library/smartcar-auth/_latestVersion
