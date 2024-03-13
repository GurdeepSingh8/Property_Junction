package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import property.junction.co.methods.UniMethods;

public class TeamMemberProfile extends AppCompatActivity {

    Activity activity = TeamMemberProfile.this;
    Context context = TeamMemberProfile.this;
    UniMethods uniMethods = new UniMethods();

    NestedScrollView nsv;
    LinearLayoutCompat ll_container;
    TextView tv_advance_permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_profile);

        nsv = findViewById(R.id.nsv);
        ll_container = findViewById(R.id.ll_container);
        tv_advance_permissions = findViewById(R.id.tv_advance_permissions);

        ImageView iv_wide_line = findViewById(R.id.iv_wide_line);
        iv_wide_line.setVisibility(View.VISIBLE);

        int dp_12 = uniMethods.getPxValueOfDp(context, 12);
        LinearLayoutCompat.LayoutParams params_for_extra_view = new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_advance_permissions.setEnabled(false);
        tv_advance_permissions.setPadding(dp_12, dp_12, dp_12, dp_12);
        tv_advance_permissions.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        tv_advance_permissions.setLayoutParams(params_for_extra_view);
        tv_advance_permissions.setTextAppearance(context, R.style.body);
        uniMethods.changeTextColor(context, tv_advance_permissions, R.color.text_body);
        tv_advance_permissions.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(context, R.drawable.vector_info), null);
        tv_advance_permissions.setText(Html.fromHtml("<b>" + "Update advance permissions" + "</b>"));

        View view_added = (ViewGroup) uniMethods.addViewIntoLinearLayoutCompat(activity, R.id.ll_container,
                R.layout.view_ll_advance_permissions,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0, 0, 0, 0);

        EditText et_last = view_added.findViewById(R.id.et_last);
        et_last.requestFocus();
        uniMethods.hideKeyboardFromActivity(activity);

    }
}