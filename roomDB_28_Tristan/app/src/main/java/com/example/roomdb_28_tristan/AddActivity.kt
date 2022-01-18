package com.example.roomdb_28_tristan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.roomdb_28_tristan.room.Constant
import com.example.roomdb_28_tristan.room.Movie
import com.example.roomdb_28_tristan.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy {MovieDb(this)}
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupView()
        setupListener()
    }

    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType) {
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
                textUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                textAdd.visibility = View.GONE
                textUpdate.visibility = View.GONE
                getMovie()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                textAdd.visibility = View.GONE
                getMovie()
            }
        }
    }

    fun setupListener(){
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                 db.movieDao().addMovie(
                     Movie(0, et_title.text.toString(),
                     et_description.text.toString())
                 )

                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().updateMovie(
                    Movie(movieId, et_title.text.toString(),
                        et_description.text.toString())
                )

                finish()
            }
        }
    }
    fun getMovie() {
        movieId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovie(movieId) [0]
            et_title.setText(movies.title)
            et_description.setText(movies.description)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}