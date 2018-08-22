package co.helpdesk.faveo.frontend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;

public class CustomBottomSheetDialog extends DialogFragment {

    @BindView(R.id.add_button)
    Button addButton;
    @BindView(R.id.input_fname)
    AppCompatEditText fname;
    @BindView(R.id.input_lname)
    AppCompatEditText lname;
    @BindView(R.id.input_email)
    AppCompatEditText email;
    @BindView(R.id.spinner_code)
    Spinner phCode;
    @BindView(R.id.input_layout_email)
    TextInputLayout textInputLayoutEmail;
    @BindView(R.id.input_layout_fname)
    TextInputLayout textInputLayoutUsername;
    @BindView(R.id.input_layout_lname)
    TextInputLayout textInputLayoutPass;

    public static CustomBottomSheetDialog getInstance() {
        return new CustomBottomSheetDialog();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyMaterialTheme);
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_requester, container, false);
        ButterKnife.bind(this, view);
        phCode.setSelection(getCountryZipCode());
        fname.addTextChangedListener(mTextWatcher);
        lname.addTextChangedListener(mTextWatcher);
        email.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
        return view;
    }

    @OnClick(R.id.add_button)
    public void addRequester() {
        String countrycode = phCode.getSelectedItem().toString();
        String[] cc = countrycode.split(",");
        countrycode = cc[0];
        dismiss();
        Toast.makeText(getContext(), "Closed", Toast.LENGTH_SHORT).show();
    }

    void checkFieldsForEmptyValues() {

        String s1 = fname.getText().toString();
        String s2 = lname.getText().toString();
        String s3 = email.getText().toString();

        if (s1.equals("") || s2.equals("") || s3.equals("")) {
            addButton.setEnabled(false);
        } else if (!Helper.isValidEmail(s3)) {
            textInputLayoutEmail.setError(getString(R.string.enter_valid_email));
        } else {
            textInputLayoutEmail.setError(null);
            addButton.setEnabled(true);
        }
    }

    public int getCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";
        int code = 0;

        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.spinnerCountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                code = i;
                break;
            }
        }
        return code;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };
}
