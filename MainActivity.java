import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;

    private TextView signalStrengthTextView;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signalStrengthTextView = findViewById(R.id.signalStrengthTextView);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                updateSignalStrength(signalStrength);
            }
        };

        requestPermissionsIfNeeded();
    }

    private void requestPermissionsIfNeeded() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION
            );
        } else {
            startSignalStrengthListener();
        }
    }

    private void startSignalStrengthListener() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private void updateSignalStrength(SignalStrength signalStrength) {
        int dbm = signalStrength.getDbm();
        signalStrengthTextView.setText("Signal Strength: " + dbm + " dBm");
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSignalStrengthListener();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }
}
