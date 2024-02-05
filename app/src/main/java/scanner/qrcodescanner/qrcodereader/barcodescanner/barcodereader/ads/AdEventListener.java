package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads;

public interface AdEventListener {

  void onAdLoaded(Object adObject);
    void onAdClosed();
    void onLoadError(String errorCode);
}
