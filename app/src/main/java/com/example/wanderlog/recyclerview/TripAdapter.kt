package com.example.wanderlog.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wanderlog.R
import com.example.wanderlog.database.models.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripAdapter(private var tripList: Set<Trip>) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {
    class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewTrip: ImageView = view.findViewById(R.id.imageViewTrip)
        val textViewTripName: TextView = view.findViewById(R.id.textViewTripName)
        val textViewRatingScore: TextView = view.findViewById(R.id.textViewRatingScore)
        val linearLayoutStarRating: LinearLayout = view.findViewById(R.id.linearlayoutStarRating)
        val textViewOriginalPrice: TextView = view.findViewById(R.id.textViewOriginalPrice)
        val textViewDiscountedPrice: TextView = view.findViewById(R.id.textViewDiscountedPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList.elementAt(position)

        holder.textViewTripName.text = trip.tripName
        holder.textViewRatingScore.text = trip.rating.toString()


        bindStars(holder.linearLayoutStarRating, trip.rating)

//        val bitmap = BitmapFactory.decodeFile(trip.photoUri)
//        holder.imageViewTrip.setImageBitmap(bitmap)

        holder.imageViewTrip.loadImageAsync(trip.photoUri)

        holder.textViewOriginalPrice.apply {
            text = "${trip.price}$"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.textViewDiscountedPrice.text = "${trip.price * 0.8}$"
    }

    override fun getItemCount() = tripList.size

    private fun bindStars(starsLayout: LinearLayout, rating: Float) {
        starsLayout.removeAllViews()

        val fullStars = rating.toInt()
        val halfStars = if (rating % 1 >= 0.5) 1 else 0
        val emptyStars = 5 - (fullStars + halfStars)

        for (i in 1..fullStars) {
            starsLayout.addView(createStarImageView(starsLayout.context, R.drawable.ic_star_filled))
        }

        if (halfStars == 1) {
            starsLayout.addView(createStarImageView(starsLayout.context, R.drawable.ic_star_half))
        }

        for (i in 1..emptyStars) {
            starsLayout.addView(createStarImageView(starsLayout.context, R.drawable.ic_star_outline))
        }
    }

    private fun createStarImageView(context: Context, drawableId: Int): ImageView {
        return ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                val margin = context.resources.getDimensionPixelSize(R.dimen.star_margin)
                setMargins(margin, margin, margin, margin)
            }
            setImageResource(drawableId)

            val size = context.resources.getDimensionPixelSize(R.dimen.star_size)
            layoutParams.width = size
            layoutParams.height = size
        }
    }

    private fun ImageView.loadImageAsync(imagePath: String) {
        val imageView = this
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            withContext(Dispatchers.Main) {
                if (imageView.tag == imagePath) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    fun updateTrips(newTrips: Set<Trip>) {
        val diffCallback = TripDiffCallback(tripList, newTrips)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        tripList = newTrips
        diffResult.dispatchUpdatesTo(this)
    }

    class TripDiffCallback(private val oldList: Set<Trip>, private val newList: Set<Trip>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.elementAt(oldItemPosition).id == newList.elementAt(newItemPosition).id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.elementAt(oldItemPosition) == newList.elementAt(newItemPosition)
        }
    }
}
