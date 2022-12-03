package sum.part7_2_cursor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button  callRecord;
    EditText    edtRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callRecord  =   findViewById(R.id.btnCallHistory);
        edtRecord   =   findViewById(R.id.edtCallRecord);

        callRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRecord.setText(getCallHistory());
            }
        });
    }
    public String getCallHistory() {
        String[] callSet    =   new String[]{CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.DURATION};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return  null;
        }
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);
        if ( c == null) return "통화 기록 없음";

        StringBuffer callBuf = new StringBuffer();
        callBuf.append("\n날짜 : 구분 : 전화번호 : 통화시간\n\n");
        c.moveToFirst();
        do {
            long callDate = c.getLong(0);
            SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-mm-dd");
            String date_str = datePattern.format(new Date(callDate));
            callBuf.append(date_str + ":");
            if(c.getInt(1) == CallLog.Calls.INCOMING_TYPE) callBuf.append("착신: ");
            else callBuf.append("발신: ");
            callBuf.append(c.getString(2) + ":");
            callBuf.append(c.getString(3) + "초\n");
        } while(c.moveToNext());

        c.close();
        return callBuf.toString();
    }
}