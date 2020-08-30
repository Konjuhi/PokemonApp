package com.kotlin.pokemonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.pokemonapp.Adapter.PokemonEvolutionAdapter
import com.kotlin.pokemonapp.Adapter.PokemonTypeAdapter
import com.kotlin.pokemonapp.Common.Common
import com.kotlin.pokemonapp.Model.Pokemon

/**
 * A simple [Fragment] subclass.
 */
class PokemonDetail : Fragment() {
    internal lateinit var pokemon_img:ImageView

    internal lateinit var pokemon_name:TextView
    internal lateinit var pokemon_height:TextView
    internal lateinit var pokemon_weight:TextView

     lateinit var recycler_type:RecyclerView
     lateinit var recycler_weakness:RecyclerView
     lateinit var recycler_prev_evolution:RecyclerView
     lateinit var recycler_next_evolution:RecyclerView

    companion object{
        internal var instance:PokemonDetail?=null

        fun getInstance():PokemonDetail{
            if(instance==null)
                instance = PokemonDetail()
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemmView = inflater.inflate(R.layout.fragment_pokemon_detail, container, false)

        val pokemon = Common.findPokemonByNum(arguments!!.getString("num"))

       /* val pokemon: Pokemon?
        if(arguments!!.getString("num")==null)
            pokemon = Common.pokemonList[arguments!!.getInt("position")]
        else
            pokemon = Common.findPokemonByNum(arguments!!.getString("num"))*/

        pokemon_img = itemmView.findViewById(R.id.pokemon_image)
        pokemon_name = itemmView.findViewById(R.id.name)
        pokemon_height = itemmView.findViewById(R.id.height)
        pokemon_weight = itemmView.findViewById(R.id.weight)

        recycler_next_evolution = itemmView.findViewById(R.id.recycler_next_evolution)
        recycler_next_evolution.setHasFixedSize(true)
        recycler_next_evolution.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        recycler_prev_evolution = itemmView.findViewById(R.id.recycler_prev_evolution)
        recycler_prev_evolution.setHasFixedSize(true)
        recycler_prev_evolution.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        recycler_type = itemmView.findViewById(R.id.recycler_type)
        recycler_type.setHasFixedSize(true)
        recycler_type.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        recycler_weakness = itemmView.findViewById(R.id.recycler_weakness)
        recycler_weakness.setHasFixedSize(true)
        recycler_weakness.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        setDetailPokemon(pokemon)
        return itemmView
    }

    private fun setDetailPokemon(pokemon: Pokemon?) {

        //Load Image
        Glide.with(activity!!).load(pokemon!!.img).into(pokemon_img)

        pokemon_name.text = pokemon.name
        pokemon_weight.text = "Weight: "+pokemon.weight
        pokemon_height.text = "Height: "+pokemon.height

        val typeAdapter = PokemonTypeAdapter(activity!!,pokemon.type!!)
        recycler_type.adapter= typeAdapter

        val weaknessAdapter = PokemonTypeAdapter(activity!!,pokemon.weaknesses!!)
        recycler_weakness.adapter= weaknessAdapter


        if(pokemon.prev_evolution != null) {
            val prevEvolution = PokemonEvolutionAdapter(activity!!, pokemon.prev_evolution!!)
            recycler_prev_evolution.adapter = prevEvolution
        }
        if(pokemon.next_evolution != null) {
            val nextEvolution = PokemonEvolutionAdapter(activity!!, pokemon.next_evolution!!)
            recycler_next_evolution.adapter = nextEvolution
        }

    }

}
