package softsolution.sks.com.app2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import softsolution.sks.com.aidlfile.IAdditionService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = " App2 MainActivity";
    IAdditionService service;
    AdditionServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initService();

        // Setup the UI
        Button buttonCalc = (Button) findViewById(R.id.buttonCalc);

        buttonCalc.setOnClickListener(new View.OnClickListener() {
            TextView result = (TextView) findViewById(R.id.result);
            EditText value1 = (EditText) findViewById(R.id.value1);
            EditText value2 = (EditText) findViewById(R.id.value2);

            public void onClick(View v) {
                int v1, v2, res = -1;
                v1 = Integer.parseInt(value1.getText().toString());
                v2 = Integer.parseInt(value2.getText().toString());

                try {
                    res = service.add(v1, v2);
                } catch (RemoteException e) {
                    Log.d(TAG, "onClick failed with: " + e);
                    e.printStackTrace();
                }
                result.setText(new Integer(res).toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();

    }

    class AdditionServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IAdditionService.Stub.asInterface((IBinder) boundService);
            Log.d(TAG, "onServiceConnected() connected");
            Toast.makeText(MainActivity.this, "Service connected",
                    Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected() disconnected");
            Toast.makeText(MainActivity.this, "Service connected",
                    Toast.LENGTH_LONG).show();
        }
    }

    /** Binds this activity to the service. */
    private void initService() {
        connection = new AdditionServiceConnection();
        Intent i = new Intent();
        i.setAction("addition.service");
        i.setPackage("softsolution.sks.com.aidlexample");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound with " + ret);
    }

    /** Unbinds this activity from the service. */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService() unbound.");
    }
}
