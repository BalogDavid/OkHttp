package com.example.retrofit;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // URL-ul către API-ul care returnează JSON-ul
        String apiUrl = "https://jsonplaceholder.typicode.com/photos/1";


        fetchImageUrl(apiUrl);
    }

    private void fetchImageUrl(String apiUrl) {
        OkHttpClient client = new OkHttpClient();

        // Creează cererea HTTP
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        // Execută cererea
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Eroare la încărcarea URL-ului", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    try {
                        // Parsează JSON-ul
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String imageUrl = jsonObject.getString("image_url");

                        // Încarcă imaginea în ImageView
                        runOnUiThread(() ->
                                Picasso.get().load(imageUrl).into(imageView)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Eroare la parsarea JSON-ului", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Cerere eșuată", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}