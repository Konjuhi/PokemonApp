package com.kotlin.pokemonapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kotlin.pokemonapp.Common.Common
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    //create BroadCast handle

    private val showDetail = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == Common.KEY_ENABLE_HOME)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)

            //Replace Fragment
            val detailFragment:PokemonDetail = PokemonDetail.getInstance()
            val position:Int = intent.getIntExtra("position",-1)
            val bundle = Bundle()
            bundle.putInt("position",position)
            detailFragment.arguments = bundle

            val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.list_pokemon_fragment,detailFragment)
            fragmentTransaction.addToBackStack("detail")
            fragmentTransaction.commit()

            //Set pokemon Name for Toolbar
            val pokemon = Common.pokemonList[position]
            toolbar.title = pokemon.name

        }

    }

    private val showEvolution = object :BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action!!.toString() == Common.KEY_NUM_EVOLUTION) {

                //Replace Fragment
                val detailFragment: PokemonDetail = PokemonDetail.getInstance()

                val bundle = Bundle()

                val num = intent.getStringExtra("num")
                bundle.putString("num", num)

                detailFragment.arguments = bundle

                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.remove(detailFragment)//Remove current
                fragmentTransaction.replace(R.id.list_pokemon_fragment, detailFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                //Set pokemon Name for Toolbar
                val pokemon = Common.findPokemonByNum(num)
                toolbar.title = pokemon!!.name

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Pokemon List"
        setSupportActionBar(toolbar)

        //Register Broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(showDetail, IntentFilter(Common.KEY_ENABLE_HOME))

        LocalBroadcastManager.getInstance(this).registerReceiver(showEvolution, IntentFilter(Common.KEY_NUM_EVOLUTION))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item!!.itemId)
       {
           android.R.id.home->{
               toolbar.title="Pokemon List"
               //clear all fragment in stack with name detail

               supportFragmentManager.popBackStack("detail",FragmentManager.POP_BACK_STACK_INCLUSIVE)

               supportActionBar!!.setDisplayHomeAsUpEnabled(false)
               supportActionBar!!.setDisplayShowHomeEnabled(false)
           }
       }
        return true
    }
}
