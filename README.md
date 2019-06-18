# Smartcar Android Auth SDK [![Build Status][ci-image]][ci-url] [![Coverage][coverage-image]][coverage-url] [![Download][bintray-image]][bintray-url]

The SmartcarAuth Android SDK makes it easy to handle the Smartcar authorization flow from Android.

## Resources

* [Smartcar Developer Dashboard](https://developer.smartcar.com)
* [Smartcar API Reference](https://smartcar.com/docs)
* [Smartcar Android SDK Reference Documentation](https://smartcar.github.io/android-sdk)

## Installation

The recommended method for obtaining the SDK is via Gradle or Maven.

### Gradle
```groovy
compile "com.smartcar.sdk:smartcar-auth:1.1.0"
```

### Maven
```xml
<dependency>
  <groupId>com.smartcar.sdk</groupId>
  <artifactId>smartcar-auth</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Getting Started

### `SmartcarAuth`

`clientId`

Application client ID can be obtained from the [Smartcar Developer Portal](https://developer.smartcar.com).

`redirectUri`

Your applciation must register a custom URI scheme in order to recieve the authorization callback. Smartcar requires the custom URI scheme to be in the format of `"sc" + clientId + "://" + hostname`. This URI bust also be registered in [Smartcar's developer portal](https://developer.smartcar.com) for your application. You may append an optional path component or TLD. For example, a redirect uri could be: `sc4a1b01e5-0497-417c-a30e-6df6ba33ba46://myapp.com/callback`.

You will then need to register the URI in the `AndroidManifest.xml` by inserting a new activity with an intent filter. More information on the [data element](https://developer.android.com/guide/topics/manifest/data-element.html).

```
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

`scope`

Permissions requested of the vehicle owner. See the [Smartcar developer documentation](https://developer.smartcar.com/docs) for a full list of available permsisions.

`testMode`

Defaults to `false`. Set to `true` to enable to Mock OEM for testing.

`callback`

Callback run when the Authorization Flow returns with a `code` and the `state`. If there is an error, the `code` will be `null` and a `message` will be provided with more details.


#### Example

```java
import com.smartcar.sdk.SmartcarAuth;
import com.smartcar.sdk.SmartcarCallback;

// Create a new instance of the `SmartcarAuth` class
SmartcarAuth smartcarAuth = new SmartcarAuth(
  "your-client-id",
  "your-redirect-uri",
  new String[] {"permission1", "permission2"},
  true, // enable development mode

  // Create a callback to handle the redirect response
  new SmartcarCallback() {
    @Override
    public void handleResponse(SmartcarResponse smartcarResponse) {
      // Retrieve the authorization code
      Log.d("SmartcarAuth", "Response code: " smartcarResponse.getCode());
      Log.d("SmartcarAuth", "Response message: " smartcarResponse.getMessage());
    }
});
```

### `launchAuthFlow`

Launch the Smartcar authorization flow.

`context`

The application's Android context.

`state` (optional)

An opaque value used to mainain state between the request and callback. The authorization server includes this value when redirecting to the client. It can be retrieved from the `SmartcarResponse.getState()` method.

`forcePrompt` (optional)

Defaults to `false`. The `false` option will skip the approval prompt for usres who have already accepted the requested permissions for your application inthe past. Set it to `true` to force a user to see the approval prompt even if they have already accepted the permissions in the past.

`authVehicleInfo` (optional)

Defaults to `null`. Passing in a `VehicleInfo` object with a `make` property causes the car brand selection screen to be bypassed. For a complete list of supported makes, please see our [API Reference](https://smartcar.com/docs/api#authorization) documentation.

#### Example

```java
smartcarAuth.launchAuthFlow(getApplicationContext());
```

### `addClickHandler`

Add a click event listener to a view.

`context`

The application's Android context.

`view`

The view to attach the click listener to.

`state` (optional)

An opaque value used to mainain state between the request and callback. The authorization server includes this value when redirecting to the client. It can be retrieved from the `SmartcarResponse.getState()` method.

`forcePrompt` (optional)

Defaults to `false`. The `false` option will skip the approval prompt for usres who have already accepted the requested permissions for your application inthe past. Set it to `true` to force a user to see the approval prompt even if they have already accepted the permissions in the past.

#### Example

```java
Button connectButton = (Button) findViewById(R.id.connect_button);
smartcarAuth.addClickHandler(getApplicationContext(), connectButton);
```

[ci-image]: https://travis-ci.com/smartcar/android-sdk.svg?token=6Yrkze1DNb8WHnHxrCy6&branch=master
[ci-url]: https://travis-ci.com/smartcar/android-sdk

[coverage-image]: https://codecov.io/gh/smartcar/android-sdk/branch/master/graph/badge.svg?token=RhacvrisiW
[coverage-url]: https://codecov.io/gh/smartcar/android-sdk

[bintray-image]: https://api.bintray.com/packages/smartcar/library/smartcar-auth/images/download.svg
[bintray-url]: https://bintray.com/smartcar/library/smartcar-auth/_latestVersion
