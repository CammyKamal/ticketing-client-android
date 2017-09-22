package com.example.theodhor.retrofit2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theodhor.retrofit2.Events.ErrorEvent;
import com.example.theodhor.retrofit2.Events.ServerEvent;
import com.example.theodhor.retrofit2.Utils.Constant;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private AzureCommunicator communicator;
    private String username, password;
    private EditText usernameET, passwordET;
    private Button loginButtonPost, loginButtonGet;
    private TextView information, extraInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getBranches();
        //  communicator = new AzureCommunicator(this);

//        usernameET = (EditText)findViewById(R.id.usernameInput);
//        passwordET = (EditText)findViewById(R.id.passwordInput);

//        loginButtonPost = (Button)findViewById(R.id.loginButtonPost);
//        loginButtonPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                USERNAME = usernameET.getText().toString();
//                PASSWORD = passwordET.getText().toString();
//                usePost(USERNAME, PASSWORD);
//            }
//        });

        loginButtonGet = (Button) findViewById(R.id.loginButtonGet);
        loginButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                USERNAME = usernameET.getText().toString();
//                PASSWORD = passwordET.getText().toString();
                // useGet(USERNAME, PASSWORD);
                getBranches();
            }
        });

        information = (TextView) findViewById(R.id.information);
        extraInformation = (TextView) findViewById(R.id.extraInformation);

       /* bus = new Bus(ThreadEnforcer.MAIN);
        bus.register(this);*/

    }

    private void usePost(String username, String password) {
//        communicator.loginPost(USERNAME, PASSWORD);
    }

    private void useGet(String username, String password) {
        communicator.getTopCategories();
    }

    @Subscribe
    public void onServerEvent(ServerEvent serverEvent) {
        Toast.makeText(this, "" + serverEvent.getServerResponse().getMessage(), Toast.LENGTH_SHORT).show();
        if (serverEvent.getServerResponse().getUsername() != null) {
            information.setText("Username: " + serverEvent.getServerResponse().getUsername() + " || Password: " + serverEvent.getServerResponse().getPassword());
        }
        extraInformation.setText("" + serverEvent.getServerResponse().getMessage());
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent) {
        Toast.makeText(this, "" + errorEvent.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private String generateNonce() {
        java.security.SecureRandom random = null;
        try {
            random = java.security.SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(System.currentTimeMillis());
            byte[] nonceBytes = new byte[16];
            random.nextBytes(nonceBytes);
            String nonce = new String(Base64.encodeToString(nonceBytes, Base64.NO_WRAP));
            System.out.print("NONCE= " + nonce);
            return nonce;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void getBranches() {

        new LongOperation().execute(Constant.BASE +"tickets");


    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser(MainActivity.this);
            User user = new User();
            user.setUsername(Constant.USERNAME);
            user.setPassword(Constant.PASSWORD);
            JSONObject jsonObject = jsonParser.getJSONFromUrl(params[0], JSONParser.GET, null, user);

            if (null != jsonObject) {
                return jsonObject.toString();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("RESPONSE BRANCHES=" + result);
            // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
