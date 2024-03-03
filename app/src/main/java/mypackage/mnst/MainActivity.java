package mypackage.mnst;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity  {
    Context context;

    WebView webView;
    TreeMap<String,String> treeMap_requestheader;
    public static final int MY_FILE_SELECTOR_REQUEST = 1;
    private ValueCallback mFilePathCallback;
//실제 단말에서만 동영상 로딩됨.

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MyLibrary.BioMetric.fingerPrinting((Context) this,"MNST뱅킹","인증에 실패하였습니다.");
        setContentView(R.layout.activity_main);

       // Log.d("myproperty",System.getProperties().toString().replaceAll(",","\n").replaceAll("[{}]",""));


        //Log.d("TAG", MyLibrary.f_exec(getApplicationContext(),"ls -al") );

        //웹에서 새창으로 넘겨줘야 함.
        /*
            startActivity(
                    new Intent(getApplicationContext() , Fragment_2.class) // test.class 로 넘어감.
            );

        */

        this.context = getApplicationContext();

        treeMap_requestheader = new TreeMap<>();

        webView = (WebView) findViewById(R.id.webView_id);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_LONG);
                // 파일 다운로드 처리 로직을 추가합니다.
                WebView webView = findViewById(R.id.webView_id);
                webView.loadUrl(url); // 다운로드 링크 url 주소가 들어감.
                Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_LONG);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView2, WebResourceRequest webResourceRequest) {
                super.shouldOverrideUrlLoading(webView2, webResourceRequest);

                webView2.loadUrl(webResourceRequest.getUrl().toString());
                return true;
                //return true; // 리다이렉션 허용
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);

                switch (webResourceError.getErrorCode()){
                    case WebViewClient.ERROR_FAILED_SSL_HANDSHAKE:
                        Toast.makeText(getApplicationContext() , "ssl 키 교환 실패",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_BAD_URL:
                        Toast.makeText(getApplicationContext() , "잘못된 url ",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_AUTHENTICATION:
                        Toast.makeText(getApplicationContext() , "유저인증실패",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_PROXY_AUTHENTICATION:
                        Toast.makeText(getApplicationContext() , "proxy 인증 실패 ",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME:
                        Toast.makeText(getApplicationContext() , "인증되지않는 스키마://",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_UNSUPPORTED_SCHEME:
                        Toast.makeText(getApplicationContext() , "지원되지않는 스키마://",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_FILE:
                        Toast.makeText(getApplicationContext() , "내부 파일 에러 ",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_FILE_NOT_FOUND:
                        Toast.makeText(getApplicationContext() , "파일 못찾음.",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_HOST_LOOKUP:
                        Toast.makeText(getApplicationContext() , "(호스트|서버) 찾을 수 없음 ",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_IO:
                        Toast.makeText(getApplicationContext() , "네트워크 IO 에러",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_TIMEOUT:
                        Toast.makeText(getApplicationContext() , "JS 로딩 중 TIMEOUT 발생.",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_UNSAFE_RESOURCE:
                        Toast.makeText(getApplicationContext() , "안전하지 않은 리소스",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_REDIRECT_LOOP:
                        Toast.makeText(getApplicationContext() , "Redirection: 너무 많이 발생.",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_TOO_MANY_REQUESTS:
                        Toast.makeText(getApplicationContext() , ".html 로드 중 추가 요청이 한계를 넘음.",Toast.LENGTH_LONG).show();
                        break;
                    case WebViewClient.ERROR_CONNECT:
                        Toast.makeText(getApplicationContext() , "연결 실패",Toast.LENGTH_LONG).show();
                        break;

                    case WebViewClient.ERROR_UNKNOWN:
                        Toast.makeText(getApplicationContext() , "알 수 없는 에러",Toast.LENGTH_LONG).show();
                        break;

                    default:
                        Toast.makeText(getApplicationContext() , "알 수 없는 에러",Toast.LENGTH_LONG).show();
                        break;
                }

            }
        });
        webView.setWebChromeClient(
                new WebChromeClient() {

                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        return super.onJsAlert(view, url, message, result);
                    }

                    @Override
                    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                        //if(confirm("확인취소")){}
                        return super.onJsConfirm(view, url, message, result);
                    }

                    @Override
                    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                        //prompt("메세지");
                        return super.onJsPrompt(view, url, message, defaultValue, result);

                    }

                    @Override
                    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                        return super.onJsBeforeUnload(view, url, message, result);
                    }

                    @Override
                    public boolean onJsTimeout() { //setTimeout(ms초 , function(e){});
                        return super.onJsTimeout();
                    }



                    @Override
                    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                        WebView newWebView = new WebView(MainActivity.this);
                        WebSettings webSettings = mySetWebSettings(newWebView.getSettings());
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(newWebView); dialog.show();
                        newWebView.setWebChromeClient(new WebChromeClient() {
                            @Override
                            public void onCloseWindow(WebView window) {
                                dialog.dismiss();
                            }
                        });
                        ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
                        resultMsg.sendToTarget();
                        return true;
                    }

                    // 파일업로드 input.type='file' 인 input 태그를 클릭했을 때 창을 열어줌.
                    @Override
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        mFilePathCallback = filePathCallback;
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                        // 여러장의 사진을 선택하는 경우
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");

                        startActivityForResult(Intent.createChooser(intent, "Select File"), MY_FILE_SELECTOR_REQUEST);
                        return true;
                    }
                }
        );
        WebSettings webSettings = mySetWebSettings(webView.getSettings());


        // post 자동 로그인
        /*
        String url = "192.168.0.134:9999/member/login";
        String _csrf = "2393241f-12bf-4b3e-bc12-6b92bcc15e3c";
        String myid = getResources().getString(R.string.myid);
        String mypw = getResources().getString(R.string.mypw);
        String params = "_csrf=" + _csrf + "myid=" + myid + "&" + "mypw=" + mypw;

        Log.d("myid="+myid+"\nmypw="+mypw,"MYLOG");
*/
        //webView.loadUrl("https://mnstbank.com/");

        // 파일 다운로드 이벤트를 감지하는 DownloadListener 설정
        webView.setDownloadListener(new DownloadListener(){
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // 파일 다운로드 처리 로직
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = android.webkit.CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        URLUtil.guessFileName(url, contentDisposition, mimeType)
                );

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (downloadManager != null) {
                    downloadManager.enqueue(request);
                }
            }
        });

        webView.loadUrl("http://192.168.0.134:8085/");
        /*
        webView.evaluateJavascript("alert(`1`);", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // 실행 결과를 받는 부분, 여기서는 결과를 사용하지 않으므로 비워둠
            }
        });
        */

        //webView.postUrl(url,params.getBytes());

        //webView.loadUrl("192.168.0.134:9999/member/login");

        // WebView에 POST 요청 전송
        //webView.postUrl("192.168.0.134:9999/member/login", "param1=value1&param2=value2".getBytes());

        //this.webView.loadUrl("javascript:자바스크립트;실행;가능힘; ajax 또는 webSocket 으로 cookie 값 전달 ㄱ;");
        //this.webView.loadUrl("https://주소 호출 시 창에 뜸?");

    }


    public WebSettings mySetWebSettings(WebSettings webSettings){

/*
        String userAgent = webSettings.getUserAgentString();
        Log.d("LOGD","userAgent => "+userAgent);
        //Mozilla/5.0 (Linux; Android 8.0.0; SM-G950N Build/R16NW; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
        // Chrome/86.0.4240.99 Mobile Safari/537.36

        webSettings.setUserAgentString("setUserAgentString");
        Log.d("LOGD","setUserAgentString => "+webSettings.getUserAgentString());
        //setUserAgentString
*/

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //멀티뷰 허용할거냐




        webSettings.setSupportMultipleWindows(true);
        // 새(여러)창 띄우기 허용할거냐

        webSettings.setLoadWithOverviewMode(true); // 메타태그 허용할거냐
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추는거 허용할거냐
        webSettings.setSupportZoom(true); // 화면 줌 허용할거냐
        webSettings.setDisplayZoomControls(true);

        webSettings.setBuiltInZoomControls(true); // 화면 축소 가능하게 할거냐
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈를 맞춤

        webSettings.setLightTouchEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(true);
        }

        // =================== 위험권한들 =====================
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 브라우저 캐시 속성 확인할 것 !!
        //webSettings.setAppCacheEnabled(true); // 안드로이드 앱 캐시허용

        webSettings.setDatabaseEnabled(true); // DB 권한 가능하게 할거냐

        webSettings.setDomStorageEnabled(true); // 로컬저장소 허용할거냐
        webSettings.setAllowContentAccess(true); // 안드로이드 컨텐트 허용할거냐
        webSettings.setAllowFileAccess(true); // 허락할거냐 파일 접근을
        webSettings.setAllowFileAccessFromFileURLs(true); //허락할거냐 파일 접근 폼 파일의 URL을
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setGeolocationEnabled(true); // 취약
        webSettings.setSaveFormData(true); // 취약
        webSettings.setSavePassword(true); // 취약

        return webSettings;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_FILE_SELECTOR_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Uri[] uris = new Uri[count];
                    for (int i = 0; i < count; i++) {
                        uris[i] = data.getClipData().getItemAt(i).getUri();
                    }
                    mFilePathCallback.onReceiveValue(uris);
                } else if (data.getData() != null) {
                    mFilePathCallback.onReceiveValue((new Uri[]{data.getData()}));
                }
            }
        }
    }
}