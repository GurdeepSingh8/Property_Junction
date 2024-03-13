package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import property.junction.co.methods.MyMethods;
import property.junction.co.methods.UniMethods;

public class AddClient extends AppCompatActivity {

    Activity activity = AddClient.this;

    UniMethods uniMethods = new UniMethods();
    MyMethods myMethods = new MyMethods();

    TextView tv_add_more_numbers, tv_client_name_tips, tv_save_in_contacts_helper;
    ConstraintLayout cons_number_1, cons_number_2;
    TextInputLayout til_country_code_2, til_client_phone_2, til_client_note;
    NestedScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        uniMethods.backButtonPanel(activity, "Add new client");

        tv_add_more_numbers = findViewById(R.id.tv_add_more_numbers);
        tv_client_name_tips = findViewById(R.id.tv_client_name_tips);
        tv_save_in_contacts_helper = findViewById(R.id.tv_save_in_contacts_helper);
        til_country_code_2 = findViewById(R.id.til_country_code_2);
        til_client_phone_2 = findViewById(R.id.til_client_phone_2);
        til_client_note = findViewById(R.id.til_client_note);
        cons_number_1 = findViewById(R.id.cons_number_1);
        cons_number_2 = findViewById(R.id.cons_number_2);
        sv = findViewById(R.id.sv);

        tv_add_more_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cons_number_2.getVisibility() == View.VISIBLE) {
                    cons_number_2.setVisibility(View.GONE);
                    tv_add_more_numbers.setText(activity.getString(R.string.more_phone_number));
                    tv_add_more_numbers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vector_arrow_down, 0);
                } else {
                    cons_number_2.setVisibility(View.VISIBLE);
                    tv_add_more_numbers.setText(activity.getString(R.string.remove_phone_number));
                    tv_add_more_numbers.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vector_arrow_up, 0);
                }
                til_country_code_2.getEditText().setText(activity.getString(R.string.default_country_code));
                til_client_phone_2.getEditText().setText("");
            }
        });

        tv_client_name_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = "<b><big>" + activity.getString(R.string.enter_client_name_hint) + " </big></b>" + activity.getString(R.string.client_name_brief) + "<br><br>" +
                        "<b><big>" + activity.getString(R.string.enter_client_display_name_hint) + " </big></b>" + activity.getString(R.string.display_name_brief) + "<br>";
                String sub_header = activity.getString(R.string.pro_tip_client_name);
                myMethods.showTextBottomSheet(activity,
                        0,
                        0,
                        R.drawable.vector_info,
                        "left",
                        header,
                        sub_header,
                        activity.getString(R.string.ok_got_it_btn_text),
                        getSupportFragmentManager(),
                        true,
                        new UniMethods(new UniMethods.Notifier() {
                            @Override
                            public void onTaskCompleted(String s, boolean b, Object o) {

                            }
                        }));
            }
        });

        til_client_note.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                uniMethods.scrollToView(sv, tv_save_in_contacts_helper);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}