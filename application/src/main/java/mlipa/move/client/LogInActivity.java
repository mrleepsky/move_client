package mlipa.move.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    private Context context;
    private Intent dashboardIntent;
    private RequestQueue queue;

    private EditText etUsername;
    private EditText etPassword;
    private Button bLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);

        context = getApplicationContext();
        Cookie.preferences = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
        queue = Volley.newRequestQueue(LogInActivity.this);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        bLogIn = (Button) findViewById(R.id.b_log_in);

        bLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.trim().length() == 0) {
                    etUsername.setError("Please fill out this field.");
                } else if (password.trim().length() == 0) {
                    etPassword.setError("Please fill out this field.");
                } else {
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.getBoolean("success")) {
                                    etUsername.setText("");
                                    etUsername.clearFocus();
                                    etPassword.setText("");
                                    etPassword.clearFocus();

                                    dashboardIntent = new Intent(LogInActivity.this, DashboardActivity.class);
                                    Toast toast = Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_LONG);

                                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(dashboardIntent);

                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    queue.add(new LogInRequest(username, password, listener));
                }
            }
        });
    }
}
