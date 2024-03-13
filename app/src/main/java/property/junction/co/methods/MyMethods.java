package property.junction.co.methods;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.R;
import property.junction.co.bottom_sheets.AddFollowupBottomSheet;
import property.junction.co.bottom_sheets.AddFollowupEventBottomSheet;
import property.junction.co.bottom_sheets.CheckPinBottomSheet;
import property.junction.co.bottom_sheets.ConfirmationBottomSheet;
import property.junction.co.bottom_sheets.ImageTextBottomSheet;
import property.junction.co.bottom_sheets.LottieTextBottomSheet;
import property.junction.co.bottom_sheets.SelectTilesBottomSheet;
import property.junction.co.bottom_sheets.ShowInfoBottomSheet;
import property.junction.co.bottom_sheets.UpdateAvailableBottomSheet;

public class MyMethods {
    CallReturn callReturn;
    String final_value = "";
    String value = "0";
    UniMethods uniMethods;

    public MyMethods() {

    }

    public interface CallReturn {
        void onCallReturn(String s, boolean b, Object o);
    }

    public MyMethods(CallReturn callReturn) {
        this.callReturn = callReturn;
        uniMethods = new UniMethods();
    }

    public AlertDialog displayProgress(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(null);
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.progress_bar, null);
        popupView.setVisibility(View.VISIBLE);

        builder.setView(popupView);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return alertDialog;
    }

    public void showAlertForNumberWhatsapp(Context context, ArrayList<HashMap<String, Object>> phone_numbers, String user_name, String callback) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(null);
        // Set Alert Title
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.alert_mobile_numbers_for_whatsapp, null);

        TextView tv_header = popupView.findViewById(R.id.tv_header);
        TextView tv_n1 = popupView.findViewById(R.id.tv_n1);
        TextView tv_n2 = popupView.findViewById(R.id.tv_n2);
        TextView tv_n3 = popupView.findViewById(R.id.tv_n3);
        TextView tv_bottom_line = popupView.findViewById(R.id.tv_bottom_line);
        tv_header.setText(user_name);
        switch (phone_numbers.size()) {
            case 1:
                String number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.GONE);
                tv_n3.setVisibility(View.GONE);
                tv_bottom_line.setText("This Client has only 1 number saved as whatsapp contact");
                break;
            case 2:
                number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                String number2 = String.valueOf(
                        String.valueOf(phone_numbers.get(1).get("country_code"))
                                + String.valueOf(phone_numbers.get(1).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n2.setText("✹✹✹✹✹✹✹✹" + number2.substring(number2.length() - 2));
                tv_n2.setTag(number2);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.VISIBLE);
                tv_n3.setVisibility(View.GONE);
                tv_bottom_line.setText("This Client has 2 numbers saved as whatsapp contact");
                break;
            case 3:
                number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                number2 = String.valueOf(
                        String.valueOf(phone_numbers.get(1).get("country_code"))
                                + String.valueOf(phone_numbers.get(1).get("phone_number"))).replace("null", "0");
                String number3 = String.valueOf(
                        String.valueOf(phone_numbers.get(2).get("country_code"))
                                + String.valueOf(phone_numbers.get(2).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n2.setText("✹✹✹✹✹✹✹✹" + number2.substring(number2.length() - 2));
                tv_n2.setTag(number2);
                tv_n3.setText("✹✹✹✹✹✹✹✹" + number3.substring(number3.length() - 2));
                tv_n3.setTag(number3);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.VISIBLE);
                tv_n3.setVisibility(View.VISIBLE);
                tv_bottom_line.setText("This Client has 3 numbers saved as whatsapp contact");
                break;
            default:
                break;
        }
        tv_n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callReturn.onCallReturn(callback, true, tv_n1.getTag().toString());
            }
        });

        tv_n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callReturn.onCallReturn(callback, true, tv_n2.getTag().toString());
            }
        });

        tv_n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callReturn.onCallReturn(callback, true, tv_n3.getTag().toString());
            }
        });

        builder.setView(popupView);
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(true);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }

    public void showAlertForNumberWhatsapp(Context context, ArrayList<HashMap<String, Object>> phone_numbers, String user_name, MyMethods myMethods) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(null);
        // Set Alert Title
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.alert_mobile_numbers_for_whatsapp, null);

        TextView tv_header = popupView.findViewById(R.id.tv_header);
        TextView tv_n1 = popupView.findViewById(R.id.tv_n1);
        TextView tv_n2 = popupView.findViewById(R.id.tv_n2);
        TextView tv_n3 = popupView.findViewById(R.id.tv_n3);
        TextView tv_bottom_line = popupView.findViewById(R.id.tv_bottom_line);
        tv_header.setText(user_name);
        switch (phone_numbers.size()) {
            case 1:
                String number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.GONE);
                tv_n3.setVisibility(View.GONE);
                tv_bottom_line.setText("This Client has only 1 number saved as whatsapp contact");
                break;
            case 2:
                number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                String number2 = String.valueOf(
                        String.valueOf(phone_numbers.get(1).get("country_code"))
                                + String.valueOf(phone_numbers.get(1).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n2.setText("✹✹✹✹✹✹✹✹" + number2.substring(number2.length() - 2));
                tv_n2.setTag(number2);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.VISIBLE);
                tv_n3.setVisibility(View.GONE);
                tv_bottom_line.setText("This Client has 2 numbers saved as whatsapp contact");
                break;
            case 3:
                number1 = String.valueOf(
                        String.valueOf(phone_numbers.get(0).get("country_code"))
                                + String.valueOf(phone_numbers.get(0).get("phone_number"))).replace("null", "0");
                number2 = String.valueOf(
                        String.valueOf(phone_numbers.get(1).get("country_code"))
                                + String.valueOf(phone_numbers.get(1).get("phone_number"))).replace("null", "0");
                String number3 = String.valueOf(
                        String.valueOf(phone_numbers.get(2).get("country_code"))
                                + String.valueOf(phone_numbers.get(2).get("phone_number"))).replace("null", "0");
                tv_n1.setText("✹✹✹✹✹✹✹✹" + number1.substring(number1.length() - 2));
                tv_n1.setTag(number1);
                tv_n2.setText("✹✹✹✹✹✹✹✹" + number2.substring(number2.length() - 2));
                tv_n2.setTag(number2);
                tv_n3.setText("✹✹✹✹✹✹✹✹" + number3.substring(number3.length() - 2));
                tv_n3.setTag(number3);
                tv_n1.setVisibility(View.VISIBLE);
                tv_n2.setVisibility(View.VISIBLE);
                tv_n3.setVisibility(View.VISIBLE);
                tv_bottom_line.setText("This Client has 3 numbers saved as whatsapp contact");
                break;
            default:
                break;
        }
        builder.setView(popupView);
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(true);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
        tv_n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                callReturn.onCallReturn("callback", true, tv_n1.getTag().toString());
            }
        });

        tv_n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                callReturn.onCallReturn("callback", true, tv_n2.getTag().toString());
            }
        });

        tv_n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                callReturn.onCallReturn("callback", true, tv_n3.getTag().toString());
            }
        });
    }

    public void alertForShowData(Context context, String data) {
        String data_string = data.replace("},{", "\n------------\n").replace("[", "\n[").replace("]", "]\n\n").replace(",", "\n").replace("{", "").replace("}", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(null);
        // Set Alert Title
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.alert_for_show_data, null);
        TextView tv_data = popupView.findViewById(R.id.tv_data);
        Button btn_back = popupView.findViewById(R.id.btn_back);
        tv_data.append(data_string);

        builder.setView(popupView);
        builder.setCancelable(true);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

        btn_back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                alertDialog.dismiss();
                return false;
            }
        });
    }

    public void alertForShowStringRaw(Context context, String data_string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(null);
        // Set Alert Title
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.alert_for_show_data, null);
        TextView tv_data = popupView.findViewById(R.id.tv_data);
        Button btn_back = popupView.findViewById(R.id.btn_back);
        tv_data.append(data_string);

        builder.setView(popupView);
        builder.setCancelable(true);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

        btn_back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                alertDialog.dismiss();
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createPIN(Context context, View view, UniMethods uniMethods) {
        /*TextInputLayout til_pin = view.findViewById(R.id.til_pin);
        TextInputLayout til_pin_confirm = view.findViewById(R.id.til_pin_confirm);
        Button btn = view.findViewById(R.id.btn_verify_pin);
        ProgressBar prog_pin = view.findViewById(R.id.prog_pin);

        disableButton(context, btn, R.color.new_grey_4);

        til_pin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_pin_confirm.setError(null);
                disableButton(context, btn, R.color.new_grey_4);
                if (til_pin.getEditText().getText().toString().length() == 4
                        && til_pin_confirm.getEditText().getText().toString().length() == 4) {
                    if (til_pin.getEditText().getText().toString().equals(til_pin_confirm.getEditText().getText().toString())) {
                        enableButton(context, btn, R.color.app_theme_color);
                    } else {
                        disableButton(context, btn, R.color.new_grey_4);
                        til_pin_confirm.setError("PIN should be same in both field, Please recheck and fill again");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        til_pin_confirm.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_pin_confirm.setError(null);
                disableButton(context, btn, R.color.new_grey_4);
                if (til_pin.getEditText().getText().toString().length() == 4
                        && til_pin_confirm.getEditText().getText().toString().length() == 4) {
                    if (til_pin.getEditText().getText().toString().equals(til_pin_confirm.getEditText().getText().toString())) {
                        enableButton(context, btn, R.color.app_theme_color);
                    } else {
                        disableButton(context, btn, R.color.new_grey_4);
                        til_pin_confirm.setError("PIN should be same in both field, Please recheck and fill again");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPress(btn, prog_pin);
                if (til_pin.getEditText().getText().toString().length() == 4
                        && til_pin_confirm.getEditText().getText().toString().length() == 4) {
                    if (til_pin.getEditText().getText().toString().equals(til_pin_confirm.getEditText().getText().toString())) {
                        String pin = til_pin_confirm.getEditText().getText().toString();
                        uniMethods.notifier.onTaskCompleted(pin, true, til_pin);
                    } else {
                        disableButton(context, btn, R.color.new_grey_4);
                        onRelease(btn, prog_pin);
                        til_pin_confirm.setError("PIN should be same in both field, Please recheck and fill again");
                    }
                } else {
                    onRelease(btn, prog_pin);
                }
            }
        });*/
    }

    public void manualPermission(Activity activity, FragmentManager fragmentManager, String head, String sub, int image) {
        ConfirmationBottomSheet confirmationBottomSheet = new ConfirmationBottomSheet(
                activity,
                image,
                head,
                sub,
                "Not now",
                "Open Settings",
                new ConfirmationBottomSheet.Notify() {
                    @Override
                    public void on_notify(boolean condition, String value) {
                        if (condition) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivityForResult(intent, 1000);
                        } else {
                        }
                    }
                });
        confirmationBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        confirmationBottomSheet.setCancelable(true);
        confirmationBottomSheet.show(fragmentManager, "create_group");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openDatePicker(Context context, MyMethods myMethods) {
        final_value = "";
        final String[] date = {"000000"};
        final String[] time = {"000000"};
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set the message show for the Alert time
        builder.setMessage(null);
        // Set Alert Title
        builder.setTitle(null);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.alert_date_picker, null);

        DatePicker date_picker = popupView.findViewById(R.id.date_picker);
        TimePicker time_picker = popupView.findViewById(R.id.time_picker);

        Button btn_cancel = popupView.findViewById(R.id.btn_cancel);
        Button btn_save = popupView.findViewById(R.id.btn_save_date);

        TextView tv_date = popupView.findViewById(R.id.tv_date);
        TextView tv_year = popupView.findViewById(R.id.tv_year);
        TextView tv_time = popupView.findViewById(R.id.tv_time);

        builder.setView(popupView);
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();

        final String[] m = {""};
        if (String.valueOf(date_picker.getMonth() + 1).length() > 1) {
            m[0] = String.valueOf(date_picker.getMonth() + 1);
        } else {
            m[0] = "0" + String.valueOf(date_picker.getMonth() + 1);
        }

        final String[] d = {""};
        if (String.valueOf(date_picker.getDayOfMonth()).length() > 1) {
            d[0] = String.valueOf(date_picker.getDayOfMonth());
        } else {
            d[0] = "0" + String.valueOf(date_picker.getDayOfMonth());
        }

        /*SimpleDateFormat formatter = new SimpleDateFormat("EEE");
        String text = formatter.format(cal.getTime());*/

        Calendar cal = Calendar.getInstance();
        //Toast.makeText(context, String.valueOf(cal.getTime()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, new SimpleDateFormat("EEE").format(cal.getTime()), Toast.LENGTH_SHORT).show();

        date[0] = String.valueOf(date_picker.getYear()).substring(2) + m[0] + d[0];
        date_picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                String m2 = "";

                //Toast.makeText(context, String.valueOf(i) + String.valueOf(i1) + String.valueOf(i2), Toast.LENGTH_SHORT).show();

                if (String.valueOf(i1 + 1).length() > 1) {
                    m2 = String.valueOf(i1 + 1);
                } else {
                    m2 = "0" + String.valueOf(i1 + 1);
                }
                String d2 = "";
                if (String.valueOf(i2).length() > 1) {
                    d2 = String.valueOf(i2);
                } else {
                    d2 = "0" + String.valueOf(i2);
                }
                date[0] = String.valueOf(i).substring(2) + m2 + d2;
                final_value = date[0] + time[0];
                tv_year.setText(String.valueOf(i));
                tv_date.setText(uniMethods.getDayName(final_value) + ", " + uniMethods.getMonthName(final_value) + " " + String.valueOf(d2));
            }
        });

        final String[] h = {""};
        if (String.valueOf(time_picker.getHour()).length() > 1) {
            h[0] = String.valueOf(time_picker.getHour());
        } else {
            h[0] = "0" + String.valueOf(time_picker.getHour());
        }

        final String[] mi = {""};
        if (String.valueOf(time_picker.getMinute()).length() > 1) {
            mi[0] = String.valueOf(time_picker.getMinute());
        } else {
            mi[0] = "0" + String.valueOf(time_picker.getMinute());
        }

        time[0] = h[0] + mi[0] + "00";
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String h2 = "";
                if (String.valueOf(i).length() > 1) {
                    h2 = String.valueOf(i);
                } else {
                    h2 = "0" + String.valueOf(i);
                }
                String mi2 = "";
                if (String.valueOf(i1).length() > 1) {
                    mi2 = String.valueOf(i1);
                } else {
                    mi2 = "0" + String.valueOf(i1);
                }
                time[0] = h2 + mi2 + "00";
                final_value = date[0] + time[0];
                tv_time.setText(uniMethods.getAmPmTime(time[0]));
            }
        });

        final_value = date[0] + time[0];
        tv_time.setText(uniMethods.getAmPmTime(time[0]));
        tv_year.setText("20" + date[0].substring(0, 2));
        tv_date.setText(uniMethods.getDayName(final_value) + ", " + uniMethods.getMonthName(final_value) + " " + date[0].substring(4));

        String finalDate = date[0];
        String finalTime = time[0];
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(final_value).equals("")) {
                    alertDialog.dismiss();
                } else {
                    uniMethods.notifier.onTaskCompleted(final_value, true, null);
                    alertDialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openDatePickerInCurrentView(View view, UniMethods uniMethods) {
        final String[] date = {"000000"};
        final String[] time = {"000000"};

        DatePicker date_picker = view.findViewById(R.id.date_picker);
        TimePicker time_picker = view.findViewById(R.id.time_picker);

        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_save = view.findViewById(R.id.btn_save_date);

        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_year = view.findViewById(R.id.tv_year);
        TextView tv_time = view.findViewById(R.id.tv_time);

        final String[] m = {""};
        if (String.valueOf(date_picker.getMonth() + 1).length() > 1) {
            m[0] = String.valueOf(date_picker.getMonth() + 1);
        } else {
            m[0] = "0" + String.valueOf(date_picker.getMonth() + 1);
        }

        final String[] d = {""};
        if (String.valueOf(date_picker.getDayOfMonth()).length() > 1) {
            d[0] = String.valueOf(date_picker.getDayOfMonth());
        } else {
            d[0] = "0" + String.valueOf(date_picker.getDayOfMonth());
        }

        /*SimpleDateFormat formatter = new SimpleDateFormat("EEE");
        String text = formatter.format(cal.getTime());*/

        Calendar cal = Calendar.getInstance();
        //Toast.makeText(context, String.valueOf(cal.getTime()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, new SimpleDateFormat("EEE").format(cal.getTime()), Toast.LENGTH_SHORT).show();

        date[0] = String.valueOf(date_picker.getYear()).substring(2) + m[0] + d[0];
        date_picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                String m2 = "";

                //Toast.makeText(context, String.valueOf(i) + String.valueOf(i1) + String.valueOf(i2), Toast.LENGTH_SHORT).show();

                if (String.valueOf(i1 + 1).length() > 1) {
                    m2 = String.valueOf(i1 + 1);
                } else {
                    m2 = "0" + String.valueOf(i1 + 1);
                }
                String d2 = "";
                if (String.valueOf(i2).length() > 1) {
                    d2 = String.valueOf(i2);
                } else {
                    d2 = "0" + String.valueOf(i2);
                }
                date[0] = String.valueOf(i).substring(2) + m2 + d2;
                final_value = date[0] + time[0];
                tv_year.setText(String.valueOf(i));
                tv_date.setText(uniMethods.getDayName(final_value) + ", " + uniMethods.getMonthName(final_value) + " " + String.valueOf(d2));
            }
        });

        final String[] h = {""};
        if (String.valueOf(time_picker.getHour()).length() > 1) {
            h[0] = String.valueOf(time_picker.getHour());
        } else {
            h[0] = "0" + String.valueOf(time_picker.getHour());
        }

        final String[] mi = {""};
        if (String.valueOf(time_picker.getMinute()).length() > 1) {
            mi[0] = String.valueOf(time_picker.getMinute());
        } else {
            mi[0] = "0" + String.valueOf(time_picker.getMinute());
        }

        time[0] = h[0] + mi[0] + "00";
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String h2 = "";
                if (String.valueOf(i).length() > 1) {
                    h2 = String.valueOf(i);
                } else {
                    h2 = "0" + String.valueOf(i);
                }
                String mi2 = "";
                if (String.valueOf(i1).length() > 1) {
                    mi2 = String.valueOf(i1);
                } else {
                    mi2 = "0" + String.valueOf(i1);
                }
                time[0] = h2 + mi2 + "00";
                final_value = date[0] + time[0];
                tv_time.setText(uniMethods.getAmPmTime(time[0]));
            }
        });

        final_value = date[0] + time[0];
        tv_time.setText(uniMethods.getAmPmTime(time[0]));
        tv_year.setText("20" + date[0].substring(0, 2));
        tv_date.setText(uniMethods.getDayName(final_value) + ", " + uniMethods.getMonthName(final_value) + " " + date[0].substring(4));

        String finalDate = date[0];
        String finalTime = time[0];
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(final_value).equals("")) {
                    uniMethods.notifier.onTaskCompleted(final_value, false, null);
                } else {
                    uniMethods.notifier.onTaskCompleted(final_value, true, null);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_value = "";
                uniMethods.notifier.onTaskCompleted(final_value, false, null);
            }
        });
    }

    public void checkPin(Activity activity, FragmentManager fragmentManager
            , String pin, UniMethods uniMethods) {
        if (pin.length() < 5) {
            CheckPinBottomSheet checkPinBottomSheet = new CheckPinBottomSheet(activity, pin, "pin", new CheckPinBottomSheet.Notify() {
                @Override
                public void on_notify(boolean condition, String value) {
                    if (condition) {
                        uniMethods.notifier.onTaskCompleted(pin, condition, value);
                    } else {
                        if (value.equals("view_destroyed")) {
                        }
                    }

                }
            });
            checkPinBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
            checkPinBottomSheet.setCancelable(false);
            checkPinBottomSheet.show(fragmentManager, "check_pin");
        } else {
            CheckPinBottomSheet checkPinBottomSheet = new CheckPinBottomSheet(activity, pin, "pin", new CheckPinBottomSheet.Notify() {
                @Override
                public void on_notify(boolean condition, String value) {
                    if (condition) {
                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "not success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            checkPinBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
            checkPinBottomSheet.show(fragmentManager, "check_pin");
        }
    }

    public void showUpdatePopup(Activity activity, FragmentManager fragmentManager, Object val) {
        UpdateAvailableBottomSheet updateAvailableBottomSheet = new UpdateAvailableBottomSheet(activity, val);
        updateAvailableBottomSheet.setCancelable(false);
        updateAvailableBottomSheet.show(fragmentManager, "update_app");
    }

    public void openBottomSheetForFollowup(Activity activity, FragmentManager fragmentManager, HashMap<String, Object> hashMap, UniMethods uniMethods) {
        AddFollowupBottomSheet addFollowupBottomSheet = new AddFollowupBottomSheet(activity, new AddFollowupBottomSheet.Notify() {
            @Override
            public void on_notify(boolean condition, String value) {
                if (condition) {
                    openBottomSheetForFollowupEvent(activity, fragmentManager, value, hashMap, uniMethods);
                }
            }
        });
        addFollowupBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        addFollowupBottomSheet.setCancelable(true);
        addFollowupBottomSheet.show(fragmentManager, "add_followup");
    }

    public void openBottomSheetForFollowupEvent(Activity activity, FragmentManager fragmentManager, String val, HashMap<String, Object> hashMap, UniMethods uniMethods) {
        AddFollowupEventBottomSheet addFollowupEventBottomSheet = new AddFollowupEventBottomSheet(activity, val, hashMap, new AddFollowupEventBottomSheet.Notify() {
            @Override
            public void on_notify(boolean condition, String value, Object o) {
                if (condition) {
                    uniMethods.notifier.onTaskCompleted(value, true, o);
                }
            }
        });
        addFollowupEventBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        addFollowupEventBottomSheet.setCancelable(true);
        addFollowupEventBottomSheet.show(fragmentManager, value);
    }

    public void openBottomSheetForEditFollowupEvent(Activity activity, FragmentManager fragmentManager, String val, HashMap<String, Object> hashMap, UniMethods uniMethods) {
        AddFollowupEventBottomSheet addFollowupEventBottomSheet = new AddFollowupEventBottomSheet(activity, val, hashMap, new AddFollowupEventBottomSheet.Notify() {
            @Override
            public void on_notify(boolean condition, String value, Object o) {
                if (condition) {
                    uniMethods.notifier.onTaskCompleted(value, true, o);
                }
            }
        });
        addFollowupEventBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        addFollowupEventBottomSheet.setCancelable(true);
        addFollowupEventBottomSheet.show(fragmentManager, value);
    }

    public void onNewRegister(View view, String user_type, UniMethods uniMethods) {
        /*LottieAnimationView lottieComplete = view.findViewById(R.id.lottie_completed);
        lottieComplete.setMinAndMaxProgress(0.15f, 0.7f);
        lottieComplete.playAnimation();
        lottieComplete.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ImageView iv1 = view.findViewById(R.id.iv1);
                TextView tv = view.findViewById(R.id.tv);
                TextView tv2 = view.findViewById(R.id.tv2);
                TextView tv3 = view.findViewById(R.id.tv3);
                Button tv_login_now = view.findViewById(R.id.tv_login_now);

                if (user_type.equals("exists")) {
                    tv.setText("Welcome back !");
                    tv2.setText("You have successfully logged back in your account");
                    tv_login_now.setText("Next");
                } else if (user_type.equals("not_exists")) {
                    tv.setText("Great job !");
                    tv2.setText("You have successfully created your account");
                    tv_login_now.setText("Login now");
                } else {

                }

                moveView(lottieComplete, "translationY", -300f, 500);
                fadingView(tv_login_now, 0.0f, 1.0f, 800, null);
                fadingView(iv1, 0.0f, 1.0f, 800, null);
                fadingView(tv, 0.0f, 1.0f, 800, null);
                fadingView(tv2, 0.0f, 1.0f, 800, null);
                fadingView(tv3, 0.0f, 1.0f, 800, null);
                tv_login_now.setVisibility(View.VISIBLE);
                iv1.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                tv_login_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uniMethods.notifier.onTaskCompleted("none", true, "none");
                    }
                });
            }
        });*/
    }

    public void showTilesBottomSheetToSelect(Activity activity, FragmentManager fragmentManager, ArrayList<HashMap<String, Object>> views_tags, String head, UniMethods uniMethods) {
        SelectTilesBottomSheet selectTilesBottomSheet = new SelectTilesBottomSheet(activity, views_tags, head, new SelectTilesBottomSheet.Notify() {
            @Override
            public void on_notify(boolean condition, String value) {
                if (condition) {
                    uniMethods.notifier.onTaskCompleted("", true, value);
                } else {
                    if (value.equals("iv_cross")) {
                        uniMethods.notifier.onTaskCompleted("", false, value);
                    }
                }

            }
        });
        selectTilesBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        selectTilesBottomSheet.setCancelable(false);
        selectTilesBottomSheet.show(fragmentManager, "create_group");
    }

    public void showImageTextBottomSheet(Activity activity,
                                         int drawable,
                                         int drawable_color,
                                         int background,
                                         int bg_color,
                                         int padding,
                                         int size,
                                         String header,
                                         String sub_header,
                                         String text_on_button,
                                         FragmentManager fragmentManager,
                                         boolean setCancelable,
                                         UniMethods uniMethods) {
        ImageTextBottomSheet imageTextBottomSheet = new ImageTextBottomSheet(
                activity,
                drawable,
                drawable_color,
                background,
                bg_color,
                padding,
                size,
                header,
                sub_header,
                text_on_button,
                new ImageTextBottomSheet.Notify() {
                    @Override
                    public void on_notify(boolean condition, String perform_action) {
                        if (condition) {
                            uniMethods.notifier.onTaskCompleted(perform_action, true, perform_action);
                        }
                    }
                });
        imageTextBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        imageTextBottomSheet.setCancelable(setCancelable);
        imageTextBottomSheet.show(fragmentManager, "create_group");
    }

    public void showTextBottomSheet(Activity activity,
                                    int img_drawable,
                                    int size,
                                    int text_drawable,
                                    String drawable_location,
                                    String header,
                                    String sub_header,
                                    String text_on_button,
                                    FragmentManager fragmentManager,
                                    boolean setCancelable,
                                    UniMethods uniMethods) {
        ShowInfoBottomSheet showInfoBottomSheet = new ShowInfoBottomSheet(
                activity,
                img_drawable,
                size,
                text_drawable,
                drawable_location,
                header,
                sub_header,
                text_on_button, new ShowInfoBottomSheet.Notify() {
            @Override
            public void on_notify(boolean condition, String value) {
                if (condition) {
                    uniMethods.notifier.onTaskCompleted(value, true, value);
                }
            }
        });
        showInfoBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        showInfoBottomSheet.setCancelable(setCancelable);
        showInfoBottomSheet.show(fragmentManager, "create_group");
    }

    public void showLottieTextBottomSheet(Activity activity,
                                          int lottie_raw,
                                          float animation_start,
                                          float animation_end,
                                          int size,
                                          float scale,
                                          String header,
                                          String sub_header,
                                          String text_on_button,
                                          FragmentManager fragmentManager,
                                          boolean setCancelable,
                                          UniMethods uniMethods) {
        LottieTextBottomSheet lottieTextBottomSheet = new LottieTextBottomSheet(
                activity,
                lottie_raw,
                animation_start,
                animation_end,
                size,
                scale,
                header,
                sub_header,
                text_on_button,
                new LottieTextBottomSheet.Notify() {
                    @Override
                    public void on_notify(boolean condition, String perform_action) {
                        if (condition) {
                            uniMethods.notifier.onTaskCompleted(perform_action, true, perform_action);
                        }
                    }
                });
        lottieTextBottomSheet.setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
        lottieTextBottomSheet.setCancelable(setCancelable);
        lottieTextBottomSheet.show(fragmentManager, "create_group");
    }

    public void assignPropertyTypesForSale(Context context, MyMethods myMethods) {
        ArrayList<String> property_types_list = new ArrayList<>();
        property_types_list.add(context.getString(R.string.plot));
        property_types_list.add(context.getString(R.string.flat));
        property_types_list.add(context.getString(R.string.commercial_plot));
        property_types_list.add(context.getString(R.string.house));
        property_types_list.add(context.getString(R.string.villa));
        property_types_list.add(context.getString(R.string.row_house));
        property_types_list.add(context.getString(R.string.farm_house));
        property_types_list.add(context.getString(R.string.shop));
        property_types_list.add(context.getString(R.string.office));
        property_types_list.add(context.getString(R.string.land));
        myMethods.callReturn.onCallReturn(String.valueOf(property_types_list.size()), true, property_types_list);
    }

    public void assignPropertyTypesForRent(Context context, MyMethods myMethods) {
        ArrayList<String> property_types_list = new ArrayList<>();
        property_types_list.add(context.getString(R.string.flat));
        property_types_list.add(context.getString(R.string.house));
        property_types_list.add(context.getString(R.string.villa));
        property_types_list.add(context.getString(R.string.farm_house));
        property_types_list.add(context.getString(R.string.shop));
        property_types_list.add(context.getString(R.string.office));
        property_types_list.add(context.getString(R.string.land));
        myMethods.callReturn.onCallReturn(String.valueOf(property_types_list.size()), true, property_types_list);

    }

    public void addMessageViewInConstraintsLayout(Activity activity, int parent_ll_compat_id, int drawable, String heading, String body, String btn_string, int margin_left, int margin_top, int margin_right, int margin_bottom, MyMethods myMethods) {
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayoutCompat view_ll_show_message = (LinearLayoutCompat) inflater.inflate(R.layout.view_ll_show_message, null);
        params.setMargins(margin_left, margin_top, margin_right, margin_bottom);
        view_ll_show_message.setLayoutParams(params);
        ConstraintLayout ll_compat = activity.findViewById(parent_ll_compat_id);
        ImageView iv_icon = view_ll_show_message.findViewById(R.id.iv_icon);
        TextView tv_heading = view_ll_show_message.findViewById(R.id.tv_heading);
        TextView tv_body = view_ll_show_message.findViewById(R.id.tv_body);
        Button btn_action = view_ll_show_message.findViewById(R.id.btn_action);

        iv_icon.setImageDrawable(AppCompatResources.getDrawable(activity, drawable));
        tv_heading.setText(Html.fromHtml(heading));
        tv_body.setText(Html.fromHtml(body));
        btn_action.setText(btn_string);

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMethods.callReturn.onCallReturn("", true, view);
            }
        });

        ll_compat.addView(view_ll_show_message);
    }


    public ConstraintLayout addCategories(Activity activity, ArrayList<String> category_list, MyMethods myMethods) {
        int dp_40 = new UniMethods().getPxValueOfDp(activity, 40);
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout.LayoutParams param = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, dp_40);
        ConstraintLayout view_cons_categories = (ConstraintLayout) inflater.inflate(R.layout.view_cons_categories, null);

        TabLayout tabLayout = (TabLayout) view_cons_categories.findViewById(R.id.tab_layout);

        for (String category : category_list) {
            tabLayout.addTab(tabLayout.newTab().setText(category));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_START);

        final ViewPager viewPager = (ViewPager) view_cons_categories.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                myMethods.callReturn.onCallReturn(Objects.requireNonNull(tab.getText()).toString(), true, tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        view_cons_categories.setLayoutParams(param);


        return view_cons_categories;
    }

}
