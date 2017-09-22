package com.example.theodhor.retrofit2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theodhor.retrofit2.Events.ErrorEvent;
import com.example.theodhor.retrofit2.Events.ServerEvent;
import com.example.theodhor.retrofit2.Utils.Constant;
import com.example.theodhor.retrofit2.model.TicketRequest;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

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
                // getBranches();
                createTicket();
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


    private void getBranches() {

        new LongOperation().execute(Constant.BASE + "branches");

    }

    private void createTicket() {

        new CreateTicketAsync().execute(Constant.BASE + "tickets");

    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser(MainActivity.this);
            User user = new User();
            user.setUsername(Constant.USERNAME);
            user.setPassword(Constant.PASSWORDH);
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

    private JSONObject createTicketParams() {
        JSONObject values = new JSONObject();
        try {
            // TODO: 22/09/17 values to be dynamic 
            values.put(TicketRequest.BRANCH, 3);
            values.put(TicketRequest.SUBJECT, " Ticket for police");
            values.put(TicketRequest.DESCRIPTION, "Harendra Create ticket from android");
            values.put(TicketRequest.STATUS, "new");
            values.put(TicketRequest.PRIORITY, "high");
            values.put(TicketRequest.SOURCE, "email");
            values.put(TicketRequest.REPORTER, "oro_2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    private class CreateTicketAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser(MainActivity.this);
            User user = new User();
            user.setUsername(Constant.USERNAME);
            user.setPassword(Constant.PASSWORDH);
            JSONObject jsonObject = jsonParser.getJSONFromUrl(params[0], JSONParser.POST, createTicketParams(), null, user);

            if (null != jsonObject) {
                return jsonObject.toString();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("RESPONSE TICKET CREATE=" + result);
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
