/*
Copyright (c) 2017 Divested Computing Group

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

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WebView mapsWebView = null;
    private WebSettings mapsWebSettings = null;
    private CookieManager mapsCookieManager = null;

    private static ArrayList<String> allowedDomains = new ArrayList<String>();
    private static ArrayList<String> blockedURLs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the WebView
        mapsWebView = (WebView) findViewById(R.id.mapsWebView);

        //Set cookie options
        mapsCookieManager = CookieManager.getInstance();
        mapsCookieManager.setAcceptCookie(true);
        mapsCookieManager.setAcceptThirdPartyCookies(mapsWebView, false);

        //Restrict what gets loaded
        initURLs();
        mapsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(!request.getUrl().toString().startsWith("https://")) {
                    Log.d("Maps", "[NON-HTTPS] Blocked access to " + request.getUrl().toString());
                    return true; //Deny non HTTPS
                }
                boolean allowed = false;
                for (String url : allowedDomains) {
                    if (request.getUrl().getHost().contains(url)) {
                        allowed = true;
                    }
                }
                if(!allowed) {
                    Log.d("Maps", "[NOT WHITELISTED] Blocked access to " + request.getUrl().getHost());
                    return true;//Deny non-whitelisted domains
                }
                for (String url : blockedURLs) {
                    if (request.getUrl().toString().contains(url)) {
                        Log.d("Maps", "[BLACKLISTED] Blocked access to " + request.getUrl().toString());
                        return true;//Deny blacklisted URLs
                    }
                }
                return false;
            }
        });

        //Give lcoation access
        mapsWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if(origin.contains("google.com")) {
                    callback.invoke(origin, true, false);
                }
            }
        });

        //Set more options
        mapsWebSettings = mapsWebView.getSettings();
        //Enable some WebView features
        mapsWebSettings.setJavaScriptEnabled(true);
        mapsWebSettings.setAppCacheEnabled(true);
        mapsWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mapsWebSettings.setGeolocationEnabled(true);
        //Disable some WebView features
        mapsWebSettings.setAllowContentAccess(false);
        mapsWebSettings.setAllowFileAccess(false);
        mapsWebSettings.setBuiltInZoomControls(false);
        mapsWebSettings.setDatabaseEnabled(false);
        mapsWebSettings.setDisplayZoomControls(false);
        mapsWebSettings.setDomStorageEnabled(false);
        //Change the User-Agent
        mapsWebSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0.1; Nexus 6P Build/MHC19Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.89 Mobile Safari/537.36");

        //Load Google Maps
        mapsWebView.loadUrl("https://www.google.com/maps");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapsWebView.clearCache(true);
        mapsWebView.clearFormData();
        mapsWebView.clearHistory();
        mapsWebView.clearMatches();
        mapsWebView.clearSslPreferences();
        mapsCookieManager.removeAllCookie();
    }

    private static void initURLs() {
        //Whitelisted Domains
        allowedDomains.add("apis.google.com");
        //allowedDomains.add("fonts.gstatic.com");
        allowedDomains.add("maps.gstatic.com");
        allowedDomains.add("ssl.gstatic.com");
        allowedDomains.add("www.google.com");
        allowedDomains.add("www.gstatic.com");

        //Blacklisted Domains
        blockedURLs.add("analytics.google.com");
        blockedURLs.add("clientmetrics-pa.googleapis.com");
        blockedURLs.add("doubleclick.com");
        blockedURLs.add("doubleclick.net");
        blockedURLs.add("googleadservices.com");
        blockedURLs.add("google-analytics.com");
        blockedURLs.add("googlesyndication.com");
        blockedURLs.add("pagead.l.google.com");
        blockedURLs.add("partnerad.l.google.com");
        blockedURLs.add("video-stats.video.google.com");
        blockedURLs.add("wintricksbanner.googlepages.com");
        blockedURLs.add("www-google-analytics.l.google.com");

        //Blacklisted URLs
        blockedURLs.add("google.com/maps/preview/log204");
        blockedURLs.add("google.com/gen_204");
    }
}
