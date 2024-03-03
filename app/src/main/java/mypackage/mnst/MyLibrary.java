package mypackage.mnst;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyLibrary { // static 안됨.
    // 브포; 쉬 f9; -> f7 step into ; f8 step over;
    // 컨 쉬 / 안될 시  =>   컨 쉬 로 Micro 입력기 로 변경 한 뒤 ㄱ
    // 컨 쉬 알;(한번에 문자열을 문자열로 변경.)
    // 컨 쉬 f; 전체 파일에서 문자열 찾음.
    public static final int DIAL_EDITTEXT=1;
    static EditText editText; static TextWatcher textWatcher; static boolean bool = true; static android.graphics.Bitmap bitmap; static String cmd_result="";
    static ImageView imageView;
    static HttpURLConnection httpURLConnection; static String urls="\n";
    //MyParser _parser;
    public static String
            os_name = "" , crlf = "" , charset = "", cmd_or_bash = "" ,
            프젝디렉루트경로 = "E:/C_drive_backup/tool/my_eclipse/eclipse-workstation/MyLibraryAndTools";
    ;


    public static List<String> list_cmd_or_bash_args = new ArrayList<String>();
    public static String[] strings_cmd_or_bash_args;


    static {
        f_set_base(); // MyLibrary.f_static메소드(); 할 때 등.. 클래스만 사용해도 수행됨.
    }

    public MyLibrary() { f_set_base(); }

    public static boolean f_check_valid(Object obj) {
        if (obj == null) return false;
        if (obj instanceof String) {
            String str = (String) obj;
            if(str.isEmpty() || str.equals("") || str.length() == 0 || str.equals("null")) return false;
        }
        if (obj instanceof Boolean && Boolean.FALSE.equals(obj)) return false;
        //if (obj instanceof Boolean) return (Boolean) obj; // true 일 시 true , false 일 시 false
        if (obj instanceof Collection && (((Collection) obj).isEmpty())  ) return false;
        if (obj.getClass().isArray() && (java.lang.reflect.Array.getLength(obj) == 0) ) return false;
        if (obj instanceof Number) {
            Number num = (Number) obj;
            return !Double.isNaN(num.doubleValue());
        }

        return true;
    }



    public static void f_set_base() {
        os_name = System.getProperty("os.name").toLowerCase();
        if(os_name.startsWith("window")) {
            charset = "CP949"; //!! 송신할때만 쓰는것임. sysout 으로 buf.readline 할때는 UTF-8임.
            crlf = "\r\n";
            cmd_or_bash = "cmd /C ";

            list_cmd_or_bash_args.add("cmd");
            list_cmd_or_bash_args.add("/C");

        }else if(os_name.startsWith("linux")) {
            charset = "UTF-8";
            crlf = "\n";
            cmd_or_bash = "/bin/bash -c ";

            list_cmd_or_bash_args.add("/bin/bash -c ");




        }else {
            charset = "UTF-8";
            crlf = "\n";
        }
    }

    public static String f_bufferedReader(InputStream inputStream , String arg2_charset) {
        //f_set_base();
        String result = "";
        try {
            BufferedReader bufferedReader = null;
            if(MyLibrary.f_check_valid(arg2_charset)) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream , arg2_charset));
            }else {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));
            }

            for(String 리드라인;(리드라인 = bufferedReader.readLine()) != null;) {
                result += 리드라인 + crlf;
            }
            bufferedReader.close(); // bufferedReader.close() 할 시 해당 inputStream 의 객체도 close() 됨. 실시간 연결 stream 일 시 현 함수 사용 x. MyLibrary.Network.Manager.f_bufferedReader(socket.getInputStream()); 사용 ㄱ
        } catch (Exception e) {e.printStackTrace();}

        System.out.println(result);

        return result;
    }

    public static void f_bufferedWriter(OutputStream outputStream , String write될문자열) {
        //f_set_base();
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(write될문자열 + crlf);
            bufferedWriter.flush();
            bufferedWriter.close(); // bufferedWriter.close() 할 시 해당 inputStream 의 객체도 close() 됨. 실시간 연결 stream 일 시 현 함수 사용 x. MyLibrary.Network.Manager.f_bufferedWriter(socket.getOutputStream() , "쓰기" + MyLibrary.crlf); 사용 ㄱ
        } catch (Exception e) {e.printStackTrace();}
    }

    public static String f_exec(Context context , String p_command) { //     "/절대경로/실파" 또는 cmd_or_bash + "실파.exe /옵션 값 /옵션 값 && 실파 /옵션 값"

		/*
 			> 기호로 인해 입출력 리다이렉션이 원하는 대로 동작하지 않기 때문.
 			Runtime.getRuntime().exec()를 사용할 때는 명령어를 배열로 전달하거나,
 			셸을 사용하지 않고 직접 프로세스를 조작하여 실행하는 것이 좋습니다.
		*/
        // powershell 환변 등록 후 사용 구현 ㄱ
        Process process = null;
        try {

            if(p_command.contains(cmd_or_bash)) {
                //command+=crlf;
                process = Runtime.getRuntime().exec(p_command + crlf);
            }else if(System.getProperties().toString().toLowerCase().contains("android")){
                //su 있다면 su 루팅탐지우회 후 su 실행 없다면 그냥 실행시킬 것.
                process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh" , "-c" , p_command } );

            }else if(! p_command.contains(cmd_or_bash) && os_name.startsWith("linux")){

                //p_command=cmd_or_bash + p_command + crlf;
                //strings_cmd_or_bash_args[0] = "/usr/bin/bash";
                //strings_cmd_or_bash_args[1]="-c";
                //strings_cmd_or_bash_args[2]=p_command + crlf;

                process = Runtime.getRuntime().exec(new String[]{"/usr/bin/bash" , "-c" , p_command } );

            }
            //System.out.println("f_exec 함수 내에서 실행됨. p_command => " + strings_cmd_or_bash_args.toString());

            String result = "";
            int process_waitFor_상태코드 = process.waitFor();


            int process_exitValue_종료코드 = process.exitValue();
            System.out.println("process_exitValue_종료코드 = " + process_exitValue_종료코드);

            if(process_waitFor_상태코드 == 0) { // !! 수정해야됨. 출력된 후 입력대기 : 뜰 시 프로세스 안끝났으므로 수행안됨.
                BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , "UTF-8"));

                for(String readline;(readline = bufferedReader_input.readLine()) != null;) {
                    result += readline + crlf;
                }

                if(! System.getProperties().toString().toLowerCase().contains("android")){
                    bufferedReader_input.close();
                    process.destroyForcibly(); // 안해줄 시 f_exec("cmd -C 명령어"); 때마다 이어서 사용됨?
                }
                return result;
            }else if(process_waitFor_상태코드 >= 1 || process_waitFor_상태코드 == -1) {
                BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , "UTF-8"));
                for(String string;(string = bufferedReader_error.readLine()) != null;) { //에러는 출력안됨. 이유모름. 밑에명령어들 모두 수행됨. 출력만안됨.
                    result += string + crlf;
                }
                bufferedReader_error.close();
                System.out.println(result);
                System.out.println("process_waitFor_상태코드 = " + process_waitFor_상태코드);
                System.out.println("process_waitFor_상태코드 >= 1 ps 비정상 종료");
                System.out.println("process_waitFor_상태코드 == -1 ps 종료코드 알 수 없음");
            }

        } catch (Exception e) {e.printStackTrace();}

        process.destroyForcibly();
        return null;
    }

    public static Process f_runtime_exec(String command) { // "/절대경로/실파" 또는 "cmd /C 실파.exe /옵션 값 /옵션 값 && 실파 /옵션 값"
        // powershell 환변 등록 후 사용 구현 ㄱ
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            bufferedWriter.write(command + crlf);
            bufferedWriter.flush();
            bufferedWriter.close();

            String result = "";
            int process_waitFor_상태코드 = process.waitFor();

            result += command + crlf;
            if(process_waitFor_상태코드 == 0) { // !! 수정해야됨. 출력된 후 입력대기 : 뜰 시 프로세스 안끝났으므로 수행안됨.
                BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , "UTF-8"));
                for(String string;(string = bufferedReader_input.readLine()) != null;) {
                    result += string + crlf;
                }
                bufferedReader_input.close();
                process.destroyForcibly(); // 안해줄 시 f_exec("cmd -C 명령어"); 때마다 이어서 사용됨?
                return process;
            }else if(process_waitFor_상태코드 >= 1 || process_waitFor_상태코드 == -1) {
                BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , "UTF-8"));
                for(String string;(string = bufferedReader_error.readLine()) != null;) { //에러는 출력안됨. 이유모름. 밑에명령어들 모두 수행됨. 출력만안됨.
                    result += string + crlf;
                }
                bufferedReader_error.close();
                System.out.println("process_waitFor_상태코드 = " + process_waitFor_상태코드);
                System.out.println("process_waitFor_상태코드 >= 1 ps 비정상 종료");
                System.out.println("process_waitFor_상태코드 == -1 ps 종료코드 알 수 없음");
            }

        } catch (Exception e) {e.printStackTrace();}

        process.destroyForcibly();
        return null;
    }

    public static void startRuntimePrintln(Process process) { // MyLibrary.startRuntimePrintln( MyLibrary.Network.Mitm.Proxy.start(proxy_port,null) );
        // 실시간으로 출력해줌.
        try {
            Thread thread_input = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader bufferedReader_input = new BufferedReader(new InputStreamReader(process.getInputStream() , "MS949"));

                        for(String readline = null; (readline = bufferedReader_input.readLine()) != null;) {
                            System.out.println(readline);
                        }


                    } catch (Exception e) { e.printStackTrace(); }

                }
            });
            thread_input.start();

            Thread thread_error = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        BufferedReader bufferedReader_error = new BufferedReader(new InputStreamReader(process.getErrorStream() , "MS949"));


                        for(String readline = null; (readline = bufferedReader_error.readLine()) != null;) {
                            System.out.println(readline);
                        }

                    } catch (Exception e) { e.printStackTrace(); }

                }
            });
            thread_error.start();


        } catch (Exception e) {e.printStackTrace();}
    }
    public static String log(String msg){
        Log.d("MYLOG",msg);
        return msg;
    }

    public static Toast toast(Context context , String text , int duration ,int top_bottom_center,int x,int y){
        // toast = myLibrary.toast("메세지",Toast.LENGTH_LONG , Gravity.CENTER,x축,y축);


        Toast toast = new Toast(context);
        toast.setText(text);
        toast.setDuration(duration); //Toast.LENGTH_LONG
        toast.setGravity(top_bottom_center , x , y);
        toast.show();

        return toast;
    }

    public static Snackbar snackbar(Context context ,  View view , String text , int duration){
        // snackbar = myLibrary.snackbar(view , "message" , Snackbar.LENGTH_LONG);
        Snackbar snackbar = Snackbar.make(context , view ,text , duration);
        snackbar.show();
        return snackbar;
    }

    public static String toastLong(Context context , String string){
        log(string);
        Toast.makeText(context,string, Toast.LENGTH_LONG).show();
        return string;
    }

    private static void clear(){
        try {
            editText=null; textWatcher=null;
            bool=true;  bitmap=null;
            System.gc();
        }catch (Exception e){
            log(e.getMessage());
            e.printStackTrace();
        }
    }


    public static AlertDialog.Builder alertDialogBuilder(Context context , @Nullable String alt_title, @Nullable String alt_message, @Nullable Integer option , String input_str , String check_str , String fault_str){
        // AlertDialog.Builder = myLibrary.alertDialogBuilder("타이틀바제목","메세지본문",1,"입력해주세요","확인되었습니다.","잘못 입력하였습니다.");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        try {
            alertDialogBuilder.setIcon(R.drawable.ic_launcher_background);
            log(alt_message);
            if(alt_message == null && alt_title == null){
                alertDialogBuilder.setTitle("AlertDialog");
                alertDialogBuilder.setMessage(alt_message);
            } else if(alt_message != null){
                alertDialogBuilder.setMessage(alt_message);

            } else if(alt_title != null){
                alertDialogBuilder.setTitle(alt_title);
            } else{
                toastLong(context,"message alt_title 비어있음.");
                return null;
            }
            //============= option 설정 부분 ===============
            if(option != null){
                switch(option){
                    case DIAL_EDITTEXT:
                        editText = new EditText(context);
                        editText.setHint(input_str);
                        editText.setBackgroundColor(Color.argb(30,0,216,255));
                        alertDialogBuilder.setView(editText);
                        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                toastLong(context,check_str);
                            }
                        });
                        break;
                    default:
                        toastLong(context,fault_str);
                        break;
                }
            }
            //=============================================
            alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    toastLong(context,check_str);
                }
            });
            alertDialogBuilder.create().show();
        }catch (Exception e){
            toastLong(context,e.getMessage());
            e.printStackTrace();
        }finally{
            clear();
        }

        return alertDialogBuilder;
    }

    public static class Permission{
        static int request_code=0;
        public static String request(Context context , String[] strings){
            //MyLibrary.Permission.request((Context)this,new String[]{Manafest.permission.권한1,Manafest.permission.권한2}); Manifest.permission.권한은 <manifest><permission name="권한"> 되어있어야 코드어시스트 보임.

            String result="";
            if(MyLibrary.f_check_valid(strings)){
                for(String string: strings){
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                        if(ContextCompat.checkSelfPermission(context,string) != PackageManager.PERMISSION_GRANTED){
                            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, string)){
                                ActivityCompat.requestPermissions((Activity) context , new String[]{ string } , request_code++);
                            }else{
                                result += "! ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, string)\n";
                            }
                        }else{
                            result += "ContextCompat.checkSelfPermission(context,string) == PackageManager.PERMISSION_GRANTED\n";
                        }
                    }else{
                        result += "Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1\n";
                    }
                }
            }
            return result;
        }

        public static String response(Context context , int requestCode,String[] strings){
            /*
                onRequestPermissionsResult(int requestCode , String[] permissions , int grantResults){
                    //안에서 호출 ㄱ
                   MyLibrary.Permission.response(context , MyLibrary.Permission.request_code , permissions);
                }

            */
            String result = "";
            for(int req_code=0;req_code<request_code;req_code++){
                if(requestCode == req_code){
                    for(String string: strings){
                        if(ContextCompat.checkSelfPermission(context,string) == PackageManager.PERMISSION_GRANTED){
                            MyLibrary.toastLong(context , "권한 허용됨.");
                            result = "권한 허용됨";
                        }else{
                            MyLibrary.toastLong(context , "권한 허용안됨");
                            result = "권한 허용안됨";
                        }
                    }
                }
            }
            return result;
        }
    }


    public class Bitmap{
        public android.graphics.Bitmap getImageParsing(final String url) throws Exception {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
                        httpURLConnection.setRequestProperty("user-agent",System.getProperty("http.agent"));
                        httpURLConnection.setReadTimeout(0);
                        httpURLConnection.connect();

                        bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            thread.start();
            while (true){
                if(thread.getState() == Thread.State.TERMINATED){
                    return bitmap;
                }
            }
        }
/*
*                try {

                      if(url_str_list.equals("")){
                        myBase.println(" url값이 비어있어 기본이미지로 변경됩니다. ");
                        textView.setText("");
                        String urls_str="";
                        String[] urls = {
                                "https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F24283C3858F778CA2EFABE"
                                ,"https://image.dongascience.com/Photo/2018/02/2829a272f41849c3a42013a0e321a236.jpg"
                                ,"https://ichef.bbci.co.uk/news/640/cpsprodpb/AD6E/production/_104889344_kitten.jpg"
                                ,"https://img9.yna.co.kr/etc/inner/KR/2019/04/08/AKR20190408066300073_01_i_P2.jpg"
                                ,"https://image-notepet.akamaized.net/resize/620x-/seimage/20200722%2Fc78376a631ffd4c6683e28036e01c133.jpg"
                                ,"https://media.mercola.com/imageserver/public/2020/june/feline-constipation-kr-saag.jpg"
                                ,"https://myanimals.com/ko/wp-content/uploads/2018/12/%EA%B3%A0%EC%96%91%EC%9D%B4-%ED%98%BC%EC%9E%90-%EB%86%94%EB%91%90%EA%B8%B0.jpg"
                                ,"https://ichef.bbci.co.uk/news/640/cpsprodpb/17805/production/_105016269_roxy1.jpg"
                                ,"https://www.ksilbo.co.kr/news/photo/202002/754450_425528_91.jpg"
                        };
                        bitmaps = new Bitmap[urls.length];
                        for (int i=0;i<urls.length;i++){
                            if( (urls[i].contains("http") || urls[i].contains("https"))) {
                                textView.append(urls[i]+"\n");
                                bitmaps[i] = myParser.getImageParsing(urls[i]);
                            }else{
                                textView.append("이 이미지 주소는 url 주소가 아니거나 base64 디코딩이 필요한 주소입니다. : \n" + urls[i]);
                            }
                        }
                        changeFragment(1);

                      } else if(!url_str_list.equals("")){
                          textView.setText("");
                          String[] urls = url_str_list.split("\n");
                          bitmaps = new Bitmap[urls.length];
                          for(int i=0; i<urls.length;i++){
                              if((urls[i].contains("http://") || urls[i].contains("https://"))){
                                  textView.append(urls[i]+"\n");
                                  bitmaps[i] = myParser.getImageParsing(urls[i]);
                              } else {
                                  textView.append("이 이미지 주소는 url 주소가 아니거나 base64 디코딩이 필요한 주소입니다. : \n" + urls[i]);
                              }

                          }
                          changeFragment(1);
                      } else {
                        myBase.println("url주소를 잘못입력하였습니다. 다시 확인해주세요.");
                    }



                }catch (Exception e){myBase.log(e.getMessage()+e.toString());}
*
*
* */

        public static String getAllImageUrlsParsing(Context context , final String url) throws InterruptedException {
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setRequestProperty("user-agent",System.getProperty("http.agent"));
                        httpURLConnection.setReadTimeout(0);

                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(
                                        httpURLConnection.getInputStream(),"UTF-8"
                                )
                        );

                        for(String readline;(readline=bufferedReader.readLine())!=null;){
                            if (readline.contains("<img")) {
                                urls += readline.split("src=\"")[1].split("\"")[0] + "\n";
                            }

                            if (readline.contains("href")) {
                                urls += readline.split("href=\"")[1].split("\"")[0] + "\n";
                            }

                            if (readline.contains("url(\"")) {
                                urls += readline.split("url\\(\"")[1].split("\"\\)")[0] + "\n";
                            }
                        }
                        log("\n\n\n url파싱 테스트 :: \n "+urls);
                        //myBase.log("urls = > " + urls);

                    }catch (Exception e){
                        e.printStackTrace();
                        log(e.getMessage()+e.toString());
                    }
                }
            };
            thread.start();
            thread.join();

            String return_value = urls;
            urls="\n";

            return return_value;
        }

    }




    AlertDialog.Builder alertDialogBuilder;
//
//    static Process process = null; static  String result = "";
//
//    //수정해야됨.
//    public static String f_exec(Context context ,final String cmdline) throws IOException, InterruptedException, TimeoutException, ExecutionException {
//        /*executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());*/
//        if(!cmdline.equals("")) {
//            try {
//                process = Runtime.getRuntime().exec(cmdline+"\n");
//                //==========================================================
//                Runnable runnable_input  = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            BufferedReader bufferedReader_input=null;
//                            bufferedReader_input = new BufferedReader(
//                                    new InputStreamReader(
//                                            process.getInputStream() , "UTF-8"
//                                    )
//                            );
//                            String readline=null;
//                            while ((readline=bufferedReader_input.readLine())!=null){
//                                result+=readline+"\n";
//                            }
//                            /*for(readline=bufferedReader_input.readLine(); readline != null;){
//                                result+=readline+"\n";
//                            }*/
//                            bufferedReader_input.close();
//                        }catch (Exception e){
//                            toastLong(context ,e.getMessage() + "\n\t" + e.toString());
//                            e.printStackTrace();
//                        }
//
//                    }
//                };
//                //==========================================================
//                Runnable runnable_error = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            BufferedReader bufferedReader_error=null;
//                            bufferedReader_error = new BufferedReader(
//                                    new InputStreamReader(
//                                            process.getErrorStream() , "UTF-8"
//                                    )
//                            );
//                            String readline=null;
//                            while ((readline=bufferedReader_error.readLine())!=null){
//                                result+=readline+"\n";
//                            }
//
//                            /*for(readline = bufferedReader_error.readLine(); readline != null;){
//                                result+=readline+"\n";
//                            }*/
//                            bufferedReader_error.close();
//                        }catch (Exception e){
//                            toastLong(context ,e.getMessage() + "\n\t" + e.toString());
//                            e.printStackTrace();
//                        }
//
//                    }
//                };
//
//
//                //==========================================================
//
///*                executorService.submit(runnable_input);
//                executorService.submit(runnable_error);
//                executorService.shutdown();
//
//                executorService.awaitTermination(1000,TimeUnit.MILLISECONDS);*/
//
//                Thread thread_input = new Thread(runnable_input);
//                Thread thread_error = new Thread(runnable_error);
//
//                thread_input.setDaemon(true);
//                thread_input.start();
//                thread_error.setDaemon(true);
//                thread_error.start();
//                while (true){
//                    if (thread_input.getState() == Thread.State.TERMINATED && thread_error.getState() == Thread.State.TERMINATED)
//                        break;
//                }
//
//            }catch (Exception e){
//                toastLong(context ,e.getMessage() + "\r\n" + e.toString());
//                e.printStackTrace();
//                clear();
//            }
//        }
//
//        String ret_result = result;
//        result="";
//        return ret_result;
//    }




    //mms전송
    public void sendMMS(Context context ,String phoneNumber_SENDTO , String sms_body , String EXTRA_STREAM_uri,String mime_type){
        //자동 전송은 안됨.
        if(!phoneNumber_SENDTO.contains("-")){
            Toast.makeText(context,"-로 분리시켜 입력해야합니다.\n\t ex :: 010-0000-0000",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("smsto:"+phoneNumber_SENDTO)
        );

        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(EXTRA_STREAM_uri));
        intent.putExtra("sms_body",sms_body);
        intent.setType(mime_type);

        context.startActivity(intent);

    }
    //문자전송
    public static void sendSMS(Context context , @NonNull String phoneNumber_SENDTO ,@Nullable String my_phoneNumber, String sms_body){
        // 폰번호 - 로 구분안함.
        if(my_phoneNumber == null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber_SENDTO, null, sms_body, null, null);

            toastLong(context ,"전송 완료 :: \n\t 폰번호 : " + phoneNumber_SENDTO + "\n\t문자내용 : " + sms_body + "\n");
        }

    }
    //문자내용을 모두 문자열로 반환
    public static String getAllSMS(Context context){
        Uri uri = Uri.parse("content://sms");
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                uri,
                new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, "date DESC"
        );

        String string = "";
        String result="";
        int count = 0;
        while (cursor.moveToNext()) {
            long 메세지id = cursor.getLong(0);
            long 스레드id = cursor.getLong(1);
            String 핸드폰번호_address = cursor.getString(2);
            long 사람_person = cursor.getLong(3);
            String 사람_person_str = cursor.getString(4);

            String 시간 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(cursor.getLong(4)).toString();
            String 문자내용 = cursor.getString(5);

            string = String.format(
                    "메세지id = %d, \n 스레드id = %d, \n 핸드폰번호_address = %s, \n 사람_person = %d, \n 사람_person_str = %s, \n 시간:%s, \n 문자내용 :: \n\t %s \n", 메세지id, 스레드id, 핸드폰번호_address, 사람_person,
                    사람_person_str, 시간, 문자내용);
            result+=string;
        }
        cursor.close();

        return result;
    }
    //전화번호부 모두 문자열로 반환
    public static String getAllContact(Context context){
        // 데이터베이스 혹은 content resolver 를 통해 가져온 데이터를 적재할 저장소를 먼저 정의

        // 1. Resolver 가져오기(데이터베이스 열어주기)
        // 전화번호부에 이미 만들어져 있는 ContentProvider 를 통해 데이터를 가져올 수 있음
        // 다른 앱에 데이터를 제공할 수 있도록 하고 싶으면 ContentProvider 를 설정
        // 핸드폰 기본 앱 들 중 데이터가 존재하는 앱들은 Content Provider 를 갖는다
        // ContentResolver 는 ContentProvider 를 가져오는 통신 수단
        ContentResolver contentResolver = context.getContentResolver();



        String[] strings = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 인덱스 값, 중복될 수 있음 -- 한 사람 번호가 여러개인 경우
                ,  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ,  ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // 4. ContentResolver로 쿼리를 날림 -> resolver 가 provider 에게 쿼리하겠다고 요청
        Cursor cursor = contentResolver.query(
                // 2. 전화번호가 저장되어 있는 테이블 주소값(Uri)
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                strings,
                null, null, null);

        // 4. 커서로 리턴된다. 반복문을 돌면서 cursor 에 담긴 데이터를 하나씩 추출
        String 결과="";

        if(cursor != null){
            while(cursor.moveToNext()){
                String 템프="";
                // 4.1 이름으로 인덱스를 찾아준다
                int idIndex = cursor.getColumnIndex(strings[0]); // 이름을 넣어주면 그 칼럼을 가져와준다.
                int nameIndex = cursor.getColumnIndex(strings[1]);
                int numberIndex = cursor.getColumnIndex(strings[2]);

                // 4.2 해당 index 를 사용해서 실제 값을 가져온다.
                String id = cursor.getString(idIndex);
                String 이름 = cursor.getString(nameIndex);
                String 전화번호 = cursor.getString(numberIndex);


                템프="id = "+id+"\n name = "+이름+"\n number = "+전화번호+"\n";
                결과 += 템프;
            }
        }
        // 데이터 계열은 반드시 닫아줘야 한다.
        cursor.close();
        return 결과;
    }
    //전화걸음
    public static void call(Context context , String phoneNumber){
        if(!phoneNumber.contains("-")){
            //"-로 분리시켜 입력해야합니다.\n\t ex :: 010-0000-0000"
            return;
        }
        toastLong(context ,"전화 완료 :: \n\t 폰번호 : "+phoneNumber);
        context.startActivity(
                new Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:"+phoneNumber)
                )
        );
    }



    public class EnDeCription{
        public class AES{
            private String symmetric_key;

            public AES(@NonNull String symmetric_key_128_192_256) { // 대칭키
                this.symmetric_key = symmetric_key_128_192_256;
            }

            public String encription(String plain_text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
                SecretKeySpec secretKeySpec = new SecretKeySpec(
                        this.symmetric_key.getBytes("UTF-8"),
                        "AES"
                );

                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE , secretKeySpec);
                byte[] bytes = cipher.doFinal(plain_text.getBytes("UTF-8"));
                bytes = Base64.encode(bytes,bytes.length);

                return new String(bytes).replaceAll("\r\n","");
            }

            public String decription(String cipher_text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
                SecretKeySpec secretKeySpec = new SecretKeySpec(
                        this.symmetric_key.getBytes(),
                        "AES"
                );
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE , secretKeySpec);
                byte[] bytes = cipher_text.getBytes("UTF-8");
                bytes = Base64.decode(bytes,bytes.length);


                return new String(
                        cipher.doFinal(bytes)
                ).replaceAll("\r\n","");
            }
        }

        public class RSA{
            private Key public_key;
            private Key private_key;

            public RSA() throws NoSuchAlgorithmException {
                /*
                 * 비대칭 키. 암호화 할 때마다 값이 바뀜. 즉 일정하지가 않고 랜덤함.
                 * 공개키 , 개인키 사용. -> 내부에서 키 생성함. (따로 지정 x) , (키 길이 2048bit)
                 * https <http , tls> 에 주로 사용됨.
                 * 키 공유 할 때 가로 챌 수 있음
                 * */
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048); // 키 길이 2048
                KeyPair keyPair = keyPairGenerator.genKeyPair(); // 일반적인 키 페어를 얻음.
                this.public_key = keyPair.getPublic(); // 공개키 반환.
                this.private_key = keyPair.getPrivate(); // 사설키 반환
            }

            public String encription(String plain_text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
                /* 암호화 할 때마다 값이 바뀜. <즉 고정값 x> */
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE , this.public_key);
                byte[] bytes = cipher.doFinal(plain_text.getBytes("UTF-8"));
                bytes = Base64.encode(bytes,bytes.length);
                return new String( bytes ).replaceAll("\r\n","");
            }
            public String decription(String cipher_text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, this.private_key);
                byte[] bytes = cipher_text.getBytes("UTF-8");

                byte[] bytes_decode = Base64.decode(bytes,bytes.length);

                return new String(
                        cipher.doFinal(bytes_decode)
                );
            }
        }
        public class MD5{
            /* 스푸핑 방지용
             *  128bit 단방향 암호 해쉬함수
             * 고속연산 -> 충돌가능성 낮음
             * 넽 전송되는 파일 무결성 확인 , 인증(비밀번호)에 주로 사용됨.<DB저장될 때 ㄱ>
             * */
            public MD5(){ }
            public String encription(String plain_text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
                byte[] bytes = plain_text.getBytes("UTF-8");
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(bytes);
                bytes = messageDigest.digest();
                bytes = Base64.encode(bytes , bytes.length);
                return new String( bytes ).replaceAll("\r\n","");
            }
        }
    }


    public class Os{}



    public class Network{
        public class Manager{
            static Map<String,String> hashMap = new HashMap<>();


            // 수정해야됨
            public static Map<String,String> getNetworkInterfaceConnectedInfo()  {
                try {
                    Thread thread = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            try {
                                Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                                while (enumeration.hasMoreElements()){
                                    NetworkInterface networkInterface = enumeration.nextElement();
                                    if(networkInterface.isUp())
                                        hashMap.put("getStates","Up");
                                    if(!networkInterface.isLoopback())
                                        hashMap.put("getStates",hashMap.get("getStates")+";Loopback");
                                    if(!networkInterface.isVirtual())
                                        hashMap.put("getInterfaceStates",hashMap.get("getStates")+";Virtual");

                                    if(networkInterface.isPointToPoint()){
                                        hashMap.put("getStates",hashMap.get("getStates")+";PointToPoint");
                                        getNetworkInterfaceInfo(networkInterface);
                                        continue;
                                    }

                                    getNetworkInterfaceInfo(networkInterface);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                    thread.join();
                }catch (Exception e){
                    e.printStackTrace();
                }

                return hashMap;
            }

            public static String getAllNetworkInterfaceInfo() throws IOException {
                String result = "";
                Enumeration<NetworkInterface> e= NetworkInterface.getNetworkInterfaces();
                while(e.hasMoreElements()) {
                    result += Manager.getNetworkInterfaceInfo(e.nextElement());
                }
                return result;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public static Map<String,String> getNetworkInterfaceInfo(NetworkInterface networkInterface) throws IOException {

                hashMap.put("getDisplayName" , networkInterface.getDisplayName());
                hashMap.put("getName",networkInterface.getName());
                for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
                    hashMap.put("getAddress", String.valueOf( interfaceAddress.getAddress() ) );
                    hashMap.put("getNetworkPrefixLength", String.valueOf( interfaceAddress.getNetworkPrefixLength() ) );
                    InetAddress inetAddress = interfaceAddress.getBroadcast();
                    if(inetAddress != null){
                        if(inetAddress instanceof Inet4Address){
                            getInetAddressInfo(inetAddress , 4);
                        }
                        if(inetAddress instanceof Inet6Address){
                            getInetAddressInfo(inetAddress , 6);
                        }
                    }
                    hashMap.put("getBroadcast" , String.valueOf(interfaceAddress.getBroadcast()));
                }

                hashMap.put("getMTU", String.valueOf(networkInterface.getMTU()));
                hashMap.put("getIndex", String.valueOf(networkInterface.getIndex()));

                Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses();
                while (enumeration.hasMoreElements()) {
                    InetAddress inetAddress = enumeration.nextElement();
                    //inetAddress 는 set 자체가 없음.
                    if(inetAddress != null){
                        if(inetAddress instanceof Inet4Address){
                            if(inetAddress.isReachable(3000) && !inetAddress.isLoopbackAddress()){

                                hashMap.put("getReachableMsSeconds", String.valueOf(3000));
                                getInetAddressInfo(inetAddress,4);
                            }

                        } else if(inetAddress instanceof Inet6Address){
                            if(inetAddress.isReachable(3000) && !inetAddress.isLoopbackAddress()){
                                hashMap.put("getReachableMsSeconds", String.valueOf(3000));
                                getInetAddressInfo(inetAddress,6);
                            }
                        }
                    }
                }

                String hardwareAddress = "";
                byte[] bytes = networkInterface.getHardwareAddress();
                for (int i=0;i<=bytes.length-1;i++){
                    hardwareAddress+=String.format("%02x:",bytes[i]);
                    if (i == bytes.length -1){
                        hardwareAddress+=String.format("%02x",bytes[i]);
                    }
                }
                hashMap.put("getHardwareAddress" , hardwareAddress);

                return hashMap;
            }

            public static Map<String,String> getInetAddressInfo(InetAddress inetAddress , int version ){
                if(inetAddress.isLinkLocalAddress()){
                    hashMap.put("isLinkLocalAddress","true");
                }
                if(inetAddress.isSiteLocalAddress()){
                    hashMap.put("isSiteLocalAddress","true");
                }
                if(inetAddress.isMulticastAddress()){
                    hashMap.put("isMulticastAddress","true");
                }

                hashMap.put("getInet"+version+"HostName",inetAddress.getHostName());
                //hostname 호스트이름 이 설정 안되어있을 경우 공인 ip 주소로 잡힘. 즉 getHostAddress() 값과 같음.
                hashMap.put("Inet"+version+"HostAddress", inetAddress.getHostAddress());


                String inetAddress_result = "";
                byte[] bytes = inetAddress.getAddress();

                if (version == 6){
                    for(int i=0;i<=bytes.length -1;i+=2){
                        if(i == 0){
                            inetAddress_result += String.format("%02x", bytes[i])+String.format("%02x::", bytes[i+1]);
                        }else if (i == bytes.length-1){
                            inetAddress_result += String.format("%02x", bytes[i])+String.format("%02x", bytes[i+1]);
                        }else{
                            inetAddress_result += String.format("%02x", bytes[i])+String.format("%02x:", bytes[i+1]);
                        }
                    }
                    inetAddress_result = inetAddress_result.replaceAll(":?0000::?","");
                }

                hashMap.put("inet"+version+"Address", inetAddress_result);
                hashMap.put("getMyEnd","\n----------- MY END ----------\n");

                return hashMap;
            }
        }

    }


    public static class BioMetric{

        public static BiometricPrompt fingerPrinting(Context context,String s_msg , String f_msg){ // MyLibrary.BioMetric.fingerPrinting((Context) this,"MNST뱅킹","취소 버튼클릭");


            Executor executor = ContextCompat.getMainExecutor(context);
            Activity activity = (Activity)context;
            BiometricPrompt biometricPrompt = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt = new BiometricPrompt((FragmentActivity) activity, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) { // x (취소) 버튼 클릭 시 발생
                        super.onAuthenticationError(errorCode, errString);
                        MyLibrary.toastLong(context , f_msg);
                        activity.finish();
                        //MyLibrary.BioMetric.fingerPrinting(s_context,s_s_msg,s_f_msg);// 재귀호출 안됨
                    }

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        MyLibrary.toastLong(context ,s_msg);
                        //activity.finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        activity.finish();
                        //MyLibrary.BioMetric.fingerPrinting(s_context,s_s_msg,s_f_msg);// 재귀호출 안됨

                    }
                });
            }

            androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("지문 인증")
                    .setNegativeButtonText("취소")
                    .setDeviceCredentialAllowed(false)
                    .build();
            biometricPrompt.authenticate(promptInfo);


        /*button_fingerprint_cancel = findViewById(R.id.button_fingerprint_cancel);
        button_fingerprint_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.cancelAuthentication();
            }
        });*/
            return biometricPrompt;
        }
    }

    public static class Data{

    }

}
