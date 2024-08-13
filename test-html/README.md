Test pages for OAuth capture SDK. Serve with `python3 -m http.server`.
The test page can be loaded in the Smartcar SDK by using `smartcarAuth.launchAuthFlow(applicationContext, "http://<local ip>:<port>/")`.
```
index.html - communication with SDK via javascript interface
authorize.html - simulated OAuth authorization page
```
