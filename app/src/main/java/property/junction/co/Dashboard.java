package property.junction.co;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.adapters.RecAdapterClients;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.MyMethods;
import property.junction.co.methods.UniMethods;

public class Dashboard extends AppCompatActivity {

    Context context = Dashboard.this;
    Activity activity = Dashboard.this;

    MyMethods myMethods = new MyMethods();
    UniMethods uniMethods = new UniMethods();
    InFirebase inFirebase;

    RecyclerView rec_clients;
    RecAdapterClients recAdapterClients;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    BottomNavigationView bottomNavigationView;

    AppBarLayout app_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        inFirebase = new InFirebase(Dashboard.this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        app_bar_layout = findViewById(R.id.app_bar_layout);

        rec_clients = findViewById(R.id.rec_clients);

        ArrayList<String> list_categories = new ArrayList<>();
        list_categories.add("All Clients");
        list_categories.add("Team");
        list_categories.add("Tele Callers");
        list_categories.add("Lead Providers");
        list_categories.add("Groups");

        app_bar_layout.addView(myMethods.addCategories(activity, list_categories,
                new MyMethods(new MyMethods.CallReturn() {
                    @Override
                    public void onCallReturn(String selected_category, boolean b, Object o) {
                        if (b) {
                            switch (list_categories.indexOf(selected_category) + 1) {
                                case 1:
                                    inFirebase.getClientList(new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                                        @Override
                                        public void onResultFromFirebase(String clients_found, boolean condition, Object value) {
                                            if (clients_found.equals("0")) {
                                                myMethods.addMessageViewInConstraintsLayout(activity,
                                                        R.id.cons_parent, R.drawable.img_customer_filled,
                                                        "Welcome to your clients",
                                                        "Add your clients to manage them more effectively, share property details in single click &amp; get notifications on every followup",
                                                        "Add Clients",
                                                        0, 0, 0, 0,
                                                        new MyMethods(new MyMethods.CallReturn() {
                                                            @Override
                                                            public void onCallReturn(String s, boolean b, Object o) {
                                                                if (b) {
                                                                    Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }));
                                            }
                                        }
                                    }));
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                })));

        uniMethods.makeRecViewV(rec_clients, context);
        recAdapterClients = new RecAdapterClients(context, list);
        rec_clients.setAdapter(recAdapterClients);

        switch (uniMethods.getLastClickedButton(context)) {
            case "2":
                bottomNavigationView.setSelectedItemId(R.id.nav_properties);
                break;
            default:
                bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
                break;
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        startActivity(new Intent(context, Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_properties:
                        return true;
                    case R.id.nav_follow_ups:
                        /*startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0, 0);*/
                        return true;
                    case R.id.nav_leads:
                        return true;
                }
                return false;
            }
        });

    }
}