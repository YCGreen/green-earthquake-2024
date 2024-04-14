package green.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import green.earthquake.json.Feature;
import green.earthquake.json.FeatureCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();
    private FeatureCollection fc = new FeatureCollection();

    public EarthquakeFrame() {

        setTitle("EarthquakeFrame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        EarthquakeService service = new EarthquakeServiceFactory().getService();

        JRadioButton rbOne = new JRadioButton("One Hour");
        JRadioButton rbThirty = new JRadioButton("30 Days");
        ButtonGroup rbGroup = new ButtonGroup();
        rbGroup.add(rbOne);
        rbGroup.add(rbThirty);

        rbOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Disposable disposable = service.oneHour()
                        // tells Rx to request the data on a background Thread
                        .subscribeOn(Schedulers.io())
                        // tells Rx to handle the response on Swing's main Thread
                        .observeOn(SwingSchedulers.edt())
                        //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                        .subscribe(
                                (response) -> handleResponse(response),
                                Throwable::printStackTrace);
            }
        });

        rbThirty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Disposable disposable = service.thirtyDays()
                        // tells Rx to request the data on a background Thread
                        .subscribeOn(Schedulers.io())
                        // tells Rx to handle the response on Swing's main Thread
                        .observeOn(SwingSchedulers.edt())
                        //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                        .subscribe(
                                (response) -> handleResponse(response),
                                Throwable::printStackTrace);
            }
        });


        JPanel rbPanel = new JPanel(new GridLayout(0, 2));
        rbPanel.add(rbOne);
        rbPanel.add(rbThirty);

        ListSelectionModel lsm = jlist.getSelectionModel();
        lsm.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = jlist.getSelectedIndex();
                double lat = fc.features[index].geometry.getLatitude();
                double lon = fc.features[index].geometry.getLongitude();
                try {
                    Desktop.getDesktop().browse(new URI(String.format("http://maps.google.com/?q=%f,%f", lat, lon)));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        add(rbPanel, BorderLayout.PAGE_START);
        add(jlist, BorderLayout.CENTER);

    }

    private void handleResponse(FeatureCollection response) {

        fc = response;
        String[] listData = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData[i] = feature.properties.getMag() + " " + feature.properties.getPlace();
        }
        jlist.setListData(listData);
    }

    public static void main(String[] args) {
        new EarthquakeFrame().setVisible(true);
    }

}