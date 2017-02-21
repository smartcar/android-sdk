# Smartcar Android Auth SDK [![Build Status][ci-image]][ci-url] [![Coverage][coverage-image]][coverage-url]

The Smartcar Android Auth SDK makes it easy to handle the Smartcar OAuth flow from
Android devices. In addition, it provides UI elements (buttons, picker) to easily integrate
the flow into your application.

## Getting Started

1. Checkout [Smartcar Android Auth SDK](https://github.com/smartcar/android-sdk) master branch
2. Take a look at the [example](example/src/main/java/com/smartcar/example/MainActivity.java)

## SDK Usage

The SDK provides a library for authenticating the vehicle-owner and fetching the authorization code
that can be used to fetch an access token using a separate request.

To add this library to your project,

1. Copy the `smartcar-auth-*.aar` file from the
[Smartcar Android Auth SDK repo](https://github.com/smartcar/android-sdk) to the `libs` folder
in your app project
2. Add a reference in your app's `build.gradle` file under the `dependencies` block.
For version `1.0.0` it would be:
```
compile `com.smartcar.sdk:smartcar-auth-1.0.0@aar`
```

## Configuration Settings

`clientId`

Application client ID obtained from [Smartcar Developer Portal] (https://developer.smartcar.com/).

`redirectURI`

Your app must register a custom URI scheme with Android in order to receive the
authorization callback. Smartcar requires the custom URI scheme to be in the
format of `"sc" + clientId + "://" + hostname`. This URI must also be registered
in [Smartcar's developer portal](https://developer.smartcar.com) for your app.
You may append an optional path component or TLD (e.g. `sc4a1b01e5-0497-417c-a30e-6df6ba33ba46://oauth2redirect.com/page`).

`scope`

Permissions requested from the user for specific grant. See the [Smartcar developer documentation](https://developer.smartcar.com/docs)
for a full list of available permissions.

## Handling the Redirect

Your app must allow the Smartcar activity to listen for the authorization code that will be returned on the
app's pre-defined redirectURI as documented above. To do so, include the following code in your app's
AndroidManifest.xml

```
        <activity android:name="com.smartcar.sdk.SmartcarCodeReceiver">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data
                    android:host="YOUR_REDIRECT_URI_HOSTNAME"
                    android:scheme="YOUR_REDIRECT_URI_SCHEME"
                    android:path="YOUR_REDIRECT_URI_PATH" />
            </intent-filter>
        </activity>
```

For a redirectURI `sc4a1b01e5-0497-417c-a30e-6df6ba33ba46://oauth2redirect.com/page`, this would be

```
        <activity android:name="com.smartcar.sdk.SmartcarCodeReceiver">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data
                    android:host="oauth2redirect.com"
                    android:scheme="sc4a1b01e5-0497-417c-a30e-6df6ba33ba46"
                    android:path="/page" />
            </intent-filter>
        </activity>
```

More information on [data element] (https://developer.android.com/guide/topics/manifest/data-element.html).

## Handling the response from Smartcar Auth library

To receive the response from the Smartcar Auth library, your app must implement the SmartcarCallback interface.
The interface defines a single method to handle the response. The response is packed into the SmartcarResponse object,
and include two variables: `code` and `message`. `code` contains the actual authorization code, and `message`
contains error messages if any.

Sample code:

```
    class MyCallback implements SmartcarCallback {
        public void handleResponse(SmartcarResponse smartcarResponse) {
            String code = smartcarResponse.getCode();
            String message = smartcarResponse.getMessage();
            String response = (code == null) ? message : code;
            Log.d(LOGTAG, "Response code " + code);
            Log.d(LOGTAG, "Response message " + message);
        }
    }
```

[ci-image]: https://travis-ci.com/smartcar/android-sdk.svg?token=6Yrkze1DNb8WHnHxrCy6&branch=master
[ci-url]: https://travis-ci.com/smartcar/android-sdk

[coverage-image]: https://codecov.io/gh/smartcar/android-sdk/branch/master/graph/badge.svg?token=RhacvrisiW
[coverage-url]: https://codecov.io/gh/smartcar/android-sdk
