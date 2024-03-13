package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.adapters.RecAdapterClients;
import property.junction.co.adapters.RecAdapterFollowup;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.UniMethods;

public class ClientDetails extends AppCompatActivity {

    Context context = ClientDetails.this;
    Activity activity = ClientDetails.this;
    UniMethods uniMethods = new UniMethods();
    InFirebase inFirebase;

    RecyclerView rec_follow_up;
    RecAdapterFollowup recAdapterFollowup;
    ArrayList<HashMap<String, Object>> list_follow_up = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        inFirebase = new InFirebase(activity);

        rec_follow_up = findViewById(R.id.rec_follow_up);

        uniMethods.makeRecViewV(rec_follow_up, context);
        recAdapterFollowup = new RecAdapterFollowup(context, list_follow_up);
        rec_follow_up.setAdapter(recAdapterFollowup);

        inFirebase.getFollowUps(uniMethods.getMyId(context), new InFirebase(activity, new InFirebase.ResultFromFirebase() {
            @Override
            public void onResultFromFirebase(String callback, boolean condition, Object value) {
                if (condition) {
                    list_follow_up.addAll((ArrayList<HashMap<String, Object>>) value);
                    recAdapterFollowup.notifyDataSetChanged();
                }
            }
        }));


    }
}