package property.junction.co.methods;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.TextView;

import java.util.List;

public class AsyncGeocoderObject {
    public Location location; // location to get address from
    Geocoder geocoder; // the geocoder
    ResultGeocoder resultGeocoder;

    public AsyncGeocoderObject(Geocoder geocoder, Location location, ResultGeocoder resultGeocoder) {
        this.geocoder = geocoder;
        this.location = location;
        this.resultGeocoder = resultGeocoder;
    }

    public interface ResultGeocoder {
        void onResult(List<Address> adds);
    }
}
