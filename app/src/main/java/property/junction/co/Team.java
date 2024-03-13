package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.adapters.RecAdapterTeamMembers;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.UniMethods;

public class Team extends AppCompatActivity {

    Activity activity = Team.this;
    Context context = Team.this;
    InFirebase inFirebase;
    UniMethods uniMethods = new UniMethods();

    LayoutInflater inflater;

    ConstraintLayout cons_parent;
    AppBarLayout app_bar_layout;

    RecyclerView rec_team;
    RecAdapterTeamMembers recAdapterTeamMembers;
    ArrayList<HashMap<String, Object>> list_members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        inFirebase = new InFirebase(activity);
        uniMethods.backButtonPanel(activity, "Team");
        inflater = (LayoutInflater) Objects.requireNonNull(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        app_bar_layout = findViewById(R.id.app_bar_layout);
        cons_parent = findViewById(R.id.cons_parent);
        rec_team = findViewById(R.id.rec_team);

        uniMethods.makeRecViewV(rec_team, context);

        recAdapterTeamMembers = new RecAdapterTeamMembers(context, list_members);
        rec_team.setAdapter(recAdapterTeamMembers);
        recAdapterTeamMembers.notifyDataSetChanged();

        ConstraintLayout.LayoutParams params_for_empty_view = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayoutCompat view_ll_no_team = (LinearLayoutCompat) inflater.inflate(R.layout.view_ll_show_message, null);
        view_ll_no_team.setLayoutParams(params_for_empty_view);
        //cons_parent.addView(view_ll_no_team);

        Button btn_action = view_ll_no_team.findViewById(R.id.btn_action);
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}