package info.nullteam.de1ay.cryptocurrencytracker

class Ticker(
        val id:String,
        val name:String,
        val symbol:String,
        var price_usd:String,
        var percent_change_1h:String,
        var percent_change_24h:String,
        var percent_change_7d:String
)