# Smartcar Android Auth SDK [![Build Status][ci-image]][ci-url] [![JavaDoc][javadoc-image]][javadoc-url] [![Maven Central][maven-image]][maven-url]

The SmartcarAuth Android SDK makes it easy to integrate with Smartcar Connect from Android.

## Reference Documentation

- [Smartcar Android Auth SDK JavaDoc][javadoc-url]
- [Smartcar Developer Dashboard][smartcar-dashboard]
- [Smartcar Documentation][smartcar-docs]

## Installation

Add the following to your application's `build.gradle` dependencies:

```
dependencies {
    implementation 'com.smartcar.sdk:smartcar-auth:4.0.0'
}
```

## Usage

1. Instantiate a new [`SmartcarAuth`](https://javadoc.io/doc/com.smartcar.sdk/smartcar-auth/latest/com/smartcar/sdk/SmartcarAuth.html) instance with a [`SmartcarCallback`](https://javadoc.io/doc/com.smartcar.sdk/smartcar-auth/latest/com/smartcar/sdk/SmartcarCallback.html) handler:

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

Alternatively, add a click handler to any view to launch the Smartcar Connect flow.

```java
Button connectButton = findViewById(R.id.connect_button);
smartcarAuth.addClickHandler(getApplicationContext(), connectButton);
```

3. Use the [`AuthUrlBuilder`](https://javadoc.io/doc/com.smartcar.sdk/smartcar-auth/latest/com/smartcar/sdk/SmartcarAuth.AuthUrlBuilder.html) for additional configuration:

```java
String authUrl = smartcarAuth.authUrlBuilder()
    .setState("foo")
    .setForcePrompt(true)
    .setMakeBypass("TESLA")
    .build();

// Launch Smartcar Connect with the configured parameters
smartcarAuth.launchAuthFlow(getApplicationContext(), authUrl);

// Alternatively attach a click handler
Button connectButton = findViewById(R.id.connect_button);
smartcarAuth.addClickHandler(getApplicationContext(), button, authUrl);
```

## Contributing

Please use Android Studio 3.5 to develop on the Smartcar Android SDK.

[smartcar-dashboard]: https://dashboard.smartcar.com/login/
[smartcar-docs]: https://smartcar.com/docs
[ci-image]: https://app.buddy.works/smartcar/android-sdk/pipelines/pipeline/504649/badge.svg?token=97cfcc42803fd12732976ed9738a7a119d87532ffdd2686b5513bbaef42bad93
[ci-url]: https://app.buddy.works/smartcar/android-sdk/pipelines/pipeline/504649
[javadoc-image]: https://javadoc.io/badge2/com.smartcar.sdk/smartcar-auth/javadoc.svg
[javadoc-url]: https://javadoc.io/doc/com.smartcar.sdk/smartcar-auth
[maven-image]: https://img.shields.io/maven-central/v/com.smartcar.sdk/smartcar-auth.svg?label=Maven%20Central
[maven-url]: https://central.sonatype.com/artifact/com.smartcar.sdk/smartcar-auth
