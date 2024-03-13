package property.junction.co;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.methods.InFirebase;
import property.junction.co.methods.UniMethods;
import property.junction.co.rec_adapters.RecAdapterCountryList;

public class CountryCodes extends AppCompatActivity implements UniMethods.Notifier, InFirebase.ResultFromFirebase, RecAdapterCountryList.Notify {

    Context context = CountryCodes.this;
    Activity activity = CountryCodes.this;
    EditText et_search_address;

    RecyclerView rec_view_country;
    RecAdapterCountryList recAdapterCountryList;
    ArrayList<HashMap<String, Object>> country_list = new ArrayList<>();

    UniMethods uniMethods = new UniMethods();
    InFirebase inFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_codes);
        inFirebase = new InFirebase(activity);

        rec_view_country = findViewById(R.id.rec_view_country);
        et_search_address = findViewById(R.id.et_search_address);

        uniMethods.backButtonPanel(CountryCodes.this, "");

        uniMethods.makeRecViewV(rec_view_country, context);

        inFirebase.getCountryList(new InFirebase(activity, new InFirebase.ResultFromFirebase() {
            @Override
            public void onResultFromFirebase(String callback, boolean condition, Object value) {
                if (condition) {
                    country_list = (ArrayList<HashMap<String, Object>>) value;
                    recAdapterCountryList = new RecAdapterCountryList(context, country_list, CountryCodes.this);
                    rec_view_country.setAdapter(recAdapterCountryList);
                    recAdapterCountryList.notifyDataSetChanged();
                }
            }
        }));

        et_search_address.getBackground().setColorFilter(getResources().getColor(R.color.app_theme_color), PorterDuff.Mode.SRC_IN);

        et_search_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() > 0) {
                    ArrayList<HashMap<String, Object>> filtered_country_list = new ArrayList<>();
                    uniMethods.simpleListSearch(country_list, "country", charSequence.toString(), new UniMethods(new UniMethods.Notifier() {
                        @Override
                        public void onTaskCompleted(String s, boolean b, Object o) {
                            filtered_country_list.addAll((ArrayList<HashMap<String, Object>>) o);
                            uniMethods.simpleListSearch(country_list, "country_code", charSequence.toString(), new UniMethods(new UniMethods.Notifier() {
                                @Override
                                public void onTaskCompleted(String s, boolean b, Object o) {
                                    filtered_country_list.addAll((ArrayList<HashMap<String, Object>>) o);
                                    recAdapterCountryList = new RecAdapterCountryList(context, filtered_country_list, CountryCodes.this);
                                    rec_view_country.setAdapter(recAdapterCountryList);
                                    recAdapterCountryList.notifyDataSetChanged();
                                }
                            }));
                        }
                    }));
                } else {
                    recAdapterCountryList = new RecAdapterCountryList(context, country_list, CountryCodes.this);
                    rec_view_country.setAdapter(recAdapterCountryList);
                    recAdapterCountryList.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onNotify(int i, HashMap<String, Object> hashmap) {
        Intent intent = new Intent();
        intent.putExtra("data", hashmap);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResultFromFirebase(String callback, boolean condition, Object value) {

    }

    @Override
    public void onTaskCompleted(String s, boolean b, Object o) {

    }
}