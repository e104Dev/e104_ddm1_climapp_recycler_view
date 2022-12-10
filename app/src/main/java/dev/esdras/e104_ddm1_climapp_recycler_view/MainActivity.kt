package dev.esdras.e104_ddm1_climapp_recycler_view

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.esdras.e104_ddm1_climapp_recycler_view.adapter.WeatherAdapter
import dev.esdras.e104_ddm1_climapp_recycler_view.model.Weather


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //<editor-fold desc="VIEWS" defaultstate="collapsed">
        val tvTemperatura = findViewById<TextView>(R.id.textViewTemperatura)
        val tvCidade = findViewById<TextView>(R.id.textViewCidade)
        val tvTempMaxima = findViewById<TextView>(R.id.textViewMaxima)
        val tvTempMinima = findViewById<TextView>(R.id.textViewMinima)
        val tvTempoCelula = findViewById<TextView>(R.id.textViewTempoCelula)
        val tvNascerDoSol = findViewById<TextView>(R.id.textViewNascerDoSol)
        val tvPorDoSol = findViewById<TextView>(R.id.textViewPorDoSol)
        val tvHora = findViewById<TextView>(R.id.textViewHora)
        val tvData = findViewById<TextView>(R.id.textViewData)

        //</editor-fold>

        //<editor-fold desc="Animacao Gradiente" defaultstate="collapsed">
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val bg = findViewById<View>(R.id.root_layout)

        val animDrawable = bg.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(100)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        //</editor-fold>

        //TODO: Criar Adapter para RecyclerViews
        var layoutManager = LinearLayoutManager(this)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        var mapper = jacksonObjectMapper()
        var weather: Weather?

        val url = "https://api.hgbrasil.com/weather"
        val queue = Volley.newRequestQueue(this)

        //TODO: Criar Spinner para exibicao enquanto a requisicao estiver em execução
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                Log.d("RESULT: ", response)
                val nodeResult = mapper.readTree(response)
                weather = mapper.readValue(nodeResult.get("results").toString(),
                    Weather::class.java)

                //TODO: Refatorar para pelo de ajuste de UI
                tvTemperatura.text = weather?.temp.toString()
                tvCidade.text = weather?.city
                tvTempMaxima.text = weather?.forecast?.get(0)?.max.toString()
                tvTempMinima.text = weather?.forecast?.get(0)?.min.toString()
                tvNascerDoSol.text = weather?.sunrise
                tvPorDoSol.text = weather?.sunrise
                tvTempoCelula.text = weather?.description
                tvHora.text = weather?.time
                tvData.text = weather?.date

                Log.i("JACKSON:", weather.toString())

                val adapter = WeatherAdapter(weather?.forecast!!)

                //TODO: Criar Adapter

            },
            { error -> error.localizedMessage?.let { Log.d("ERROR: ", it) } })

        queue.add(stringRequest)
    }
}

