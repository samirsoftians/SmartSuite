package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MeFragmentAddContacts extends Fragment {
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_FULL_NAME = "key_full_name";
    public static final String KEY_MOB_NO = "key_mob_no";
    public static final String KEY_FROM_FRAGMENT = "fromFragment";
    private MainActivity parentActivity;
    private MeRepoLogin repoLogin;

    public static MeFragmentAddContacts newInstance(String companyName, Integer customerCode, Integer salesCustomerCode, String fromFragment) {
        MeFragmentAddContacts meFragmentAddContacts = new MeFragmentAddContacts();
        Bundle args = new Bundle();
        args.putString(KEY_FROM_FRAGMENT, fromFragment);
        args.putString(KEY_FRAGMENT_NAME, "MeFragmentAddContacts");
        args.putInt(MeFragmentFollowUp.KEY_CUSTOMER_CODE, customerCode);
        args.putInt(MeFragmentFollowUp.KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putString(MeFragmentFollowUp.KEY_COMPANY_NAME, companyName);
        meFragmentAddContacts.setArguments(args);
        return meFragmentAddContacts;
    }

    public static MeFragmentAddContacts newInstance(String fullName, String mobNo, String fromFragment) {
        MeFragmentAddContacts meFragmentAddContacts = new MeFragmentAddContacts();
        Bundle args = new Bundle();
        Log.i("@", " Name in getInstance: " + fullName);
        Log.i("@", " mobno in getInstance: " + mobNo);
        args.putString(KEY_FROM_FRAGMENT, fromFragment);
        args.putString(KEY_FRAGMENT_NAME, "MeFragmentContacts");
        args.putString(KEY_FULL_NAME, fullName);
        args.putString(KEY_MOB_NO, mobNo);
        meFragmentAddContacts.setArguments(args);
        return meFragmentAddContacts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        repoLogin = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper());
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
        parentActivity.setTitle("Add Contact");
        parentActivity.clearListView();
        parentActivity.isToolbarClickable();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_contact_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        try {
            if (item.getItemId() == R.id.action_done) {
                if (areAllFilled(getView())) {
                    if (parentActivity.isNetworkAvailable()) {
                        Log.i("@Transworld", "Online mode");
                        new MeTaskSaveContact().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        try {
                            Log.i("@Transworld", "Offline mode");
                            addContactsLocal(makeRequestObject(getView()));
                            parentActivity.snack(getView(), "Contact Saved Locally");
                            if (!getArguments().get(KEY_FROM_FRAGMENT).equals("nextFragment")) {
                                parentActivity.popBackStack(2);  //if come from followUpNext fragment
                            } else {
                                parentActivity.popBackStack(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //separate validation
                    String emailId = ((EditText) (getView().findViewById(R.id.edtEmail))).getText().toString();
                    String emailId2 = ((EditText) (getView().findViewById(R.id.edtEmail2))).getText().toString();
                    String emailId3 = ((EditText) (getView().findViewById(R.id.edtEmail3))).getText().toString();
                    Log.i(MainActivity.TAG, "emailId -" + emailId);
                    Log.i(MainActivity.TAG, "emailId2 -" + emailId2);
                    Log.i(MainActivity.TAG, "emailId3 -" + emailId3);
                    if ((!isEmailIdValid(getView(), emailId) && !emailId.isEmpty()) || (isEmailIdValid(getView(), emailId2)) || (isEmailIdValid(getView(), emailId3))) {
                        parentActivity.snack(getView(), "Enter valid email id :(");
                    } else
                        parentActivity.snack(getView(), "Not a valid email id / mobile number:(");
                    String firstName = ((EditText) (getView().findViewById(R.id.edtFirstName))).getText().toString();
                    Log.i(MainActivity.TAG, "firstName - " + firstName);
                    if (firstName.isEmpty()) {
                        parentActivity.snack(getView(), "Enter First name :(");
                    } else {
                        String lastName = ((EditText) (getView().findViewById(R.id.edtLastName))).getText().toString();
                        Log.i(MainActivity.TAG, "lastName - " + lastName);
                        if (lastName.isEmpty()) {
                            parentActivity.snack(getView(), "Enter Last name :(");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addContactsLocal(MeAddContact meAddContact) {
        if (meAddContact != null) {
            MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
            try {
                repoContactDet.addContactsLocally(meAddContact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String fullName = getArguments().getString(KEY_FULL_NAME);
        String mobNo = getArguments().getString(KEY_MOB_NO);
        Log.i(MainActivity.TAG, "onCreateView Name : " + fullName);
        Log.i(MainActivity.TAG, "onCreateView Mob No : " + mobNo);
        Log.i("@Transworld", "From Frag " + getArguments().get(KEY_FROM_FRAGMENT));
        View rootView = inflater.inflate(R.layout.fragment_add_contacts, container, false);
      // setName(rootView);
        parentActivity.isToolbarClickable();
        initSpinner(rootView);
        initUI(rootView);

        setNameFromContacts(rootView);
        return rootView;
    }

    public void setNameFromContacts(final View rootView) {
        String fullName = getArguments().getString(KEY_FULL_NAME);
        String mobNo = getArguments().getString(KEY_MOB_NO);
        String name[];
        Log.i(MainActivity.TAG, "setNameFromContacts Name : " + fullName);
        Log.i(MainActivity.TAG, "setNameFromContacts Mob No : " + mobNo);
        if (fullName != null && mobNo != null) {
            if (fullName.contains(" ")) {
                name = fullName.split(" ");
                ((EditText) rootView.findViewById(R.id.edtName)).setText((fullName != null) ? fullName : "");
                ((EditText) rootView.findViewById(R.id.edtFirstName)).setText((name[0] != null) ? name[0] : "");
                ((EditText) rootView.findViewById(R.id.edtLastName)).setText((name[1] != null) ? name[1] : "");
                ((EditText) rootView.findViewById(R.id.edtMobileNo1)).setText((mobNo != null) ? mobNo : "");
            } else {
                ((EditText) rootView.findViewById(R.id.edtName)).setText((fullName != null) ? fullName : "");
                ((EditText) rootView.findViewById(R.id.edtFirstName)).setText(fullName);
                ((EditText) rootView.findViewById(R.id.edtMobileNo1)).setText((mobNo != null) ? mobNo : "");
            }
        }
    }

    private void initUI(final View rootView) {
        final Integer[] count = {0};
        final Integer[] countMob = {0};
        final Integer[] count1 = {0};


        rootView.findViewById(R.id.imageDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = ((EditText) rootView.findViewById(R.id.edtName)).getText().toString();
                String name1 = "";
                Log.i(MainActivity.TAG, " Name : " + fullName);
                name1 = fullName;
                if (name1.contains("Mr. ")) {
                    String[] removeInitials = name1.split("Mr. ");
                    Log.i(MainActivity.TAG, "removeInitials[1] - " + removeInitials[1]);
                    name1 = removeInitials[1].trim();
                    Log.i(MainActivity.TAG, " name1 : " + name1);
                }
                if (fullName.contains("Ms. ")) {
                    String[] removeInitials = name1.split("Ms. ");
                    Log.i(MainActivity.TAG, "removeInitials[1] - " + removeInitials[1]);
                    name1 = removeInitials[1].trim();
                    Log.i(MainActivity.TAG, " name1 : " + name1);
                }
                if (fullName.contains("Mrs. ")) {
                    String[] removeInitials = name1.split("Mrs. ");
                    Log.i(MainActivity.TAG, "removeInitials[1] - " + removeInitials[1]);
                    name1 = removeInitials[1].trim();
                    Log.i(MainActivity.TAG, " name1 : " + name1);
                }
                if (fullName.contains("Dr. ")) {
                    String[] removeInitials = name1.split("Dr. ");
                    Log.i(MainActivity.TAG, "removeInitials[1] - " + removeInitials[1]);
                    name1 = removeInitials[1].trim();
                    Log.i(MainActivity.TAG, " name1 : " + name1);
                }
                if (fullName.contains("M/s. ")) {
                    String[] removeInitials = name1.split("M/s. ");
                    Log.i(MainActivity.TAG, "removeInitials[1] - " + removeInitials[1]);
                    name1 = removeInitials[1].trim();
                    Log.i(MainActivity.TAG, " name1 : " + name1);
                }

                if (((EditText)rootView.findViewById(R.id.edtName)).getText().toString().equals("")){
                    ((EditText) rootView.findViewById(R.id.edtName)).setText("");
                    ((EditText) rootView.findViewById(R.id.edtFirstName)).setText("");
                    ((EditText) rootView.findViewById(R.id.edtLastName)).setText("");
                }

                (rootView.findViewById(R.id.edtName)).setVisibility(View.GONE);
                final RelativeLayout relativeLayoutSpinner = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinner);
                relativeLayoutSpinner.setVisibility(View.VISIBLE);
                ((Spinner) (rootView.findViewById(R.id.spinnerInitials))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                     //   ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                final LinearLayout linearLayoutName = (LinearLayout) rootView.findViewById(R.id.linearLayoutName);
                linearLayoutName.setVisibility(View.VISIBLE);
                Log.i(MainActivity.TAG, "name1 - " + name1);
                for (Integer i = 0; i < name1.length(); i++) {
                    Character str = name1.charAt(i);
                    Log.i(parentActivity.TAG, " str : " + str);
                    if (str.toString().equals(" ")) {
                        count1[0]++;
                    }
                }
                Log.i(MainActivity.TAG, " count out for : " + count1[0]);
                (rootView.findViewById(R.id.edtFirstName)).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtFirstName)), InputMethodManager.SHOW_IMPLICIT);
                 if((EditText) rootView.findViewById(R.id.edtName) != null){
                if (count1[0] < 1 && name1 != null) {
                    ((EditText) rootView.findViewById(R.id.edtFirstName)).setText(name1);
                }
                if (count1[0] == 1 && name1 != null) {
                    String name[] = name1.split(" ");
                    Log.i(MainActivity.TAG, " name[0] : " + name[0]);
                    Log.i(MainActivity.TAG, " count if(count==1): " + count1[0]);
                    ((EditText) rootView.findViewById(R.id.edtFirstName)).setText(name[0]);
                    ((EditText) rootView.findViewById(R.id.edtMiddleName)).setText("");
                    ((EditText) rootView.findViewById(R.id.edtLastName)).setText(name[1]);
                }
                if (count1[0] == 2 && name1 != null) {
                    Log.i(MainActivity.TAG, " fullName if(count==2): " + name1);
                    String name[] = name1.split(" ");
                    Log.i(MainActivity.TAG, " name[0] : " + name[0]);
                    Log.i(MainActivity.TAG, " name[1] : " + name[1]);
                    Log.i(MainActivity.TAG, " count if(count==2): " + count1[0]);
                    ((EditText) rootView.findViewById(R.id.edtFirstName)).setText(name[0]);
                    ((EditText) rootView.findViewById(R.id.edtMiddleName)).setText(name[1]);
                    ((EditText) rootView.findViewById(R.id.edtLastName)).setText(name[2]);
                }
            }

                    count1[0] = 0;
                    rootView.findViewById(R.id.imageUp).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageDown).setVisibility(View.GONE);
            }
        });
        rootView.findViewById(R.id.imageUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.edtName).setVisibility(View.VISIBLE);
                final RelativeLayout relativeLayoutSpinner = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinner);
                relativeLayoutSpinner.setVisibility(View.GONE);
                (rootView.findViewById(R.id.edtName)).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtName)), InputMethodManager.SHOW_IMPLICIT);
                final LinearLayout linearLayoutName = (LinearLayout) rootView.findViewById(R.id.linearLayoutName);
                linearLayoutName.setVisibility(View.GONE);
                rootView.findViewById(R.id.imageDown).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imageUp).setVisibility(View.GONE);
                if ((!(((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString().isEmpty()) || !(((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString().isEmpty()))) {
                    if (!(((EditText) rootView.findViewById(R.id.edtMiddleName)).getText().toString().isEmpty()) || !((rootView.findViewById(R.id.spinnerInitials)).toString().isEmpty())) {
                        String fullName = (((Spinner) (rootView.findViewById(R.id.spinnerInitials))).getSelectedItem().toString()) + " " + (((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString().trim()) + " " + (((EditText) rootView.findViewById(R.id.edtMiddleName)).getText().toString().trim()) + " " + (((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString());
                        Log.i(MainActivity.TAG, " Name : " + fullName);
                        ((EditText) rootView.findViewById(R.id.edtName)).setText(fullName.replaceAll("[ ]+", " "));
                    } else {
                        String fullName = (((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString()) + " " + (((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString());
                        Log.i(MainActivity.TAG, " Name : " + fullName);
                        ((EditText) rootView.findViewById(R.id.edtName)).setText(fullName.replaceAll("[ ]+", " "));
                    }
                } else {
                    ((EditText) (rootView.findViewById(R.id.edtName))).setText("");
                }
            }
        });
        rootView.findViewById(R.id.txtAddNewMob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (rootView.findViewById(R.id.edtMobileNo1)).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtMobileNo1)), InputMethodManager.SHOW_IMPLICIT);
                countMob[0]++;
                Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                if (countMob[0] == 1) {
                    final RelativeLayout relativeLayoutMob2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob2);
                    relativeLayoutMob2.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageClosedTagM2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutMob2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob2);
                            relativeLayoutMob2.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0 || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMob).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                if (countMob[0] == 2) {
                    final RelativeLayout relativeLayoutPhone = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutPhone);
                    relativeLayoutPhone.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageClosedTagPhone).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutPhone = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutPhone);
                            relativeLayoutPhone.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0 || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMob).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                if (countMob[0] == 3) {
                    rootView.findViewById(R.id.txtAddNewMob).setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutAltNo = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAltNo);
                    relativeLayoutAltNo.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageClosedTagAltNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutAltNo = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAltNo);
                            relativeLayoutAltNo.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0 || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMob).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
        rootView.findViewById(R.id.txtAddNewEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (rootView.findViewById(R.id.edtEmail)).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtEmail)), InputMethodManager.SHOW_IMPLICIT);
                count[0]++;
                Log.i(MainActivity.TAG, "count[0] - " + count[0]);
                if (count[0] == 1) {
                    final RelativeLayout relativeLayoutEmail2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutEmail2);
                    relativeLayoutEmail2.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageClosedTag2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutEmail2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutEmail2);
                            relativeLayoutEmail2.setVisibility(View.GONE);
                            count[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + count[0]);
                            if (count[0] == 0 || count[0] < 2) {
                                rootView.findViewById(R.id.txtAddNewEmail).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                if (count[0] == 2) {
                    rootView.findViewById(R.id.txtAddNewEmail).setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutEmail3 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutEmail3);
                    relativeLayoutEmail3.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.imageClosedTag3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutEmail3 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutEmail3);
                            relativeLayoutEmail3.setVisibility(View.GONE);
                            count[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + count[0]);
                            if (count[0] == 0 || count[0] < 2) {
                                rootView.findViewById(R.id.txtAddNewEmail).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
        rootView.findViewById(R.id.imageDownAddr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.edtAddr).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtAddr)), InputMethodManager.SHOW_IMPLICIT);
                final LinearLayout linearLayoutAddr = (LinearLayout) rootView.findViewById(R.id.linearLayoutAddr);
                linearLayoutAddr.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imageUpAddr).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imageDownAddr).setVisibility(View.GONE);
            }
        });
        rootView.findViewById(R.id.imageUpAddr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayoutAddr = (LinearLayout) rootView.findViewById(R.id.linearLayoutAddr);
                linearLayoutAddr.setVisibility(View.GONE);
                rootView.findViewById(R.id.imageUpAddr).setVisibility(View.GONE);
                rootView.findViewById(R.id.imageDownAddr).setVisibility(View.VISIBLE);
            }
        });

        /*rootView.findViewById(R.id.imgAddContactFromAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cleanIt(rootView);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, INTENT_REQUEST_GET_CONTACT);
            }
        });*/
        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanIt(rootView);
                new MeFragmentBottomSheetDialog().show(parentActivity.getSupportFragmentManager(), "sample");
            }
        });
    }



    private void cleanIt(View rootView) {
        ((EditText) rootView.findViewById(R.id.edtName)).setText("");
        ((EditText) rootView.findViewById(R.id.edtFirstName)).setText("");
        ((EditText) rootView.findViewById(R.id.edtMiddleName)).setText("");
        ((EditText) rootView.findViewById(R.id.edtLastName)).setText("");
        ((EditText) rootView.findViewById(R.id.edtAddr)).setText("");
        ((EditText) rootView.findViewById(R.id.edtCity)).setText("");
        ((EditText) rootView.findViewById(R.id.edtCountry)).setText("");
        ((EditText) rootView.findViewById(R.id.edtState)).setText("");
        ((EditText) rootView.findViewById(R.id.edtDesignation)).setText("");
        ((EditText) rootView.findViewById(R.id.edtPinCode)).setText("");
        ((EditText) rootView.findViewById(R.id.edtMobileNo1)).setText("");
        ((EditText) rootView.findViewById(R.id.edtMobileNo2)).setText("");
        ((EditText) rootView.findViewById(R.id.edtPhone)).setText("");
        ((EditText) rootView.findViewById(R.id.edtAltNo)).setText("");
        ((EditText) rootView.findViewById(R.id.edtEmail)).setText("");
        ((EditText) rootView.findViewById(R.id.edtEmail2)).setText("");
        ((EditText) rootView.findViewById(R.id.edtEmail3)).setText("");
        ((EditText) rootView.findViewById(R.id.edtFaxNo)).setText("");
    }

    private final void initSpinner(View rootView) {
        Spinner spinnerInitials = (Spinner) rootView.findViewById(R.id.spinnerInitials);
        ArrayList<String> initialsList = new ArrayList<String>();
        initialsList.add("Mr.");
        initialsList.add("Ms.");
        initialsList.add("Mrs.");
        initialsList.add("Dr.");
        initialsList.add("M/s.");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, initialsList);
      //  arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInitials.setAdapter(arrayAdapter);
    }

    private final Boolean isMobileNumberValid(View rootView) {
        return ((EditText) rootView.findViewById(R.id.edtMobileNo1)).getText().toString().length() >= 10 && !(((EditText) rootView.findViewById(R.id.edtMobileNo1)).getText().toString().startsWith(" "));
    }

    private final Boolean isEmailIdValid(View rootView, String emailId) {
        if (((EditText) (rootView.findViewById(R.id.edtEmail))).getText().toString().startsWith(" ")) {
            ((EditText) (rootView.findViewById(R.id.edtEmail))).setText("");
        }
        String emailPattern = "(?:[a-z0-9'+_`-]+(?:\\.[a-z0-9'_`-])*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        String email = emailId;
        Log.i(MainActivity.TAG, "EmailId - " + emailId);
        if (email == null) {
            return false;
        } else {
            Log.i(MainActivity.TAG, "email -" + email.matches(emailPattern));
            return email.matches(emailPattern);
        }
    }

    private final Boolean isFirstNameFilled(View rootView) {
        Log.i("@Transworld", "" + ((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString().startsWith(" "));
        return ((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString().length() > 0 && !(((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString().contains(" "));
    }

    private final Boolean isLastNameFilled(View rootView) {
        return ((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString().length() > 0 && !(((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString().contains(" "));
    }

    private final Boolean areAllFilled(View rootView) {
        return areFirstLastNameFilled(rootView) && areEmailMobileFilled(rootView);
    }

    private final Boolean areFirstLastNameFilled(View rootView) {
        if (!isFirstNameFilled(rootView) && (getView()!=null)) {
            parentActivity.snack(getView(), "Please Enter first name :(");
        } else if (!isLastNameFilled(rootView) && (getView()!=null)) {
            parentActivity.snack(getView(), "Please Enter last name :(");
        } else {
            return isFirstNameFilled(rootView) && isLastNameFilled(rootView);
        }
        return false;
    }

    private final Boolean areEmailMobileFilled(View rootView) {
        return isEmailIdValid(rootView, ((EditText) rootView.findViewById(R.id.edtEmail)).getText().toString()) || isMobileNumberValid(rootView);
    }

    private final MeAddContact makeRequestObject(View rootView) throws Exception {
        MeAddContact makeRequestObject = new MeAddContact();
        makeRequestObject.setCustomerCode((MePrefs.getKeyCustomerCode(getContext()) != -1 ? MePrefs.getKeyCustomerCode(getContext()) : MePrefs.getKeyCustomerCodeTemp(getContext())));
        makeRequestObject.setSalesCustomerCode((MePrefs.getKeySaleCustCode(getContext()) != -1 ? MePrefs.getKeySaleCustCode(getContext()) : MePrefs.getKeySaleCustCodeTemp(getContext())));
        makeRequestObject.setCompanyMasterId(repoLogin.getCompanyMasterId());
        makeRequestObject.setCompanyName((MePrefs.getKeyCompanyName(getContext()) != "none" ? MePrefs.getKeyCompanyName(getContext()) : MePrefs.getKeyCompanyNameTemp(getContext())));
        makeRequestObject.setContactPerson((((Spinner) (rootView.findViewById(R.id.spinnerInitials))).getSelectedItem().toString()) + " " + (((EditText) rootView.findViewById(R.id.edtFirstName)).getText().toString()) + " " + (((EditText) rootView.findViewById(R.id.edtMiddleName)).getText().toString()) + " " + (((EditText) rootView.findViewById(R.id.edtLastName)).getText().toString()));
        makeRequestObject.setSalutation(((Spinner) (rootView.findViewById(R.id.spinnerInitials))).getSelectedItem().toString().length() > 0 ? ((Spinner) (rootView.findViewById(R.id.spinnerInitials))).getSelectedItem().toString() : "");
        makeRequestObject.setFirstName(((EditText) (rootView.findViewById(R.id.edtFirstName))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtFirstName))).getText().toString() : "");
        makeRequestObject.setMiddleName(((EditText) (rootView.findViewById(R.id.edtMiddleName))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMiddleName))).getText().toString() : "");
        makeRequestObject.setLastName(((EditText) (rootView.findViewById(R.id.edtLastName))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtLastName))).getText().toString() : "");
        makeRequestObject.setAddress(((EditText) (rootView.findViewById(R.id.edtAddr))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtAddr))).getText().toString() : "");
        makeRequestObject.setCity(((EditText) (rootView.findViewById(R.id.edtCity))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtCity))).getText().toString() : "");
        makeRequestObject.setZipcode(((EditText) (rootView.findViewById(R.id.edtPinCode))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtPinCode))).getText().toString() : "");
        makeRequestObject.setDesignation(((EditText) (rootView.findViewById(R.id.edtDesignation))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtDesignation))).getText().toString() : "");
        makeRequestObject.setMobileNo(((EditText) (rootView.findViewById(R.id.edtMobileNo1))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMobileNo1))).getText().toString() : "");
        makeRequestObject.setMobileno1(((EditText) (rootView.findViewById(R.id.edtMobileNo2))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMobileNo2))).getText().toString() : "");
        makeRequestObject.setEmail(((EditText) (rootView.findViewById(R.id.edtEmail))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtEmail))).getText().toString() : "");
        makeRequestObject.setPhone(((EditText) (rootView.findViewById(R.id.edtPhone))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtPhone))).getText().toString() : "");
        makeRequestObject.setFax(((EditText) (rootView.findViewById(R.id.edtFaxNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtFaxNo))).getText().toString() : "");
        makeRequestObject.setState(((EditText) (rootView.findViewById(R.id.edtState))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtState))).getText().toString() : "");
        makeRequestObject.setAlternetNo(((EditText) (rootView.findViewById(R.id.edtAltNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtAltNo))).getText().toString() : "");
        makeRequestObject.setEmail1(((EditText) (rootView.findViewById(R.id.edtEmail2))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtEmail2))).getText().toString() : "");
        makeRequestObject.setEmail2(((EditText) (rootView.findViewById(R.id.edtEmail3))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtEmail3))).getText().toString() : "");
        makeRequestObject.setCountry(((EditText) (rootView.findViewById(R.id.edtCountry))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtCountry))).getText().toString() : "");
        makeRequestObject.setEntryBy("ERP");
        Log.i(MainActivity.TAG, "MakeReqObj : " + makeRequestObject);
        return makeRequestObject;
    }

    private final class MeConnectorAddContact implements MeConnectable {
        public Map<String, Object> saveNewContact() throws Exception {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(makeRequestObject(getView())));
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MeIUrl.URL_ADD_CONTACT)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i(MainActivity.TAG, "Response Json Add Contact- " + responsJson);
            Log.i("@Transworld", MeIUrl.URL_ADD_CONTACT);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i(MainActivity.TAG, "Response Entity - " + mapEntity);
            String status = mapEntity.get("status").toString();
            Log.i(MainActivity.TAG, "Status :  " + status);
            if (status.equals("added contact successfully")) {
                Log.i(MainActivity.TAG, "Status 2:  " + status);
                Log.i(MainActivity.TAG, "Fragment followup loaded");
            }
            return mapEntity;
        }
    }

    private final class MeTaskSaveContact extends AsyncTask<Void, Void, Map<String, Object>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Contact", "saving contact");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            MeConnectorAddContact connector = new MeConnectorAddContact();
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.saveNewContact();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapEntity;
        }

        @Override
        protected void onPostExecute(Map<String, Object> mapEntity) {
            super.onPostExecute(mapEntity);
            progressDialog.dismiss();
        //    parentActivity.snack(getView(), "Contact Added Successfully !! :) ");
            if (mapEntity != null) {
                String status = (String) mapEntity.get("status");
                String message = (String) mapEntity.get("message");
                if (status.equals("success")) {
                    if (getView()!=null) {
                        parentActivity.snack(getView(), "" + message);
                    }
                    Log.i("@Transworld", "From Frag - " + getArguments().get(KEY_FROM_FRAGMENT));
                    if (!getArguments().get(KEY_FROM_FRAGMENT).equals("nextFragment")) {
                        parentActivity.popBackStack(2);  //if come from followUpNext fragment
                    } else {
                        parentActivity.popBackStack(1);
                    }
                } else {
                    if (getView()!=null) {
                        parentActivity.snack(getView(), message);
                    }
                }
            }
        }
    }
}
