package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class BookSearchActivity extends AppCompatActivity {

    //global variables
    EditText editText;
    Button search;
    RequestQueue requestQueue;

    ArrayList<String> books;
    ArrayList<String> authors;
    ArrayList<Integer> ids;
    ArrayList<String> coverURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        setTitle("Search");

        editText = findViewById(R.id.txtSearch);
        search = findViewById(R.id.btnBeginSearch);

        books = new ArrayList<>();
        authors = new ArrayList<>();
        ids = new ArrayList<>();
        coverURL = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
    }

    public void goToMain(View view) {
        finish();
    }

    public void searchCatalog(View view) {

        String newUrl = "https://kamorris.com/lab/cis3515/search.php?term=" + editText.getText().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, newUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    //loop through and add extras to intent object
                    for(int i = 0; i < response.length(); i++){
                        JSONObject issd = response.getJSONObject(i);
                        books.add(issd.getString("title"));
                        authors.add(issd.getString("author"));
                        ids.add(issd.getInt("id"));
                        coverURL.add(issd.getString("cover_url"));
                    }

                        //check if there are any books present
                        if(books.size() == 0){
                            Intent intent = new Intent();
                            setResult(RESULT_FIRST_USER, intent);
                            finish();
                        }else{
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("title",books);
                            intent.putStringArrayListExtra("author",authors);
                            intent.putIntegerArrayListExtra("id",ids);
                            intent.putStringArrayListExtra("pictures",coverURL);
                            setResult(RESULT_OK, intent);
                            finish();
                        }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}