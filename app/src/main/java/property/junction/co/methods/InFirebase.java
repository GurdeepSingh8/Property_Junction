package property.junction.co.methods;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import property.junction.co.BuildConfig;
import property.junction.co.R;

public class InFirebase {

    ResultFromFirebase firebaseResult;
    FirebaseFirestore db;
    DocumentReference doc_main, my_doc;
    Activity activity;
    UniMethods uniMethods = new UniMethods();

    public InFirebase(Activity activity, ResultFromFirebase firebaseResult) {
        this.firebaseResult = firebaseResult;
        this.activity = activity;
        db = FirebaseFirestore.getInstance();
        doc_main = db.collection(activity.getString(R.string.database)).document(activity.getString(R.string.database_id));
        my_doc = doc_main.collection(activity.getString(R.string.associates)).document(uniMethods.getMyId(activity));
    }

    public InFirebase(Activity m_activity) {
        this.activity = m_activity;
        db = FirebaseFirestore.getInstance();
        doc_main = db.collection(activity.getString(R.string.database)).document(activity.getString(R.string.database_id));
        my_doc = doc_main.collection(activity.getString(R.string.associates)).document(uniMethods.getMyId(activity));
    }

    public interface ResultFromFirebase {
        void onResultFromFirebase(String callback, boolean condition, Object value);
    }

    public void isDatabaseCreated(InFirebase inFirebase) {
        doc_main.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_exists), true, task);
                    } else {
                        inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_not_exists), false, task);
                    }
                } else {
                    isDatabaseCreated(inFirebase);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isDatabaseCreated(inFirebase);
            }
        });
    }

    public void isUpdateAvailable(InFirebase inFirebase) {
        doc_main.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int app_version_code = BuildConfig.VERSION_CODE;
                    String version_code = task.getResult().getString(activity.getString(R.string.version_code));
                    String update_link = task.getResult().getString(activity.getString(R.string.update_link));
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(activity.getString(R.string.version_code), version_code);
                    hashMap.put(activity.getString(R.string.update_link), update_link);
                    if (String.valueOf(app_version_code).equals(version_code)) {
                        inFirebase.firebaseResult.onResultFromFirebase(version_code, false, hashMap);
                    } else {
                        inFirebase.firebaseResult.onResultFromFirebase(version_code, true, hashMap);
                    }
                }
            }
        });
    }

    public void getSecurityLevel(InFirebase inFirebase) {
        my_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    inFirebase.firebaseResult.onResultFromFirebase(task.getResult().getString(activity.getString(R.string.security_level)), true, task);
                }
            }
        });
    }

    public void getCountryList(InFirebase inFirebase) {
        db.collection(activity.getString(R.string.data)).document(activity.getString(R.string.countries)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<HashMap<String, Object>> country_list = (ArrayList<HashMap<String, Object>>) task.getResult().get(activity.getString(R.string.counties_with_map_and_code));
                    inFirebase.firebaseResult.onResultFromFirebase("", true, country_list);
                }
            }
        });

    }

    public void sendOtp(Activity activity, String country_code, String number, InFirebase inFirebase) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country_code + number,
                0,
                TimeUnit.SECONDS,
                activity,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        inFirebase.firebaseResult.onResultFromFirebase(e.getMessage(), false, e);
                    }

                    @Override
                    public void onCodeSent(@NonNull String VerificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        inFirebase.firebaseResult.onResultFromFirebase(VerificationId, true, forceResendingToken);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }
                }
        );
    }

    public void otpVerification(String verificationId, String code, InFirebase inFirebase) {
        if (verificationId != null) {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                    verificationId,
                    code
            );
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                inFirebase.firebaseResult.onResultFromFirebase("null", true, null);
                            } else {
                                inFirebase.firebaseResult.onResultFromFirebase("null", false, task.getException());
                            }
                        }
                    });
        }
    }

    public void registerAssociate(String name, String country_code, String number, String pin, InFirebase inFirebase) {
        String m_country_code = country_code.substring(country_code.indexOf("+"));
        HashMap<String, Object> hashMap = uniMethods.getAssociateProfileMap(
                activity,
                name,
                m_country_code,
                number,
                null,
                pin,
                null,
                null,
                null,
                null,
                activity.getString(R.string.security_level_0));
        isAssociateExists(m_country_code, number, new InFirebase(activity, new ResultFromFirebase() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResultFromFirebase(String callback, boolean condition, Object existing_user_id) {
                if (callback.equals(activity.getString(R.string.cb_exists))) {
                    hashMap.put(activity.getString(R.string.last_login), uniMethods.getCurrentTime());
                    doc_main.collection(activity.getString(R.string.associates)).document(String.valueOf(existing_user_id))
                            .update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_name), name);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_number), number);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_country_code), m_country_code);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_user_id), String.valueOf(existing_user_id));
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_login_status), activity.getString(R.string.logged_in));
                                        inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_exists), true, hashMap);
                                    } else {

                                    }
                                }
                            });
                } else if (callback.equals(activity.getString(R.string.cb_not_exists))) {
                    hashMap.put(activity.getString(R.string.register_date), uniMethods.getCurrentTime());
                    doc_main.collection(activity.getString(R.string.associates)).add(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_name), name);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_number), number);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_country_code), m_country_code);
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_user_id), task.getResult().getId());
                                        uniMethods.setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_login_status), activity.getString(R.string.logged_in));
                                        inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_not_exists), true, hashMap);
                                    } else {

                                    }
                                }
                            });
                }
            }
        }));

    }

    public void isAssociateExists(String country_code, String associate_number, InFirebase inFirebase) {
        doc_main.collection(activity.getString(R.string.associates))
                .whereEqualTo(activity.getString(R.string.number), associate_number)
                .whereEqualTo(activity.getString(R.string.country_code), country_code)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() > 0) {
                                inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_exists), true, task.getResult().getDocuments().get(0).getId());
                            } else {
                                inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_not_exists), false, task);
                            }
                        } else {
                            inFirebase.firebaseResult.onResultFromFirebase(activity.getString(R.string.cb_failed), false, task.getException());
                        }
                    }
                });
    }

    public void getFollowUps(String user_id, InFirebase inFirebase) {
        ArrayList<HashMap<String, Object>> list_activity = new ArrayList<>();

        HashMap<String, Object> client_details_hm = new HashMap<>();
        client_details_hm.put(activity.getString(R.string.view), activity.getString(R.string.client_details));
        list_activity.add(client_details_hm);

        HashMap<String, Object> add_activity_hm = new HashMap<>();
        add_activity_hm.put(activity.getString(R.string.view), activity.getString(R.string.add_activity));
        list_activity.add(add_activity_hm);

        HashMap<String, Object> activity_hm = new HashMap<>();
        activity_hm.put(activity.getString(R.string.view), activity.getString(R.string.activity));
        list_activity.add(activity_hm);

        HashMap<String, Object> first_activity_hm = new HashMap<>();
        first_activity_hm.put(activity.getString(R.string.view), activity.getString(R.string.client_added));
        list_activity.add(first_activity_hm);


        inFirebase.firebaseResult.onResultFromFirebase(user_id, true, list_activity);

    }

    public void getClientList(InFirebase inFirebase) {
        doc_main.collection("Clients").whereEqualTo("associate_id", uniMethods.getMyId(activity))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            inFirebase.firebaseResult.onResultFromFirebase(String.valueOf(task.getResult().size()), true, task);
                        }
                    }
                });
    }

}