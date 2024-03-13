package property.junction.co;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.adapters.RecAdapterAddPropertyImages;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.MyMethods;
import property.junction.co.methods.NumberToWords;
import property.junction.co.methods.UniMethods;

public class AddProperty extends AppCompatActivity {

    UniMethods uniMethods = new UniMethods();
    MyMethods myMethods = new MyMethods();
    InFirebase inFirebase;
    Activity activity = AddProperty.this;
    Context context = AddProperty.this;

    AppBarLayout app_bar_layout;
    Toolbar tb_add_property;

    CheckBox cv_agent, cv_owner, cv_for_sale, cv_for_rent;
    LinearLayoutCompat ll_sell_rent, ll_property_type_select;
    NestedScrollView nsv_property_type;
    ImageView iv_barrier_1;
    TextView tv_property_type_text;
    TextInputLayout til_property_location;
    ProgressBar prog_property_type_select, prog_main;
    FlexboxLayout flb_property_type;
    CountDownTimer countDownTimer;
    boolean shake = false;
    boolean shake_first = true;
    boolean showing_sale = true;

    ArrayList<TextView> views_list_all = new ArrayList<>();
    ArrayList<String> views_list_to_remove = new ArrayList<>();
    View selected_view = null;
    View iv_cross = null;


    ProgressBar progress_universal;

    String property_type = "none";
    String listing_purpose = "none";
    String listing_by = "none";

    Button btn_add_property;

    LayoutInflater inflater;

    FlexboxLayout flb_images;
    ArrayList<String> image_list = new ArrayList<>();
    LinearLayoutCompat.LayoutParams image_params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        inFirebase = new InFirebase(AddProperty.this);
        uniMethods.backButtonPanel(activity, "Add Property");
        inflater = (LayoutInflater) Objects.requireNonNull(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progress_universal = (ProgressBar) inflater.inflate(R.layout.progress_universal, null);

        app_bar_layout = findViewById(R.id.app_bar_layout);
        tb_add_property = findViewById(R.id.tb_add_property);
        nsv_property_type = findViewById(R.id.nsv_property_type);
        cv_agent = findViewById(R.id.cv_agent);
        cv_owner = findViewById(R.id.cv_owner);
        cv_for_sale = findViewById(R.id.cv_for_sale);
        cv_for_rent = findViewById(R.id.cv_for_rent);
        iv_barrier_1 = findViewById(R.id.iv_barrier_1);
        ll_sell_rent = findViewById(R.id.ll_sell_rent);
        ll_property_type_select = findViewById(R.id.ll_property_type_select);
        tv_property_type_text = findViewById(R.id.tv_property_type_text);
        flb_property_type = findViewById(R.id.flb_property_type);
        btn_add_property = findViewById(R.id.btn_add_property);
        prog_property_type_select = findViewById(R.id.prog_property_type_select);
        prog_main = findViewById(R.id.prog_main);

        app_bar_layout.setExpanded(true, true);

        shakeListingBy();

        cv_agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_sell_rent.setVisibility(View.VISIBLE);
                iv_barrier_1.setVisibility(View.VISIBLE);
                shakeListingPurpose();
                if (!cv_agent.isChecked()) {
                    cv_agent.performClick();
                }
                if (cv_owner.isChecked()) {
                    cv_owner.setChecked(false);
                }
            }
        });

        cv_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_sell_rent.setVisibility(View.VISIBLE);
                iv_barrier_1.setVisibility(View.VISIBLE);
                shakeListingPurpose();
                if (!cv_owner.isChecked()) {
                    cv_owner.performClick();
                }
                if (cv_agent.isChecked()) {
                    cv_agent.setChecked(false);
                }
            }
        });

        cv_for_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shake = false;
                showing_sale = true;
                listing_purpose = context.getString(R.string.for_sale);
                updatePropertyTypes(true);
                if (!cv_for_sale.isChecked()) {
                    cv_for_sale.performClick();
                }
                if (cv_for_rent.isChecked()) {
                    cv_for_rent.setChecked(false);
                }
            }
        });

        cv_for_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shake = false;
                showing_sale = false;
                listing_purpose = context.getString(R.string.for_rent);
                updatePropertyTypes(true);
                if (!cv_for_rent.isChecked()) {
                    cv_for_rent.performClick();
                }
                if (cv_for_sale.isChecked()) {
                    cv_for_sale.setChecked(false);
                }
            }
        });

        myMethods.assignPropertyTypesForSale(context, new MyMethods(new MyMethods.CallReturn() {
            @Override
            public void onCallReturn(String s, boolean b, Object sales_list) {
                createViewFromList((ArrayList<String>) sales_list);
                views_list_to_remove.add(context.getString(R.string.plot));
                views_list_to_remove.add(context.getString(R.string.commercial_plot));
                views_list_to_remove.add(context.getString(R.string.row_house));
                views_list_to_remove.add(context.getString(R.string.land));
            }
        }));

        /*app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                uniMethods.backButtonPanel(activity, String.valueOf(appBarLayout.getTotalScrollRange()));
                *//*if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    uniMethods.backButtonPanel(activity, "collapsed");
                } else {
                    uniMethods.backButtonPanel(activity, "expended");
                }*//*
            }
        });*/

    }

    private void shakeListingBy() {
        shake = false;
        shake_first = true;
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                if (shake) {
                    if (shake_first) {
                        shake_first = false;
                        uniMethods.zoomInOutView(cv_agent);
                    } else {
                        shake_first = true;
                        uniMethods.zoomInOutView(cv_owner);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (!cv_agent.isChecked() && !cv_owner.isChecked()) {
                    shake = true;
                    this.cancel();
                    this.start();
                }
            }
        }.start();
    }

    private void shakeListingPurpose() {
        countDownTimer.cancel();
        shake = false;
        shake_first = true;
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                if (shake) {
                    if (shake_first) {
                        shake_first = false;
                        uniMethods.zoomInOutView(cv_for_sale);
                    } else {
                        shake_first = true;
                        uniMethods.zoomInOutView(cv_for_rent);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (!cv_for_sale.isChecked() && !cv_for_rent.isChecked()) {
                    shake = true;
                    this.cancel();
                    this.start();
                }
            }
        }.start();
    }

    private void createViewFromList(ArrayList<String> property_types_list) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                uniMethods.getPxValueOfDp(context, 32));
        LinearLayout.LayoutParams params_for_iv_cross = new LinearLayout.LayoutParams(
                uniMethods.getPxValueOfDp(context, 32),
                uniMethods.getPxValueOfDp(context, 32));

        int m_6_dp = uniMethods.getPxValueOfDp(context, 6);
        int m_12_dp = uniMethods.getPxValueOfDp(context, 12);
        params.setMargins(m_12_dp, m_6_dp, 0, 0);
        params_for_iv_cross.setMargins(0, m_6_dp, 0, 0);
        iv_cross = (ImageView) inflater.inflate(R.layout.iv_cross, null);
        iv_cross.setLayoutParams(params_for_iv_cross);
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iv_view) {
                updatePropertyTypes(false);
            }
        });

        views_list_all.clear();
        for (String property_type_string : property_types_list) {
            TextView tv_property_type = (TextView) inflater.inflate(R.layout.tv_property_type, null);
            tv_property_type.setLayoutParams(params);
            tv_property_type.setText(property_type_string);
            tv_property_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    property_type = tv_property_type.getText().toString();
                    tv_property_type.setEnabled(false);
                    selected_view = view;
                    flb_property_type.removeAllViews();
                    flb_property_type.addView(selected_view);
                    selected_view.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.app_theme_color));
                    uniMethods.changeTextColor(context, (TextView) selected_view, R.color.background_0);
                    selected_view.setElevation(12f);
                    try {
                        flb_property_type.addView(iv_cross);
                        iv_cross.setEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prog_main.setVisibility(View.VISIBLE);
                    new CountDownTimer(300, 300) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            app_bar_layout.setExpanded(false, true);
                            updateAddPropertyView();
                        }
                    }.start();
                }
            });
            views_list_all.add(tv_property_type);
        }
    }

    private void updatePropertyTypes(boolean show_progress) {
        if (show_progress) {
            ll_property_type_select.setVisibility(View.INVISIBLE);
            prog_property_type_select.setVisibility(View.VISIBLE);
            new CountDownTimer(690, 690) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    updateFlexLayout();
                }
            }.start();
        } else {
            updateFlexLayout();
        }
    }

    private void updateFlexLayout() {
        if (selected_view != null) {
            btn_add_property.setEnabled(false);
            btn_add_property.setVisibility(View.GONE);
            selected_view.setElevation(0f);
            selected_view.setEnabled(true);
            selected_view.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.background_1));
            uniMethods.changeTextColor(context, (TextView) selected_view, R.color.text_body);
            selected_view = null;
        }
        nsv_property_type.removeAllViews();
        app_bar_layout.setExpanded(true, true);
        flb_property_type.removeAllViews();
        prog_property_type_select.setVisibility(View.INVISIBLE);
        ll_property_type_select.setVisibility(View.VISIBLE);
        for (TextView view : views_list_all) {
            if (showing_sale) {
                flb_property_type.addView(view);
            } else {
                if (!views_list_to_remove.contains(view.getText().toString())) {
                    flb_property_type.addView(view);
                }
            }
        }
    }

    private void updateAddPropertyView() {
        new CountDownTimer(300, 300) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                prog_main.setVisibility(View.GONE);
                LinearLayoutCompat view_add_property = (LinearLayoutCompat) inflater.inflate(R.layout.view_add_property, null);
                nsv_property_type.addView(view_add_property);
                btn_add_property.setEnabled(true);
                btn_add_property.setVisibility(View.VISIBLE);

                TextInputLayout til_property_name = view_add_property.findViewById(R.id.til_property_name);
                TextInputLayout til_property_price = view_add_property.findViewById(R.id.til_property_price);
                TextInputLayout til_property_location = view_add_property.findViewById(R.id.til_property_location);
                TextInputLayout til_amenities = view_add_property.findViewById(R.id.til_amenities);
                TextInputLayout til_other_details = view_add_property.findViewById(R.id.til_other_details);
                TextView tv_add_extra_field_for_team = view_add_property.findViewById(R.id.tv_add_extra_field_for_team);

                uniMethods.addListenerToCollapseAppBarLayoutWhenFocused(app_bar_layout, til_property_name.getEditText());
                uniMethods.addListenerToCollapseAppBarLayoutWhenFocused(app_bar_layout, til_property_price.getEditText());
                uniMethods.addListenerToCollapseAppBarLayoutWhenFocused(app_bar_layout, til_amenities.getEditText());
                uniMethods.addListenerToCollapseAppBarLayoutWhenFocused(app_bar_layout, til_other_details.getEditText());

                til_amenities.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        app_bar_layout.setExpanded(false);
                        uniMethods.scrollToView(nsv_property_type, til_amenities);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                til_other_details.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        app_bar_layout.setExpanded(false);
                        uniMethods.scrollToView(nsv_property_type, til_other_details);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                tv_add_extra_field_for_team.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        LinearLayoutCompat.LayoutParams params_for_extra_view = new LinearLayoutCompat.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        int margin_horizontal = uniMethods.getPxValueOfDp(context, 12);
                        params_for_extra_view.setMargins(margin_horizontal, margin_horizontal / 2, margin_horizontal, 0);

                        tv_add_extra_field_for_team.setEnabled(false);
                        tv_add_extra_field_for_team.setLayoutParams(params_for_extra_view);
                        tv_add_extra_field_for_team.setTextAppearance(context, R.style.body);
                        uniMethods.changeTextColor(context, tv_add_extra_field_for_team, R.color.text_body);
                        tv_add_extra_field_for_team.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(context, R.drawable.vector_info), null);
                        tv_add_extra_field_for_team.setText(Html.fromHtml("<b>" + context.getString(R.string.add_extra_field_heading)
                                + "</b><br><small>" + context.getString(R.string.add_extra_field_sub_heading) + "</small>"));
                        TextInputLayout view_til_extra_field = (TextInputLayout) inflater.inflate(R.layout.view_til_extra_field, null);
                        view_til_extra_field.setLayoutParams(params_for_extra_view);
                        view_add_property.addView(view_til_extra_field);
                        uniMethods.addListenerToCollapseAppBarLayoutWhenFocused(app_bar_layout, view_til_extra_field.getEditText());
                        view_til_extra_field.getEditText().addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                app_bar_layout.setExpanded(false);
                                uniMethods.scrollToView(nsv_property_type, tv_add_extra_field_for_team);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        app_bar_layout.setExpanded(false);
                        uniMethods.scrollToView(nsv_property_type, tv_add_extra_field_for_team);
                        view_til_extra_field.getEditText().requestFocus();
                    }
                });

                image_params = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        uniMethods.getPxValueOfDp(context, 90)
                );
                image_params.setMargins(uniMethods.getPxValueOfDp(context, 12), uniMethods.getPxValueOfDp(context, 12), 0, 0);
                ImageView row_add_image = (ImageView) inflater.inflate(R.layout.row_add_image, null);
                row_add_image.setLayoutParams(image_params);

                flb_images = view_add_property.findViewById(R.id.flb_images);
                flb_images.addView(row_add_image);
                row_add_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });

                TextView tv_price_helper = view_add_property.findViewById(R.id.tv_price_helper);
                Spinner spnr_price_unit = view_add_property.findViewById(R.id.spnr_price_unit);
                TextView tv_add_property_text = view_add_property.findViewById(R.id.tv_add_property_text);
                tv_add_property_text.setText(String.format("%s %s", property_type, context.getString(R.string.details)));
                tv_add_property_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iv_cross.performClick();
                    }
                });
                String[] units_list = uniMethods.getPriceUnits(context, listing_purpose, property_type);
                uniMethods.createSpinnerCustom(spnr_price_unit, units_list, context);

                spnr_price_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (adapterView.getSelectedItem().toString().equals("/-")) {
                            til_property_price.setHint("Total " + context.getString(R.string.price));
                        } else {
                            til_property_price.setHint(context.getString(R.string.price) + " " + adapterView.getSelectedItem().toString().replace("/", "per"));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                til_property_price.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        til_property_price.getEditText().removeTextChangedListener(this);
                        String text = "";
                        if (charSequence.toString().length() > 0) {
                            long obj = Long.parseLong(charSequence.toString().replaceAll(",", ""));
                            tv_price_helper.setText(NumberToWords.convertToIndianEnglish((int) obj));
                            text = uniMethods.getIndianFormattedCurrencyAmount(obj);
                            //Toast.makeText(context, NumberToWords.convert(obj), Toast.LENGTH_SHORT).show();
                        } else {
                            tv_price_helper.setText(context.getString(R.string.price_default_helper));
                        }
                        til_property_price.getEditText().setText(text);
                        til_property_price.getEditText().setSelection(til_property_price.getEditText().getText().length());
                        til_property_price.getEditText().addTextChangedListener(this);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                til_property_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddProperty.this, GoogleMapAddress.class);
                        activity.startActivityForResult(intent, 2);
                    }
                });

            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if (data.getClipData() != null) {
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            ConstraintLayout row_image = (ConstraintLayout) inflater.inflate(R.layout.row_image, null);
                            row_image.setLayoutParams(image_params);
                            ImageView iv_img = row_image.findViewById(R.id.iv_img);
                            ImageView iv_delete = row_image.findViewById(R.id.iv_delete);
                            Glide.with(iv_img).load(data.getClipData().getItemAt(i).getUri().toString()).into(iv_img);
                            iv_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    flb_images.removeView(row_image);
                                    updateImageCounts();
                                }
                            });
                            flb_images.addView(row_image);
                        }
                        updateImageCounts();
                    } else {
                        ConstraintLayout row_image = (ConstraintLayout) inflater.inflate(R.layout.row_image, null);
                        row_image.setLayoutParams(image_params);
                        ImageView iv_img = row_image.findViewById(R.id.iv_img);
                        ImageView iv_delete = row_image.findViewById(R.id.iv_delete);
                        Glide.with(iv_img).load(data.getData().toString()).into(iv_img);
                        iv_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flb_images.removeView(row_image);
                                updateImageCounts();
                            }
                        });
                        flb_images.addView(row_image);
                        updateImageCounts();
                    }
                } else {
                    Toast.makeText(context, "Empty image data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateImageCounts() {
        try {
            for (int i = 0; i < flb_images.getChildCount(); i++) {
                if (i > 0) {
                    TextView textView = (TextView) flb_images.getChildAt(i).findViewById(R.id.tv_image_count);
                    textView.setText(String.valueOf(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (nsv_property_type.getChildCount() > 0) {
            if (iv_cross != null) {
                iv_cross.performClick();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
}