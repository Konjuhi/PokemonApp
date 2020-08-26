package com.kotlin.pokemonapp.Interface

import android.view.View

interface IItemClickListener {
    fun onClick(view: View, position:Int)
}