package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import property.junction.co.methods.UniMethods;

public class InviteTeamMember extends AppCompatActivity {

    Activity activity = InviteTeamMember.this;
    Activity context = InviteTeamMember.this;
    UniMethods uniMethods = new UniMethods();

    TextView tv_show_advance_permissions;
    NestedScrollView nsv;
    LinearLayoutCompat ll_inner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_member);

        uniMethods.backButtonPanel(activity, "Invite new team member");

        tv_show_advance_permissions = findViewById(R.id.tv_show_advance_permissions);
        ll_inner = findViewById(R.id.ll_inner);
        nsv = findViewById(R.id.nsv);

        tv_show_advance_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView iv_wide_line = findViewById(R.id.iv_wide_line);
                iv_wide_line.setVisibility(View.VISIBLE);

                int dp_12 = uniMethods.getPxValueOfDp(context, 12);
                LinearLayoutCompat.LayoutParams params_for_extra_view = new LinearLayoutCompat.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                tv_show_advance_permissions.setEnabled(false);
                tv_show_advance_permissions.setPadding(dp_12, dp_12, dp_12, dp_12);
                tv_show_advance_permissions.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                tv_show_advance_permissions.setLayoutParams(params_for_extra_view);
                tv_show_advance_permissions.setTextAppearance(context, R.style.body);
                uniMethods.changeTextColor(context, tv_show_advance_permissions, R.color.text_body);
                tv_show_advance_permissions.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(context, R.drawable.vector_info), null);
                tv_show_advance_permissions.setText(Html.fromHtml("<b>" + "Advance permissions" + "</b>"));

                View view_added = (ViewGroup) uniMethods.addViewIntoLinearLayoutCompat(activity, R.id.ll_inner,
                        R.layout.view_ll_advance_permissions,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0, 0, 0, 0);

                EditText et_last = view_added.findViewById(R.id.et_last);
                et_last.requestFocus();
                uniMethods.hideKeyboardFromActivity(activity);

            }
        });

    }
}