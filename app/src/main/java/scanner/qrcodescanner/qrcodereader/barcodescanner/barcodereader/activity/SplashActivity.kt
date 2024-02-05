package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.view.View
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivitySplashBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SearchCountryModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SearchUrlsCountryModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants

class SplashActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashBinding
    private var isAdLoadTimeExceeded=false

    var urlList= arrayListOf(
        SearchUrlsCountryModelClass(
            "au",
            "Australia",
            R.drawable.flag_australia,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.au/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.au/sch/i.html?_nkw={code}&mkcid=1&mkrid=705-53470-19255-0&campid=53338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com.au/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://au.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.au/search?tbm=bks&q={code}","Google Book"),
            )
        ),
        SearchUrlsCountryModelClass("at","Austria",
            R.drawable.flag_austria,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.at/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.at/sch/i.html?_nkw={code}&mkcid=1&mkrid=5221-53469-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://at.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.au/search?tbm=bks&q={code}","Google Book"),

                )


        ),
        SearchUrlsCountryModelClass("be","Belgium",
            R.drawable.flag_belgium,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.be/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.befr.ebay.be/sch/i.html?_nkw={code}&mkcid=1&mkrid=1553-53471-19255-0&campid=53380577533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://partnerprogramma.bol.com/click/click?p=1&t=url&s=48523&f=TXL&url=https%3A%2F%2Fwww.lpol.com%2Fnl%2Fs%2F%3Fsearchtext%3D{code}%3Fcountry%3DBE&name=qrbot-ios","Bol"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com.au/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://be.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.be/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("br","Brazil",
            R.drawable.flag_brazil,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.br/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"http://www.americanas.com.br/busca/?conteudo={code}","Americanas"),
                SearchCountryModel(R.drawable.ic_back,"http://www.submarino.com.br/busca/?conteudo={code}","Submarino"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://br.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.br/search?tbm=bks&q={code}","Google Book"),

                )

        ),
        SearchUrlsCountryModelClass("ca","Canada",
            R.drawable.flag_canada,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.ca/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.ca/sch/i.html?_nkw={code}&mkcid=1&mkrid=706-53473-19255-0&campid=53338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com.ca/s/?keywords={code}&language=&tag=teacapps0b-20","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.wallmart.ca/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://ca.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.ca/search?tbm=bks&q={code}","Google Book")

            )

        ),
        SearchUrlsCountryModelClass("cn","China mainland",
            R.drawable.flag_china,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://www.baidu.com/s?wd={code}","Baidu"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.cn/s/?keywords={code}&language=en_CN","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://ca.openfoodfacts.org/product/{code}","Open Food Facts"),

                )
        ),
        SearchUrlsCountryModelClass("cz","Czechia",
            R.drawable.flag_czechia,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.cz/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://cz.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.cz/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("dk","Denmark",
            R.drawable.flag_denmark,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.dk/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.de/sch/i.html?_nkw={code}&mkcid=1&mkrid=707-53477-19255-0&campid=53:38057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.wallmart.ca/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://dk.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.dk/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("fi","Finland",
            R.drawable.flag_finland,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.fi/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.co.uk/sch/i.html?_nkw={code}&mkcid=1&mkrid=710-53481-19255-0&campid=53388057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://fi.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.fi/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("fr","France",
            R.drawable.flag_france,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.fr/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.fr/sch/i.html?_nkw={code}&mkcid=1&mkrid=709-53476-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.fr/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.auchan.fr/recherche?text={code}","Auchan"),
                SearchCountryModel(R.drawable.ic_back,"http://recherche.fnac.com/SearchResult/ResultList.aspx?Search={code}","Fnac"),
                SearchCountryModel(R.drawable.ic_back,"https://fr.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.fr/search?tbm=bks&q={code}","Google Book")

            )
        ),//france
        SearchUrlsCountryModelClass("de","Germany",
            R.drawable.flag_germany,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.de/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.de/sch/i.html?_nkw={code}&mkcid=1&mkrid=707-53477-19255-0&campid=53380575533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.de/s/?keywords={code}&language=en_GB","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://de.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.de/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("hk","Hong Kong",
            R.drawable.flag_hong_kong,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.hk/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.hk/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://hk.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.hk/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("in","India",
            R.drawable.flag_india,
            arrayListOf<SearchCountryModel>(
//                SearchCountryModel(R.drawable.ic_back,"https://google.co.in/search?q={code}","Google"),
//                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
//                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.in/s/?keywords={code}&language=","Amazon"),
//                SearchCountryModel(R.drawable.ic_back,"https://www.wallmart.com/search?q={code}","Walmart"),
//                SearchCountryModel(R.drawable.ic_back,"https://in.openfoodfacts.org/product/{code}","Open Food Facts"),
//                SearchCountryModel(R.drawable.ic_back,"https://www.google.co.in/search?tbm=bks&q={code}","Google Book")
                SearchCountryModel(R.drawable.ic_back,"https://google.co.in/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.in/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.wallmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://in.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.co.in/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("ie","Ireland",
            R.drawable.flag_ireland,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.ie/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.ie/sch/i.html?_nkw={code}&mkcid=1&mkrid=528253468-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://ie.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.ie/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("it","Italy",
            R.drawable.flag_italy,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.it/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.it/sch/i.html?_nkw={code}&mkcid=1&mkrid=724-53478-19255-0&campid=53380577533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.it/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://it.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.it/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("jp","Japan",
            R.drawable.flag_japan,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.co.jp/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.co.jp/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://jp.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.co.jp/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("li","Liechtenstein",
            R.drawable.flag_liechtenstein,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.li/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.ch/sch/i.html?_nkw={code}&mkcid=1&mkrid=5222-53480-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://li.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.li/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("lu","Luxembourg",
            R.drawable.flag_luxembourg,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.lu/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.fr/sch/i.html?_nkw={code}&mkcid=1&mkrid=709-53476-19255-0&campid=53338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&langguage=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://lu.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.lu/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("my","Malaysia",
            R.drawable.flag_malaysia,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.my/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.my/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://my.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.my/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("mx","Mexico",
            R.drawable.flag_mexico,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.mx/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com.mx/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://super.walmart.com.mx/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://mx.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.mx/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("mc","Monaco",
            R.drawable.flag_monaco,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.mn/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.fr/sch/i.html?_nkw={code}&mkcid=1&mkrid=709-53476-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://mc.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.mn/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("nl","Netherlands",
            R.drawable.flag_netherlands,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.nl/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.nl/sch/i.html?_nkw={code}&mkcid=1&mkrid=1346-53480-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.nl/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://nl.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.nl/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("nz","New Zealand",
            R.drawable.flag_new_zealand,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.co.nz/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.au/sch/i.html?_nkw={code}&mkcid=1&mkrid=705-53470-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://nz.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.nz/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("no","Norway",
            R.drawable.flag_norway,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.no/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.co.uk/sch/i.html?_nkw={code}&mkcid=1&mkrid=710-53481-19255-0&campid=53380557533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://no.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.no/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("ph","Philippines",
            R.drawable.flag_philippines,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.ph/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.ph/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=53388057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://ph.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.ph/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("pl","Poland",
            R.drawable.flag_poland,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.pl/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.pl/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://allergo.pl/listing?string={code}","Allegro"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://pl.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.pl/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("pt","Portugal",
            R.drawable.flag_portugal,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.pt/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.es/sch/i.html?_nkw={code}&mkcid=1&mkrid=1185-53479-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://pt.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.pt/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("ru","Russia",
            R.drawable.flag_russia,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.ru/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=53338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://ratengoods.com/product/{code}","Ratengoods"),
                SearchCountryModel(R.drawable.ic_back,"https://ru.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.ru/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("sm","San Marino",
            R.drawable.flag_san_marino,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.sm/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.it/sch/i.html?_nkw={code}&mkcid=1&mkrid=724-53478-19255-0&campid=53380577533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://sm.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.sm/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("sg","Singapore",
            R.drawable.flag_singapore,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.sg/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com.sg/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?q={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://sg.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.sg/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("kr","Sotuh Korea",
            R.drawable.flag_south_korea,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.co.kr/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://kr.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.co.kr/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("es","Spain",
            R.drawable.flag_spain,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.es/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.es/sch/i.html?_nkw={code}&mkcid=1&mkrid=1185-53479-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.es/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://es.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.es/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("se","Sweden",
            R.drawable.flag_sweden,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.se/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.co.uk/sch/i.html?_nkw={code}&mkcid=1&mkrid=710-53481-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://se.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.se/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("ch","Switzerland",
            R.drawable.flag_switzerland,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.ch/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.ch/sch/i.html?_nkw={code}&mkcid=1&mkrid=5222-53480-19255-0&campid=55338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://ch.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.ch/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("tr","Turkey",
            R.drawable.flag_turkey,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com.tr/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.hepsiburada.com/ara?q={code}","Hepsiburada"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://tr.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com.tr/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("gb","United Kingdom",
            R.drawable.flag_united_kingdom,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.co.uk/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.co.uk/sch/i.html?_nkw={code}&mkcid=1&mkrid=710-53481-19255-0&campid=5338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.co.uk/s/?keywords={code}&language=","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://uk.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.co.uk/search?tbm=bks&q={code}","Google Book")

            )
        ),
        SearchUrlsCountryModelClass("us","United States",
            R.drawable.flag_us,
            arrayListOf<SearchCountryModel>(
                SearchCountryModel(R.drawable.ic_back,"https://google.com/search?q={code}","Google"),
                SearchCountryModel(R.drawable.ic_back,"https://www.ebay.com/sch/i.html?_nkw={code}&mkcid=1&mkrid=711-53200-19255-0&campid=53338057533&toolid=20008&mkevt=1","eBay"),
                SearchCountryModel(R.drawable.ic_back,"https://www.amazon.com/s/?keywords={code}&language=&tag=teacapps-20","Amazon"),
                SearchCountryModel(R.drawable.ic_back,"https://www.walmart.com/search?query={code}","Walmart"),
                SearchCountryModel(R.drawable.ic_back,"https://www.bestbuy.com/site/searchpage.jsp?st={code}&","BestBuy"),
                SearchCountryModel(R.drawable.ic_back,"http://goto.target.com/c/369396/81938/2092?u=http%3A%2F%2Fwww.target.com%2Fs%3FsearchTerm%3D{code}","Target"),
                SearchCountryModel(R.drawable.ic_back,"https://www.homedepot.com/s/{code}","HomeDepot"),
                SearchCountryModel(R.drawable.ic_back,"https://us.openfoodfacts.org/product/{code}","Open Food Facts"),
                SearchCountryModel(R.drawable.ic_back,"https://www.google.com/search?tbm=bks&q={code}","Google Book")
            )

        )
    )
    var selectedCountry: SearchUrlsCountryModelClass?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(sharedPref.languageCode)
        super.onCreate(savedInstanceState)
        when(sharedPref.appTheme){
            0->{
//                application.setTheme(R.style.GreenTheme)
                setTheme(R.style.GreenTheme)
            }
            1->{
//                application.setTheme(R.style.BlueTheme)
                setTheme(R.style.BlueTheme)
            }
            2->{
//                application.setTheme(R.style.PurpleTheme)
                setTheme(R.style.PurpleTheme)
            }
            3->{
//                application.setTheme(R.style.YellowTheme)
                setTheme(R.style.YellowTheme)
            }
            4->{
//                application.setTheme(R.style.PinkTheme)
                setTheme(R.style.PinkTheme)
            }
        }
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Constants.isSplashScreen=true
        if(sharedPref.countryCodeSearchOption==""){
            val countryCode=getCountryCode()
            sharedPref.countryCodeSearchOption=countryCode

        }


        val filteredMainObjects = urlList.filter { it.countryCode ==  sharedPref.countryCodeSearchOption }

        val searchCountryObjectLists = filteredMainObjects.map { it.urlList }.flatten() as MutableList
        Constants.productUrlList=searchCountryObjectLists


        val onlyAdLoad= intent.getBooleanExtra("adLoader",false)
        if(onlyAdLoad){

            if(scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isNetworkAvailable(this)){
                loadAds()
//                startNextScreen()

                Handler(Looper.getMainLooper()).postDelayed({
//                    isAdLoadTimeExceeded=true
                    showInterAd()
                },4000)

            }else{
                Handler(Looper.getMainLooper()).postDelayed({
                   finish()
                },4000)
            }

//            Handler(Looper.getMainLooper()).postDelayed({
//                AdHandler.getInstance(this).loadAndShowInterstitialAd(this,getString(R.string.inter),object :
//                    OnAdClosedListener{
//
//                    override fun onAdClosed() {
//                        finish()
//                        Log.d("addClosed","hii")
//
//                    }
//                })
//            },4000)

        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                callNextActivity()
            },5000)
        }


    }
    fun getCountryCode():String{

        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.networkCountryIso

        return countryCodeValue
    }
    private fun loadAds(){
        scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).loadInterstitialAd(this,getString(R.string.inter))//getString(R.string.inter_id)

    }

    fun startNextScreen(){

        Thread{
            while (!scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isAdLoaded && !scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isAdLoadFailed && !isAdLoadTimeExceeded){
            }
            runOnUiThread{
               showInterAd()
            }
        }.start()
    }
    fun showInterAd(){

        if(scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isNetworkAvailable(this)){

            if(scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).interstitialAd!=null){

                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).loadInterstitialAdWithRandomNumber(this,getString(R.string.inter),1) {
                    finish()
                }
            }else{
                finish()
            }
//            else{
//                AdHandler.getInstance(this).showProgress(this)
//                AdHandler.getInstance(this).loadSplashInter(this,Constant.interstitialId
//                ) {
//                    AdHandler.getInstance(this@ScannerActivity).dismissProgress(this@ScannerActivity)
//                }
//            }
        }
    }

//    fun startNextScreen(){
//
//        Thread{
//            while (!AdHandler.getInstance(this).isAdLoaded && !AdHandler.getInstance(this).isAdLoadFailed && !isAdLoadTimeExceeded){
//            }
//            runOnUiThread{
//                callNextActivity()
//            }
//        }.start()
//    }

    private fun callNextActivity(){
        binding.animation.visibility= View.GONE
        if(sharedPref.isAppIntroShown){
            if(sharedPref.languageCode.isEmpty()){
                startActivity(Intent(this, LanguageActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }else{
            val intent= Intent(this, AppIntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants.isSplashScreen=false
    }

}