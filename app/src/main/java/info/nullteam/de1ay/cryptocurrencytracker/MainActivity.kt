package info.nullteam.de1ay.cryptocurrencytracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.net.URL

import kotlin.io.readText
import android.widget.Toast



class MainActivity : AppCompatActivity() {

    private val cryptoIDs: Array<String> = arrayOf<String>("BTC", "ETH", "XRP", "BCH", "ADA", "LTC", "NEO")
    private var cryptoCurrencies:JsonArray<JsonObject>? = JsonArray<JsonObject>()
    private var isInitialized: Boolean = false
    private var currenciesList: LinearLayout? = null
    private var currenciesViews: Array<View> = arrayOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        updateCurrencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currenciesList = findViewById<LinearLayout>(R.id.currencies_list)
    }

    private fun getCurrencies(): JsonArray<JsonObject>? {
        val responseData = URL("https://api.coinmarketcap.com/v1/ticker/").readText()
        val parser = Parser()
        val stringBuilder = StringBuilder(responseData)
        return parser.parse(stringBuilder) as JsonArray<JsonObject>
    }

    private fun updateCurrencies() {
        launch {
            while(true) {
                runOnUiThread() {
                    val toast = Toast.makeText(applicationContext,
                            "Обновление курсов...", Toast.LENGTH_SHORT)
                    toast.show()
                }
                cryptoCurrencies = getCurrencies()
                runOnUiThread() {
                    if (isInitialized) {
                        updateTextViews()
                    } else {
                        initializeCurrencies()
                    }
                }
                delay(60000)
            }
        }
    }

    private fun initializeCurrencies(){
        for (crypt in cryptoCurrencies!!) {
            if (crypt["symbol"] !in cryptoIDs) {
                continue
            }
            val newCurrencyView: View = layoutInflater.inflate(R.layout.currency_layout, null)
            val logo: ImageView = newCurrencyView.findViewById<ImageView>(R.id.logo)
            val text: TextView = newCurrencyView.findViewById<TextView>(R.id.text)
            val fullText: TextView = newCurrencyView.findViewById<TextView>(R.id.text_full)
            when (crypt["symbol"]) {
                "BTC" -> logo.setImageResource(R.drawable.btc)
                "ETH" -> logo.setImageResource(R.drawable.eth)
                "XRP" -> logo.setImageResource(R.drawable.xrp)
                "BCH" -> logo.setImageResource(R.drawable.bch)
                "ADA" -> logo.setImageResource(R.drawable.ada)
                "LTC" -> logo.setImageResource(R.drawable.ltc)
                "NEO" -> logo.setImageResource(R.drawable.neo)
                else -> {}
            }
            text.text = crypt["symbol"].toString()
            fullText.text = crypt["name"].toString()
            updateValues(crypt, newCurrencyView)
            currenciesList!!.addView(newCurrencyView)
            currenciesViews = currenciesViews.plus(newCurrencyView)
        }
        isInitialized = true
    }

    private fun updateTextViews() {
        for (crypt in cryptoCurrencies!!) {
            if (crypt["symbol"] !in cryptoIDs) {
                continue
            }
            val currencyView = currenciesViews.filterIndexed{ _, currencyView ->
                currencyView.findViewById<TextView>(R.id.text).text == crypt["symbol"] }[0]
            updateValues(crypt, currencyView)
        }
    }

    private fun updateValues(crypt: JsonObject, currencyView: View) {
        val currency: TextView = currencyView.findViewById<TextView>(R.id.currency)
        val _1h: TextView = currencyView.findViewById<TextView>(R.id._1h)
        val _24h: TextView = currencyView.findViewById<TextView>(R.id._24h)
        val _7d: TextView = currencyView.findViewById<TextView>(R.id._7d)
        val _1hPercentMark: TextView = currencyView.findViewById<TextView>(R.id._1h_percent_mark)
        val _24hPercentMark: TextView = currencyView.findViewById<TextView>(R.id._24h_percent_mark)
        val _7dPercentMark: TextView = currencyView.findViewById<TextView>(R.id._7d_percent_mark)
        currency.text = crypt["price_usd"].toString()
        _1h.text = crypt["percent_change_1h"].toString()
        _24h.text = crypt["percent_change_24h"].toString()
        _7d.text = crypt["percent_change_7d"].toString()
        if (_1h.text.contains('-')) {
            _1h.setTextColor(resources.getColor(R.color.negativeChange, null))
            _1hPercentMark.setTextColor(resources.getColor(R.color.negativeChange, null))
        } else {
            _1h.setTextColor(resources.getColor(R.color.positiveChange, null))
            _1hPercentMark.setTextColor(resources.getColor(R.color.positiveChange, null))
        }
        if (_24h.text.contains('-')) {
            _24h.setTextColor(resources.getColor(R.color.negativeChange, null))
            _24hPercentMark.setTextColor(resources.getColor(R.color.negativeChange, null))
        } else {
            _24h.setTextColor(resources.getColor(R.color.positiveChange, null))
            _24hPercentMark.setTextColor(resources.getColor(R.color.positiveChange, null))
        }
        if (_7d.text.contains('-')) {
            _7d.setTextColor(resources.getColor(R.color.negativeChange, null))
            _7dPercentMark.setTextColor(resources.getColor(R.color.negativeChange, null))
        } else {
            _7d.setTextColor(resources.getColor(R.color.positiveChange, null))
            _7dPercentMark.setTextColor(resources.getColor(R.color.positiveChange, null))
        }
    }
}
