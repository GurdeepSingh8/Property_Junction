package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.adapters.RecAdapterClients;
import property.junction.co.methods.UniMethods;

public class TeamMemberOpen extends AppCompatActivity {

    Context context = TeamMemberOpen.this;

    UniMethods uniMethods = new UniMethods();

    RecyclerView rec_clients;
    RecAdapterClients recAdapterClients;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_open);

        rec_clients = findViewById(R.id.rec_clients);

        uniMethods.makeRecViewV(rec_clients, context);
        recAdapterClients = new RecAdapterClients(context, list);
        rec_clients.setAdapter(recAdapterClients);
        recAdapterClients.notifyDataSetChanged();

    }
}