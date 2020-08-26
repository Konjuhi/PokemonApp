package com.kotlin.pokemonapp.Retrofit

import com.kotlin.pokemonapp.Model.Pokedex
import io.reactivex.Observable
import retrofit2.http.GET
import java.util.*

interface IPokemonList {
    @get:GET("pokedex.json")
    val listPokemon:Observable<Pokedex>
}