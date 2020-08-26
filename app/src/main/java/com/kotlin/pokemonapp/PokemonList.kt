package com.kotlin.pokemonapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.pokemonapp.Adapter.PokemonListAdapter
import com.kotlin.pokemonapp.Common.Common
import com.kotlin.pokemonapp.Common.ItemOffsetDecoration
import com.kotlin.pokemonapp.Retrofit.IPokemonList
import com.kotlin.pokemonapp.Retrofit.RetrofitClient
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

        fetchData()

        return itemView
    }

    private fun fetchData() {
        compositeDisposable.add(iPokemonList.listPokemon
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{pokemonDex->
                Common.pokemonList = pokemonDex.pokemon!!

                val adapter = PokemonListAdapter(activity!!,Common.pokemonList)
                recyclerView.adapter = adapter

            }

        )


    }
}
