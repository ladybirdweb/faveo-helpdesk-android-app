package co.helpdesk.faveo.frontend.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.activities.SplashActivity;

public class Settings extends Fragment implements CompoundButton.OnCheckedChangeListener, CompoundButton.OnClickListener {
    SwitchCompat switchCompatStatus, switchCompatResponse, switchCompatCreated,
            switchCompatAssignedGroup, switchCompatAssignedMe, switchCompatCrashReports;
    public static boolean isCheckedStatus = false, isCheckedResponse = false, isCheckedCreated = false,
            isCheckedAssignedGroup = false, isCheckedAssignedMe = false, isCheckedCrashReports = true;

    View rootView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            //switchCompatStatus = (SwitchCompat) rootView.findViewById(R.id.switch_status);
            // switchCompatResponse = (SwitchCompat) rootView.findViewById(R.id.switch_response);
            // switchCompatCreated = (SwitchCompat) rootView.findViewById(R.id.switch_created);
            // switchCompatAssignedGroup = (SwitchCompat) rootView.findViewById(R.id.switch_assigned_group);
            // switchCompatAssignedMe = (SwitchCompat) rootView.findViewById(R.id.switch_assigned_me);
            switchCompatCrashReports = (SwitchCompat) rootView.findViewById(R.id.switch_crash_reports);
            switchCompatCrashReports.setChecked(Preference.isCrashReport());
            // switchCompatCrashReports.setOnCheckedChangeListener(this);
            switchCompatCrashReports.setOnClickListener(this);

        }
        ((MainActivity) getActivity()).setActionBarTitle("Settings");
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//            case R.id.switch_status:
//                isCheckedStatus = switchCompatStatus.isChecked();
//                break;
//            case R.id.switch_response:
//                isCheckedResponse = switchCompatResponse.isChecked();
//                break;
//            case R.id.switch_created:
//                isCheckedCreated = switchCompatCreated.isChecked();
//                break;
//            case R.id.switch_assigned_group:
//                isCheckedAssignedGroup = switchCompatAssignedGroup.isChecked();
//                break;
//            case R.id.switch_assigned_me:
//                isCheckedAssignedMe = switchCompatAssignedMe.isChecked();
//                break;
            case R.id.switch_crash_reports: {

//                if (!switchCompatCrashReports.isChecked()) {
//                    isCheckedCrashReports = false;
//                    SharedPreferences.Editor authenticationEditor = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE, 0).edit();
//                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
//                    authenticationEditor.apply();
//
//                } else {
//                    isCheckedCrashReports = true;
//                    SharedPreferences.Editor authenticationEditor = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE, 0).edit();
//                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
//                    authenticationEditor.apply();
//                }

                break;
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.switch_crash_reports: {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Restart App ?")
                        .setMessage("To apply the changes in settings, you must restart the app!")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_warning_black_36dp)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switchCompatCrashReports.setChecked(Preference.isCrashReport());
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!switchCompatCrashReports.isChecked()) {
                                    isCheckedCrashReports = false;
                                    SharedPreferences.Editor authenticationEditor = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE, 0).edit();
                                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
                                    authenticationEditor.apply();

                                } else {
                                    isCheckedCrashReports = true;
                                    SharedPreferences.Editor authenticationEditor = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFERENCE, 0).edit();
                                    authenticationEditor.putBoolean("CRASH_REPORT", isCheckedCrashReports);
                                    authenticationEditor.apply();
                                }
                                Intent i = getContext().getPackageManager()
                                        .getLaunchIntentForPackage(getContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                Toast.makeText(getContext(), "Restarting...", Toast.LENGTH_LONG).show();
                            }
                        });

                builder.create();
                builder.show();

                break;
            }
        }

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
