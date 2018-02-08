package info.nullteam.de1ay.cryptocurrencytracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.currency_layout.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.net.URL

import kotlin.io.readText

class MainActivity : AppCompatActivity() {

    private val cryptoIDs: Array<String> = arrayOf<String>("BTC", "ETH", "XRP", "BCH", "ADA", "LTC", "NEO")
    private var cryptoCurrencies:JsonArray<JsonObject>? = JsonArray<JsonObject>()
    private var isInitialized: Boolean = false
    private var currenciesList: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        updateCurrencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currenciesList = findViewById<LinearLayout>(R.id.currencies_list)
    }

    private fun updateTextViews() {
        for (crypt in cryptoCurrencies!!) {
            if (crypt["symbol"] !in cryptoIDs) {
                continue
            }
            var newCurrencyView: View = layoutInflater.inflate(R.layout.currency_layout, null)
            var logo: ImageView = newCurrencyView.findViewById<ImageView>(R.id.logo)
            var text: TextView = newCurrencyView.findViewById<TextView>(R.id.text)
            var fullText: TextView = newCurrencyView.findViewById<TextView>(R.id.text_full)
            var currency: TextView = newCurrencyView.findViewById<TextView>(R.id.currency)
            var _1h: TextView = newCurrencyView.findViewById<TextView>(R.id._1h)
            var _24h: TextView = newCurrencyView.findViewById<TextView>(R.id._24h)
            var _7d: TextView = newCurrencyView.findViewById<TextView>(R.id._7d)
            var _1hPercentMark: TextView = newCurrencyView.findViewById<TextView>(R.id._1h_percent_mark)
            var _24hPercentMark: TextView = newCurrencyView.findViewById<TextView>(R.id._24h_percent_mark)
            var _7dPercentMark: TextView = newCurrencyView.findViewById<TextView>(R.id._7d_percent_mark)
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
            currency.text = crypt["price_usd"].toString()
            _1h.text = crypt["percent_change_1h"].toString()
            _24h.text = crypt["percent_change_24h"].toString()
            _7d.text = crypt["percent_change_7d"].toString()
            if (_1h.text.contains('-')) {
                _1h.setTextColor(resources.getColor(R.color.negativeChange))
                _1hPercentMark.setTextColor(resources.getColor(R.color.negativeChange))
            } else {
                _1h.setTextColor(resources.getColor(R.color.positiveChange))
                _1hPercentMark.setTextColor(resources.getColor(R.color.positiveChange))
            }
            if (_24h.text.contains('-')) {
                _24h.setTextColor(resources.getColor(R.color.negativeChange))
                _24hPercentMark.setTextColor(resources.getColor(R.color.negativeChange))
            } else {
                _24h.setTextColor(resources.getColor(R.color.positiveChange))
                _24hPercentMark.setTextColor(resources.getColor(R.color.positiveChange))
            }
            if (_7d.text.contains('-')) {
                _7d.setTextColor(resources.getColor(R.color.negativeChange))
                _7dPercentMark.setTextColor(resources.getColor(R.color.negativeChange))
            } else {
                _7d.setTextColor(resources.getColor(R.color.positiveChange))
                _7dPercentMark.setTextColor(resources.getColor(R.color.positiveChange))
            }
            currenciesList!!.addView(newCurrencyView)
        }
    }

    private fun getCurrencies(): JsonArray<JsonObject>? {
        val responseData = URL("https://api.coinmarketcap.com/v1/ticker/").readText()
        val parser: Parser = Parser()
        val stringBuilder = StringBuilder(responseData)
        return parser.parse(stringBuilder) as JsonArray<JsonObject>
    }

    //TODO: real-time update
    private fun updateCurrencies() {
        launch {
            while(true) {
                cryptoCurrencies = getCurrencies()
                runOnUiThread() {
                    updateTextViews()
                }
                delay(10000)
                break
            }
        }
    }
}
