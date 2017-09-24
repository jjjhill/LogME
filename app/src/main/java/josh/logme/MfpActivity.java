package josh.logme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MfpActivity extends AppCompatActivity {
    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfp);

        webView = (WebView) findViewById(R.id.mfpView);
        webView.setVisibility(View.GONE);
        webView  = new WebView(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("http://www.myfitnesspal.com/food/calorie-chart-nutrition-facts");
        setContentView(webView);
    }
}
