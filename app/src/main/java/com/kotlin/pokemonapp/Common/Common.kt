package com.kotlin.pokemonapp.Common

import com.kotlin.pokemonapp.Model.Pokemon
import com.kotlin.pokemonapp.PokemonList

object Common {
    var pokemonList:List<Pokemon> = ArrayList()
    var KEY_ENABLE_HOME = "position"

    fun findPokemonByNum(num:String?):Pokemon?{
        for(pokemon:Pokemon in Common.pokemonList)
            if(pokemon.num.equals(num))
                return pokemon
        return null
    }
}