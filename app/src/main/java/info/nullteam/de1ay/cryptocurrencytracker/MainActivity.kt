package info.nullteam.de1ay.cryptocurrencytracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.net.URL

import kotlin.io.readText

class MainActivity : AppCompatActivity() {

    private var cryptoCurrencies:JsonArray<JsonObject>? = JsonArray<JsonObject>()
    private var bitcoinCurrency: TextView? = null
    private var bitcoin1h: TextView? = null
    private var bitcoin24h: TextView? = null
    private var bitcoin7d: TextView? = null
    private var bitcoin1hMark: TextView? = null
    private var bitcoin24hMark: TextView? = null
    private var bitcoin7dMark: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        updateCurrencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bitcoinCurrency = findViewById<TextView>(R.id.bitcoin_currency)
        bitcoin1h = findViewById<TextView>(R.id.bitcoin_1h)
        bitcoin24h = findViewById<TextView>(R.id.bitcoin_24h)
        bitcoin7d = findViewById<TextView>(R.id.bitcoin_7d)
        bitcoin1hMark = findViewById<TextView>(R.id.bitcoin_1h_percent_mark)
        bitcoin24hMark = findViewById<TextView>(R.id.bitcoin_24h_percent_mark)
        bitcoin7dMark = findViewById<TextView>(R.id.bitcoin_7d_percent_mark)
    }

    private fun updateTextViews() {
        bitcoinCurrency!!.text = cryptoCurrencies!![0]["price_usd"].toString()
        bitcoin1h!!.text = cryptoCurrencies!![0]["percent_change_1h"].toString()
        bitcoin24h!!.text = cryptoCurrencies!![0]["percent_change_24h"].toString()
        bitcoin7d!!.text = cryptoCurrencies!![0]["percent_change_7d"].toString()
        if (bitcoin1h!!.text.contains('-')) {
            bitcoin1h!!.setTextColor(resources.getColor(R.color.negativeChange))
            bitcoin1hMark!!.setTextColor(resources.getColor(R.color.negativeChange))
        } else {
            bitcoin1h!!.setTextColor(resources.getColor(R.color.negativeChange))
            bitcoin1hMark!!.setTextColor(resources.getColor(R.color.positiveChange))
        }
        if (bitcoin24h!!.text.contains('-')) {
            bitcoin24h!!.setTextColor(resources.getColor(R.color.negativeChange))
            bitcoin24hMark!!.setTextColor(resources.getColor(R.color.negativeChange))
        } else {
            bitcoin24h!!.setTextColor(resources.getColor(R.color.positiveChange))
            bitcoin24hMark!!.setTextColor(resources.getColor(R.color.negativeChange))
        }
        if (bitcoin7d!!.text.contains('-')) {
            bitcoin7d!!.setTextColor(resources.getColor(R.color.negativeChange))
            bitcoin7dMark!!.setTextColor(resources.getColor(R.color.negativeChange))
        } else {
            bitcoin7d!!.setTextColor(resources.getColor(R.color.negativeChange))
            bitcoin7dMark!!.setTextColor(resources.getColor(R.color.positiveChange))
        }
    }

    private fun getCurrencies(): JsonArray<JsonObject>? {
        val responseData = URL("https://api.coinmarketcap.com/v1/ticker/").readText()
        val parser: Parser = Parser()
        val stringBuilder = StringBuilder(responseData)
        val currencies: JsonArray<JsonObject> = parser.parse(stringBuilder) as JsonArray<JsonObject>
        return currencies
    }

    private fun updateCurrencies() {
        launch {
            while(true) {
                cryptoCurrencies = getCurrencies()
                runOnUiThread() {
                    updateTextViews()
                }
                delay(10000)
            }
        }
    }
}
