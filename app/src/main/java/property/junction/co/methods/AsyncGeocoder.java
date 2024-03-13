package property.junction.co.methods;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class AsyncGeocoder extends AsyncTask<AsyncGeocoderObject, Void, List<Address>> {

    private AsyncGeocoderObject.ResultGeocoder resultGeocoder;

    @Override
    protected List<Address> doInBackground(AsyncGeocoderObject... asyncGeocoderObjects) {
        List<Address> addresses = null;
        AsyncGeocoderObject asyncGeocoderObject = asyncGeocoderObjects[0];
        resultGeocoder = asyncGeocoderObject.resultGeocoder;
        try {
            addresses = asyncGeocoderObject.geocoder.getFromLocation(asyncGeocoderObject.location.getLatitude(),
                    asyncGeocoderObject.location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        Log.v("onPostExecute", "location: " + addresses);
        resultGeocoder.onResult(addresses);

    }
}

