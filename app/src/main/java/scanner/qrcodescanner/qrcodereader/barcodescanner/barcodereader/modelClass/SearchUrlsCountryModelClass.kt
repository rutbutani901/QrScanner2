package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass
//
//data class SearchUrlsCountryModelClass(
//    var countryCode:String,
//    var countryName:String,
//    var countryFlag:Int,
//   var googleUrl:SearchCountryModel?=null,
//   var amazonUrl:SearchCountryModel?=null,
//   var ebayUrl:SearchCountryModel?=null,
//   var wallmartUrl:SearchCountryModel?=null,
//   var openFoodFactsUrl:SearchCountryModel?=null,
//   var googleBookUrl:SearchCountryModel?=null,
//   var bolUrl:SearchCountryModel?=null,
//   var americanUrl:SearchCountryModel?=null,
//   var submarino:SearchCountryModel?=null,
//):java.io.Serializable

data class SearchUrlsCountryModelClass(
    var countryCode:String,
    var countryName:String,
    var countryFlag:Int,
    var urlList:ArrayList<SearchCountryModel>
):java.io.Serializable

data class SearchCountryModel(
    var icon:Int,
    var url:String,
    var title:String
)

data class CountryModel(
    var icon:Int,
    var code:String,
    var title:String
)

//data class SearchUrlsCountryModelClass(
//    var countryCode:String,
//    var countryName:String,
//    var googleUrl:String,
//    var amazonUrl:String,
//    var ebayUrl:String,
//    var wallmartUrl:String,
//    var openFoodFactsUrl:String,
//    var googleBookUrl:String,
//    var bolUrl:String,
//    var americanUrl:String="",
//    var submarino:String=""
//)