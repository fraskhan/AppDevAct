package com.example.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://192.168.1.8/CRUDPHP/create.php"; // Assuming your server is running locally

    EditText name, email;
    Button button;
    String TempName, TempEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
                InsertData();
            }
        });

    }

    public void GetData() {
        TempName = name.getText().toString();
        TempEmail = email.getText().toString();
    }

    public void InsertData() {

            class SendPostReqAsyncTask extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    String NameHolder = TempName;
                    String EmailHolder = TempEmail;

                try {
                    URL url = new URL(SERVER_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    HashMap<String, String> postDataParams = new HashMap<>();
                    postDataParams.put("name", NameHolder);
                    postDataParams.put("email", EmailHolder);

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String, String> param : postDataParams.entrySet()) {
                        if (postData.length() != 0)
                            postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    }

                    bufferedWriter.write(postData.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    int responseCode=httpURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        return "Data Inserted Successfully";
                    } else {
                        return "Error: " + responseCode;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error inserting data", Toast.LENGTH_LONG).show();
                }
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }
}
