# Smartcar Android Auth SDK [![Build Status][ci-image]][ci-url] [![Coverage][coverage-image]][coverage-url] [![Download][bintray-image]][bintray-url]

The Smartcar Android Auth SDK makes it easy to handle the Smartcar OAuth flow from
Android devices. It provides a library for authenticating the vehicle-owner and
fetching the authorization code that can be used to fetch an access token using
a separate request.

## Resources

* [Smartcar Developer Dashboard](https://developer.smartcar.com)
* [Smartcar API Reference](https://smartcar.com/docs)
* [Sample App](https://github.com/smartcar/android-sdk-example)

## Installation

The recommended method for obtaining the SDK is via Gradle or Maven.

### Gradle
```groovy
compile "com.smartcar.sdk:smartcar-auth:1.0.4"
```

### Maven
```xml
<dependency>
  <groupId>com.smartcar.sdk</groupId>
  <artifactId>smartcar-auth</artifactId>
  <version>1.0.4</version>
</dependency>
```

# Usage

Refer to the [Sample App](https://github.com/smartcar/android-sdk-example) for a quick review.
Otherwise, follow these steps:

1. [Manage your application's configuration settings](#application-configuration-settings)

2. [Handle the response from the Smartcar Auth library](#handling-the-response-from-smartcar-auth-library)

3. [Handle the redirect from the Smartcar service](#handling-the-redirect)

4. Instantiate a new SmartcarAuth object
```
    SmartcarAuth smartcarAuth = new SmartcarAuth(callback, CLIENT_ID, REDIRECT_URI, SCOPE, ApprovalPrompt.force, true);
```

5. Create a View and attach the Smartcar Auth library's click handler
```
    smartcarAuth.addClickHandler(getApplicationContext(), connectButton);
```

6. Test your Android application. Clicking on the View should trigger open a WebView for
the vehicle-owner to pick the vehicle brand, input the owner crendentials, and appprove your
application to access the requested permissions. When this is done, Smartcar's services will
redirect back to your REDIRECT_URI which will be your Android application. Based on the sample
code here, the `code` returned by the Smartcar service will be logged by your application.
You can then use this code to exchange for a Smartcar access_token. For details on the
exchange, refer to the [Smartcar documentation](https://smartcar.com/docs)

### Application Configuration Settings

`CLIENT_ID`

Application client ID obtained from [Smartcar Developer Portal](https://developer.smartcar.com/).

`REDIRECT_URI`

Your app must register a custom URI scheme with Android in order to receive the
authorization callback. Smartcar requires the custom URI scheme to be in the
format of `"sc" + clientId + "://" + hostname`. This URI must also be registered
in [Smartcar Developer Dashboard](https://developer.smartcar.com) for your app.
You may append an optional path component or
TLD (e.g. `sc4a1b01e5-0497-417c-a30e-6df6ba33ba46://oauth2redirect.com/page`).

`SCOPE`

Permissions requested from the user for specific grant.
See the [Smartcar API reference](https://smartcar.com/docs)
for permissions required for each API request.


### Handling the response from Smartcar Auth library

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

    MyCallback callback = new MyCallback();
```


### Handling the Redirect

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

More information on [data element](https://developer.android.com/guide/topics/manifest/data-element.html).


[ci-image]: https://travis-ci.com/smartcar/android-sdk.svg?token=6Yrkze1DNb8WHnHxrCy6&branch=master
[ci-url]: https://travis-ci.com/smartcar/android-sdk

[coverage-image]: https://codecov.io/gh/smartcar/android-sdk/branch/master/graph/badge.svg?token=RhacvrisiW
[coverage-url]: https://codecov.io/gh/smartcar/android-sdk

[bintray-image]: https://api.bintray.com/packages/smartcar/library/smartcar-auth/images/download.svg
[bintray-url]: https://bintray.com/smartcar/library/smartcar-auth/_latestVersion