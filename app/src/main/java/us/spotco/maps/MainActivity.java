/*
Copyright (c) 2017-2019 Divested Computing Group

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package us.spotco.maps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView mapsWebView = null;
    private WebSettings mapsWebSettings = null;
    private CookieManager mapsCookieManager = null;

    private static ArrayList<String> allowedDomains = new ArrayList<String>();
    private static ArrayList<String> blockedURLs = new ArrayList<String>();

    private static final DateFormat consentDateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final String TAG = "GMapsWV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        String urlToLoad = "https://www.google.com/maps";

        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();
            urlToLoad = data.toString();
        } catch (Exception e) {
            Log.d(TAG, "No or Invalid URL passed. Opening homepage instead.");
        }

        //Create the WebView
        mapsWebView = (WebView) findViewById(R.id.mapsWebView);

        //Set cookie options
        mapsCookieManager = CookieManager.getInstance();
        mapsCookieManager.setAcceptCookie(true);
        mapsCookieManager.setAcceptThirdPartyCookies(mapsWebView, false);

        //Delete anything from previous sessions
        resetWebView();

        //Set the consent cookie to prevent unnecessary redirections
        setConsentCookie();

        //Restrict what gets loaded
        initURLs();
        mapsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().startsWith("tel:")) {
                    Intent dial = new Intent(Intent.ACTION_DIAL, request.getUrl());
                    startActivity(dial);
                    return true;
                }
                if (!request.getUrl().toString().startsWith("https://")) {
                    Log.d(TAG, "[NON-HTTPS] Blocked access to " + request.getUrl().toString());
                    return true; //Deny URLs that aren't HTTPS
                }
                boolean allowed = false;
                for (String url : allowedDomains) {
                    if (request.getUrl().getHost().contains(url)) {
                        allowed = true;
                    }
                }
                if (!allowed) {
                    Log.d(TAG, "[NOT ON ALLOWLIST] Blocked access to " + request.getUrl().getHost());
                    return true;//Deny URLs not on ALLOWLIST
                }
                for (String url : blockedURLs) {
                    if (request.getUrl().toString().contains(url)) {
                        Log.d(TAG, "[ON DENYLIST] Blocked access to " + request.getUrl().toString());
                        return true;//Deny URLs on DENYLIST
                    }
                }
                return false;
            }
        });

        //Give location access
        mapsWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (origin.contains("google.com")) {
                    callback.invoke(origin, true, false);
                }
            }
        });

        //Set more options
        mapsWebSettings = mapsWebView.getSettings();
        //Enable some WebView features
        mapsWebSettings.setJavaScriptEnabled(true);
        mapsWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mapsWebSettings.setGeolocationEnabled(true);
        //Disable some WebView features
        mapsWebSettings.setAppCacheEnabled(false);
        mapsWebSettings.setAllowContentAccess(false);
        mapsWebSettings.setAllowFileAccess(false);
        mapsWebSettings.setBuiltInZoomControls(false);
        mapsWebSettings.setDatabaseEnabled(false);
        mapsWebSettings.setDisplayZoomControls(false);
        mapsWebSettings.setDomStorageEnabled(false);
        mapsWebSettings.setSaveFormData(false);
        //Change the User-Agent
        mapsWebSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 11; Unspecified Device) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.82 Mobile Safari/537.36");

        //Load Google Maps
        mapsWebView.loadUrl(urlToLoad);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Credit (CC BY-SA 3.0): https://stackoverflow.com/a/6077173
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mapsWebView.canGoBack()) {
                        mapsWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void resetWebView() {
        //mapsWebView.clearCache(true);
        mapsWebView.clearFormData();
        mapsWebView.clearHistory();
        mapsWebView.clearMatches();
        mapsWebView.clearSslPreferences();
        mapsCookieManager.removeAllCookie();
    }

    private void setConsentCookie() {
        String consentDate = consentDateFormat.format(System.currentTimeMillis());
        Random random = new Random();
        int random2digit = random.nextInt(2) + 15;
        int random3digit = random.nextInt(999);
        String consentCookie = "YES+cb." + consentDate + "-" + random2digit + "-p1.en+F+" + random3digit;
        mapsCookieManager.setCookie(".google.com", "CONSENT=" + consentCookie + ";");
        //mapsCookieManager.setCookie(".google.com", "CONSENT=PENDING+" + random3digit + ";"); //alternative
        mapsCookieManager.setCookie(".google.com", "ANID=OPT_OUT;");
    }

    private static void initURLs() {
        //Allowed Domains
        allowedDomains.add("apis.google.com");
        allowedDomains.add("fonts.gstatic.com");
        allowedDomains.add("maps.gstatic.com");
        allowedDomains.add("ssl.gstatic.com");
        allowedDomains.add("www.google.com");
        allowedDomains.add("www.gstatic.com");
        allowedDomains.add("consent.google.com");
        allowedDomains.add("consent.google."); //TODO: better cctld handling
        allowedDomains.add("consent.youtube.com"); //XXX: Maybe not required?
        allowedDomains.add("maps.google.com");

        //Blocked Domains
        blockedURLs.add("analytics.google.com");
        blockedURLs.add("clientmetrics-pa.googleapis.com");
        blockedURLs.add("doubleclick.com");
        blockedURLs.add("doubleclick.net");
        blockedURLs.add("googleadservices.com");
        blockedURLs.add("google-analytics.com");
        blockedURLs.add("googlesyndication.com");
        blockedURLs.add("tpc.googlesyndication.com");
        blockedURLs.add("pagead.l.google.com");
        blockedURLs.add("partnerad.l.google.com");
        blockedURLs.add("video-stats.video.google.com");
        blockedURLs.add("wintricksbanner.googlepages.com");
        blockedURLs.add("www-google-analytics.l.google.com");
        blockedURLs.add("gstaticadssl.l.google.com");

        //Blocked URLs
        blockedURLs.add("google.com/maps/preview/log204");
        blockedURLs.add("google.com/gen_204");
        blockedURLs.add("play.google.com/log");
        blockedURLs.add("/gen_204?");
        blockedURLs.add("/log204?");

    }
}
