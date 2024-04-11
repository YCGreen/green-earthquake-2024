package green.earthquake;

import javax.swing.*;

import green.earthquake.json.Feature;
import green.earthquake.json.FeatureCollection;
import green.earthquake.json.Properties;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EarthquakeFrame extends JFrame {

    JList<String> jList = new JList<>();

    public EarthquakeFrame() {
        setSize(800, 600);
        setTitle("Earthquake");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        EarthquakeService service = new EarthquakeServiceFactory().getService();

        Disposable disposable = service.oneHour()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread
                .observeOn(SwingSchedulers.edt())
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(response -> handleResponse(response),
        Throwable::printStackTrace);


    }

    private void handleResponse(FeatureCollection response) {
    }

    private void displayFeature(Feature feature) {

    }


}
