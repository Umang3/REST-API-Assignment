package com.example.restapiassignment

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.restapiassignment.database.ArtistDataBase
import com.example.restapiassignment.database.ArtistEntity
import com.squareup.picasso.Picasso


class ArtistNameAdaptor(var context: Context , val arrayList: ArrayList<ArtistDetails>) : RecyclerView.Adapter<ArtistNameAdaptor.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_adaptor,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aList = arrayList[position]

        val name = aList.artistname
        holder.textArtistName.text = "Artist : $name"

//        val collection = aList.collectionname
//        holder.textCollectionName.text = "Collection : $collection"

//        val track = aList.artisttrack
//        holder.textTrack.text = "Track : $track"

        Picasso.get().load(aList.collectionurl).error(R.drawable.error_foreground).into(holder.imageArtist)



        val genere = aList.primaryGenreName
        holder.textPrimaryGenRename.text = "Genere : $genere"

        val collectionPrice  = aList.collectionPrice
        holder.textCollectionPrice.text = "Collection Price : $collectionPrice"

        val releaseDate = aList.releaseDate
        holder.textReleaseDate.text = "Release Date : $releaseDate"
        holder.cardView.setOnClickListener{
            Toast.makeText(context,"You have to Pay Rs. $collectionPrice",Toast.LENGTH_SHORT).show()
        }

        val artistEntity = ArtistEntity(
              aList.collectionId.toInt(),
                aList.artistname,
                aList.primaryGenreName,
                aList.collectionPrice,
                aList.releaseDate,
                aList.collectionurl
        )


        if (!DBAsyncTask(
                context,
                artistEntity,
                1
            ).execute().get()) {
            //Toast.makeText(context,"Data of this artist has been stored in DB you can access W/O Internet",Toast.LENGTH_SHORT).show()
            val async =
                DBAsyncTask(
                    context,
                    artistEntity,
                    2
                ).execute()
            val result = async.get()
            if (result) {
           //     Toast.makeText(context,"Data Offline",Toast.LENGTH_SHORT).show()

                val name = aList.artistname
                holder.textArtistName.text = "Artist : $name"

//        val collection = aList.collectionname
//        holder.textCollectionName.text = "Collection : $collection"

//        val track = aList.artisttrack
//        holder.textTrack.text = "Track : $track"

                Picasso.get().load(aList.collectionurl).error(R.drawable.error_foreground).into(holder.imageArtist)


                val genere = aList.primaryGenreName
                holder.textPrimaryGenRename.text = "Genere : $genere"

                val collectionPrice  = aList.collectionPrice
                holder.textCollectionPrice.text = "Collection Price : $collectionPrice"

                val releaseDate = aList.releaseDate
                holder.textReleaseDate.text = "Release Date : $releaseDate"
                holder.cardView.setOnClickListener{
                    Toast.makeText(context,"You have to Pay Rs. $collectionPrice",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            val async = DBAsyncTask(
                context,
                artistEntity,
                3
            ).execute()
            val result = async.get()

            if (result) {
                Picasso.get().load(artistEntity.collectionurl).error(R.drawable.error_foreground).into(holder.imageArtist)
                holder.textArtistName.text = artistEntity.artistname
                holder.textPrimaryGenRename.text = artistEntity.primaryGenreName
                holder.textCollectionPrice.text = artistEntity.collectionPrice
                holder.textReleaseDate.text = artistEntity.releaseDate


           //     holder.imgIsFav.setImageResource(R.drawable.ic_favourites_restaurants)
            }
        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imageArtist : ImageView = view.findViewById(R.id.imageArtist)
        var textArtistName : TextView = view.findViewById(R.id.textArtistName)
      //  val textTrack : TextView = view.findViewById(R.id.textTrack)
      //  val textCollectionName : TextView = view.findViewById(R.id.textCollectionName)
        var textPrimaryGenRename : TextView = view.findViewById(R.id.textPrimaryGenRename)
        var textCollectionPrice : TextView = view.findViewById(R.id.textCollectionPrice)
        var textReleaseDate : TextView = view.findViewById(R.id.textReleaseDate)
        var cardView : CardView = view.findViewById(R.id.cardView)

    }

class DBAsyncTask(context: Context , val artistEntity: ArtistEntity , val mode : Int):AsyncTask<Void,Void,Boolean>(){

    val db = Room.databaseBuilder(context,ArtistDataBase::class.java, "artist-db").build()

    override fun doInBackground(vararg paramsss: Void?): Boolean {

        when (mode) {

            1 -> {
                val res: ArtistEntity? =
                       db.artistDao().getArtistById(artistEntity.collectionId.toString())
                db.close()
                return res != null
            }

            2 -> {
                db.artistDao().insertArtist(artistEntity)
                db.close()
                return true
            }
            3 -> {
                db.artistDao().deleteArtist(artistEntity)
                db.close()
                return true
            }

        }

        return true

    }

}


}
