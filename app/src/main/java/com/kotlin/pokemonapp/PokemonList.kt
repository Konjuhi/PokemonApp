package com.kotlin.pokemonapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.pokemonapp.Adapter.PokemonListAdapter
import com.kotlin.pokemonapp.Common.Common
import com.kotlin.pokemonapp.Common.ItemOffsetDecoration
import com.kotlin.pokemonapp.Model.Pokemon
import com.kotlin.pokemonapp.Retrofit.IPokemonList
import com.kotlin.pokemonapp.Retrofit.RetrofitClient
import com.mancj.materialsearchbar.MaterialSearchBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.pokemon_list.*
import kotlinx.android.synthetic.main.pokemon_list_item.*
import retrofit2.Retrofit

/**
 * A simple [Fragment] subclass.
 */
class PokemonList : Fragment() {
    internal var compositeDisposable = CompositeDisposable()
    internal var iPokemonList: IPokemonList

    internal  lateinit var recyclerView: RecyclerView

    internal lateinit var adapter:PokemonListAdapter
    internal lateinit var search_adapter:PokemonListAdapter
    internal var last_suggest:MutableList<String> = ArrayList()

    internal lateinit var search_bar: MaterialSearchBar


    init {
        val retrofit: Retrofit = RetrofitClient.instance
        iPokemonList = retrofit.create(IPokemonList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView: View = inflater.inflate(R.layout.pokemon_list, container, false)

        recyclerView = itemView.findViewById(R.id.pokemon_recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        val itemDecoration = ItemOffsetDecoration(activity!!, R.dimen.spacing)
        recyclerView.addItemDecoration(itemDecoration)

        //Setup search bar
        search_bar = itemView.findViewById(R.id.search_bar) as MaterialSearchBar
        search_bar.setHint("Enter Pokemon Name")
        search_bar.setCardViewElevation(10)
        search_bar.addTextChangeListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val suggest = ArrayList<String>()
                for(search in last_suggest)
                    if(search.toLowerCase().contains(search_bar.text.toLowerCase()))
                        suggest.add(search)
                search_bar.lastSuggestions = suggest
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        search_bar.setOnSearchActionListener(object :MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                if(!enabled)
                    pokemon_recyclerView.adapter = adapter
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())

            }

            override fun onButtonClicked(buttonCode: Int) {
                TODO("Not yet implemented")
            }
        })



        fetchData()

        return itemView
    }

    private fun startSearch(text: String) {
        if(Common.pokemonList.size >0){
            var result= ArrayList<Pokemon>()
            for(pokemon in Common.pokemonList)
                if(pokemon.name!!.toLowerCase().contains(text.toLowerCase()))
                    result.add(pokemon)
            search_adapter = PokemonListAdapter(activity!!,result)
            pokemon_recyclerView.adapter = search_adapter

            last_suggest.clear()
            for(pokemon in Common.pokemonList)
                last_suggest.add(pokemon.name!!)
            search_bar.visibility = View.VISIBLE
            search_bar.lastSuggestions = last_suggest
        }

    }

    private fun fetchData() {
        compositeDisposable.add(iPokemonList.listPokemon
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{pokemonDex->
                Common.pokemonList = pokemonDex.pokemon!!

                adapter = PokemonListAdapter(activity!!,Common.pokemonList)
                recyclerView.adapter = adapter



            }

        )


    }
}
