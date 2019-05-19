package pe.edu.cibertec;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import pe.edu.cibertec.clases.Movie;
import pe.edu.cibertec.retrofit.MovieInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText titleMovie;
    private Button searchMovie;
    private TextView nameMovie, yearMovie, sinopsisMovie;
    private String name, apiKey;
    private Retrofit retrofit;
    private MovieInterface service;
    private Call<Movie> method;
    private Movie movie;
    private ImageView imageMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleMovie = findViewById(R.id.titleMovie);
        searchMovie = findViewById(R.id.searchMovie);
        nameMovie = findViewById(R.id.nameMovie);
        yearMovie = findViewById(R.id.yearMovie);
        sinopsisMovie = findViewById(R.id.sinopsisMovie);
        imageMovie = findViewById(R.id.imageMovie);

        searchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = titleMovie.getText().toString();
                apiKey = "6fc43ba7";
                if(name.equals("")){
                    Toast.makeText(MainActivity.this, "¡Por favor inserte un título!", Toast.LENGTH_LONG).show();
                }else{
                    retrofit = new Retrofit.Builder().baseUrl("https://www.omdbapi.com/").addConverterFactory(GsonConverterFactory.create()).build();

                    service = retrofit.create(MovieInterface.class);

                    method = service.searchMovie(apiKey, name);

                    method.enqueue(new Callback<Movie>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if(response.isSuccessful()){
                                movie = response.body();
                                nameMovie.setText("Nombre: "+movie.getTitle());
                                yearMovie.setText("Año: "+movie.getYear());
                                sinopsisMovie.setText("Sinopsis: "+movie.getPlot());
                                Glide.with(MainActivity.this).load(movie.getPoster()).into(imageMovie);
                            }else{
                                Toast.makeText(MainActivity.this, "¡Se presento un error!", Toast.LENGTH_LONG).show();
                                Log.e("ERROR SIN ACCESO A LA API", "¡Sin acceso a la api!");
                            }
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "¡Se presento un error!", Toast.LENGTH_LONG).show();
                            Log.e("ERROR BUSCAR PELICULA", "Mensaje: "+t.getMessage()+", Causa: "+t.getCause());
                        }
                    });

                }
            }
        });

    }
}
