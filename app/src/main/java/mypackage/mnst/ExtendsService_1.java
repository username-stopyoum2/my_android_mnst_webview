package mypackage.mnst;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/*
    service list;
    service check ExtendsService_1;
    dumpsys ExtendsService_1;
    logcat *:* | grep -Ei 'ExtendsService';


*/
public class ExtendsService_1 extends Service {
    public ExtendsService_1() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!MyLibrary.f_check_valid(intent)) return Service.START_STICKY;
        //루팅탐지 코드 작성 ㄱ


        return super.onStartCommand(intent, flags, startId);

    }
}