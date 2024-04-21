package com.ubaya.hobbyapp_160421093.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.squareup.picasso.Picasso
import com.ubaya.hobbyapp_160421093.model.Model
import com.squareup.picasso.Callback
import com.ubaya.hobbyapp_160421093.databinding.CardHobbyItemBinding

class HobbyAdapter(val hobbyList:ArrayList<Model>)
    : RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder>()
{
    class HobbyViewHolder(var binding: CardHobbyItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
        val binding = CardHobbyItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return HobbyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hobbyList.size
    }

    override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
        val builder = Picasso.Builder(holder.itemView.context)
        builder.listener { picasso, uri, exception -> exception.printStackTrace() }
        Picasso.get().load(hobbyList[position].images).into(holder.binding.imgCover)

        holder.binding.txtJudul.text = hobbyList[position].title
        holder.binding.txtPembuat.text = "@"+hobbyList[position].direction
        holder.binding.txtDeskripsi.text = hobbyList[position].shortDescription

        holder.binding.btnRead.setOnClickListener {
            val action = HomeFragmentDirections.actionHobbyDetailFragment(
                hobbyList[position].direction ?: "",
                hobbyList[position].title ?: "",
                hobbyList[position].fullDescription ?: "",
                hobbyList[position].images ?: "")
            holder.itemView.findNavController().navigate(action)
        }
        val picasso = Picasso.Builder(holder.itemView.context)
        picasso.listener { picasso, uri, exception ->
            exception.printStackTrace()
        }
        picasso.build().load(hobbyList[position].images)
            .into(holder.binding.imgCover, object: Callback {
                override fun onSuccess() {
                    holder.binding.imgCover.visibility = View.VISIBLE
                }
                override fun onError(e: Exception?) {
                    Log.e("picasso_error", e.toString())
                }
            })
    }
    fun updateHobby(newHobbyList: ArrayList<Model>) {
        hobbyList.clear()
        hobbyList.addAll(newHobbyList)
        notifyDataSetChanged()
    }
}