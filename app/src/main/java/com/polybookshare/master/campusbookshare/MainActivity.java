package com.polybookshare.master.campusbookshare;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import android.text.InputType;
import android.util.Base64;
import com.android.volley.*;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.content.Intent;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
public class MainActivity extends AppCompatActivity {

    protected static final String Cookie = "COOKIE_SAVE";
    private int status = -1;
    private RequestQueue queue;
    private String cookie = "";
    protected EditText username;
    protected EditText password;
    protected Button login;
    protected Button newUser;
    protected Button forgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*boolean logout = getIntent().getBooleanExtra("logout", false);
        if (logout == true) {
            Toast.makeText(getBaseContext(), "You have been successfully logout!", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences prefs = this.getSharedPreferences(Cookie, MODE_PRIVATE);

        cookie = prefs.getString("Cookie", null);

        if (cookie != null) {
            showProfile();
        }*/
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);

        initLayout();
        addListenners();

    }

    public void initLayout() {
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginbtn);
        newUser = (Button) findViewById(R.id.register);
        forgotPwd = (Button) findViewById(R.id.passbtn);
    }

    public void addListenners() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });
        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    login();
                    return true;
                }
                return false;
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    login();
                    return true;
                }
                return false;
            }
        });
    }
    private void login() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        String url = "http://192.168.43.162:8080/auth/login";

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Log.d("username", "username " + String.valueOf(username.getText()));
        final String email =  username.getText().toString();
        final String pwd = password.getText().toString();

        if (email.isEmpty()) {
            alertDialog.setTitle("Empty email address");
            alertDialog.setMessage("Please type your email address");
            alertDialog.show();
        }

        else if (pwd.isEmpty()) {
            alertDialog.setTitle("Empty password");
            alertDialog.setMessage("Please type your password");
            alertDialog.show();
        }

        else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showProfile();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("error log in ", String.valueOf(error.networkResponse.statusCode));
                    NetworkResponse networkResponse = error.networkResponse;

                    if (error.networkResponse == null) {
                        Log.d("throw error", error.getClass().toString());
                        if (error.getClass().equals(NoConnectionError.class)
                                || error.getClass().equals(TimeoutError.class)) {
                            // Show timeout error message
                            Toast.makeText(getBaseContext(),
                                    "Sorry Cannot Connect this time",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    if (networkResponse != null && networkResponse.statusCode == 401) {
                        // HTTP Status Code: 401 Unauthorized
                        status = networkResponse.statusCode;
                        Log.d("error64", String.valueOf(networkResponse.statusCode));


                        alertDialog.setTitle("HTTP 401");
                        alertDialog.setMessage("Wrong email or password");
                        alertDialog.show();
                    }

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //add params <key,value>
                    params.put("email", email);
                    params.put("password", pwd);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    // add headers <key,value>
                    String credentials = "ferrel@calpoly.edu" + ":" + "4481257Phone";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    headers.put("Authorization", auth);
                    return headers;
                }

                @Override
                protected Response parseNetworkResponse(NetworkResponse response) {
                    Map headers = response.headers;
                    if (response != null && response.statusCode == 200) {
                        cookie = (String) headers.get("set-cookie");
                        Log.d("Headers", cookie);
                        SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences(Cookie, MODE_PRIVATE).edit();
                        editor.putString("Cookie", cookie);
                        editor.commit();
                    }
                    return super.parseNetworkResponse(response);

                }
            };
            // Add the request to the RequestQueue.
            Log.d("end of login", stringRequest.toString());
            queue.add(stringRequest);
        }

    }

    private void showProfile() {
        finish();
        Intent i = new Intent(getApplicationContext(), HomeTab.class);
        i.putExtra("cookie", cookie);
        startActivity(i);
    }

    private void forgotPassword() {

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        final EditText text= new EditText(alertDialog.getContext());
        text.setLayoutParams(lp);
        text.setHint("email address");
        text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setTitle("Forgot Password?");
        alertDialog.setMessage("Please enter your email address.");
        alertDialog.setView(text);


        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = text.getText().toString();

                if (!email.isEmpty()) {
                    updatePassword(email);
                    alertDialog.dismiss();
                }

                else {
                    Toast.makeText(MainActivity.this, "Please enter your email!",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    @Override
    protected void onPause() {
        username.setText("");
        password.setText("");

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        cookie = prefs.getString("Cookie", null);

        if (cookie != null) {
            showProfile();
        }
        super.onPause();
    }

    private void updatePassword(final String email) {
        String url = "http://192.168.43.162:8080/auth/forgot";

        Log.d("email", "email " + email);
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Please check your email",
                        Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if(error.networkResponse != null &&
                            error.networkResponse.statusCode == 500)
                        Toast.makeText(MainActivity.this, "Wrong email address",
                                Toast.LENGTH_LONG).show();

                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //add params <key,value>
                params.put("email", email);
                return params;
            }

        };

        queue.add(req);
    }


    public void createNewUser() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        View signup = getLayoutInflater().inflate(R.layout.dialog_signup, null);
        alertDialog.setView(signup);

        final EditText signup_pwd = (EditText) signup.findViewById(R.id.signup_password);
        final EditText signup_pwd2 = (EditText) signup.findViewById(R.id.signup_password2);
        final EditText signup_email = (EditText) signup.findViewById(R.id.signup_email);
        final EditText signup_code = (EditText) signup.findViewById(R.id.signup_code);


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Create Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paswword1 = signup_pwd.getText().toString();
                String password2 = signup_pwd2.getText().toString();

                if (!paswword1.equals(password2))
                    signup_pwd2.setError("Passwords do not match.");
                else if (signup_email.getText().length() == 0 ||
                        signup_code.getText().length() == 0
                        || signup_pwd.getText().length() == 0
                        || signup_pwd2.getText().length() == 0) {
                    if(signup_email.getText().length() == 0)
                        signup_email.setError("Field cannot be left blank.");
                    if (signup_code.getText().length() == 0)
                        signup_code.setError("Field cannot be left blank.");
                    if (signup_pwd.getText().length() == 0)
                        signup_pwd.setError("Field cannot be left blank.");
                    if (signup_pwd2.getText().length() == 0)
                        signup_pwd2.setError("Field cannot be left blank.");
                }

                else
                    alertDialog.dismiss();
            }
        });
    }
}
