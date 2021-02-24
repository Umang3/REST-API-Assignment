package com.example.restapiassignment

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.restapiassignment.database.ArtistDataBase
import com.example.restapiassignment.util.ConnectionManager
import org.json.JSONObject

class MainActivity : AppCompatActivity() {lateinit var editText: EditText
    var editText1 = ""
    var uRL = ""
    var startUrl = ""
    var endUrl = ""

    lateinit var button: Button
    lateinit var bottomButton: Button
    var artistList = arrayListOf<ArtistDetails>()
    lateinit var artistNameAdaptor: ArtistNameAdaptor
    lateinit var recyclerView: RecyclerView

    var artistList1 = arrayListOf<ArtistDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        bottomButton = findViewById(R.id.bottomButton)
        recyclerView = findViewById(R.id.recyclerView)


        button.setOnClickListener {
            editText1 = editText.text.toString()

            if (editText1.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please Enter Artist Name!!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                fetchData()
            }
        }

        bottomButton.setOnClickListener {
            button.visibility = View.VISIBLE
            editText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            bottomButton.visibility = View.GONE
        }
    }

    private fun fetchData() {
        recyclerView.visibility = View.VISIBLE
        button.visibility = View.GONE
        if (ConnectionManager().isNetworkAvailable(this@MainActivity as Context)) {
            editText.text.clear()
            startUrl = "https://itunes.apple.com/"
            endUrl = "search?term=$editText1"
            Global.artistName = endUrl
            uRL = "$startUrl$endUrl"

            getData()

        }

        else {

            if (artistList.isEmpty()) {
                button.visibility = View.VISIBLE

                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            } else {
                bottomButton.visibility = View.VISIBLE

                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("No Internet")
                dialog.setMessage("Internet Connection is NOT  Found")
                dialog.setPositiveButton("Check Previous Artist Data !!")
                { text, listener ->

                    artistNameAdaptor =
                        ArtistNameAdaptor(
                            this,
                            artistList1
                        )
                    recyclerView.adapter = artistNameAdaptor
                    val mLayoutManager = GridLayoutManager(this, 2)
                    recyclerView.layoutManager = mLayoutManager


                }

                dialog.setNegativeButton("Exit")
                { text, listener ->

                    ActivityCompat.finishAffinity(this@MainActivity)
                    super.onBackPressed()
                }
            }
        }
    }



    private fun getData() {

        try{
            val jsonObject = JsonObjectRequest(Request.Method.GET,uRL,null,
                {
                        response ->

                    val jsonObj = JSONObject(response.toString())
                    var resultCount = jsonObj.getString("resultCount").toInt()
                    if(resultCount < 0){
                        Toast.makeText(this,"No Artist Found!!",Toast.LENGTH_SHORT).show()
                    }
                    if(resultCount == 0){
                        Toast.makeText(this,"No Data Available!!",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        editText.visibility = View.GONE
                        button.visibility = View.GONE
                        bottomButton.visibility = View.VISIBLE
                        val resultArray = jsonObj.getJSONArray("results")
                        for(i in 0 until resultArray.length()){
                            val resultArrayObject = resultArray.getJSONObject(i)
                            val artistdetails = ArtistDetails(
                                resultArrayObject.getString("collectionId"),
                                resultArrayObject.getString("artistName"),
                                resultArrayObject.getString("primaryGenreName"),
                                resultArrayObject.getString("collectionPrice"),
                                resultArrayObject.getString("releaseDate"),
                                resultArrayObject.getString("artworkUrl100")
                            )
                            artistList.add(artistdetails)
                            artistNameAdaptor = ArtistNameAdaptor(this@MainActivity, artistList)

                            recyclerView.adapter = artistNameAdaptor
                            recyclerView.layoutManager = GridLayoutManager(this,2)
                        }
                    }
                } ,  {
                        error ->
                    Toast.makeText(this,error.toString(),Toast.LENGTH_SHORT).show()

                })
            Volley.newRequestQueue(this).add(jsonObject)

        }

        catch(e : Exception){
            e.printStackTrace()
        }
    }

    class GetAllArtist(context : Context) : AsyncTask<Void, Void, List<ArtistDetails>>() {

        val db = Room.databaseBuilder(context,ArtistDataBase::class.java,"res-db").build()
        override fun doInBackground(vararg p0: Void?): List<ArtistDetails> {
            val list = db.artistDao().getAllArtists()
            val listOfIds = arrayListOf<ArtistDetails>()
            for (i in list) {
                val artistt = ArtistDetails(
                    i.collectionId.toString(),
                    i.artistname,
                    i.primaryGenreName,
                    i.collectionPrice,
                    i.releaseDate,
                    i.collectionurl
                )
                listOfIds.add(artistt)
            }
            return listOfIds
        }
    }

    }
