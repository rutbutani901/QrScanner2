package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util

import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SearchCountryModel

class Constants {

   companion object{
       var isFavEnabledInHistory=false
       var selectedFilterType=-1
       var isScanHistoryEmpty=false
       var isCreateHistoryEmpty=false

       public var isSplashScreen=false
       public var openOtherScreenFromApp=false

       var productUrlList= mutableListOf<SearchCountryModel>()
   }
}