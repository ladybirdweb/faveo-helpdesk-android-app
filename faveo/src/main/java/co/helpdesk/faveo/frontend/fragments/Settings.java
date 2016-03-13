package co.helpdesk.faveo.frontend.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.MainActivity;

public class Settings extends Fragment implements CompoundButton.OnCheckedChangeListener{
    SwitchCompat switchCompatStatus, switchCompatResponse, switchCompatCreated,
            switchCompatAssignedGroup, switchCompatAssignedMe;
    Boolean isCheckedStatus = false, isCheckedResponse = false, isCheckedCreated = false,
        isCheckedAssignedGroup = false, isCheckedAssignedMe = false;

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
            switchCompatStatus = (SwitchCompat) rootView.findViewById(R.id.switch_status);
            switchCompatResponse = (SwitchCompat) rootView.findViewById(R.id.switch_response);
            switchCompatCreated = (SwitchCompat) rootView.findViewById(R.id.switch_created);
            switchCompatAssignedGroup = (SwitchCompat) rootView.findViewById(R.id.switch_assigned_group);
            switchCompatAssignedMe = (SwitchCompat) rootView.findViewById(R.id.switch_assigned_me);
        }
        ((MainActivity) getActivity()).setActionBarTitle("Settings");
        return rootView;
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
            case R.id.switch_status:
                isCheckedStatus = switchCompatStatus.isChecked();
                break;
            case R.id.switch_response:
                isCheckedResponse = switchCompatResponse.isChecked();
                break;
            case R.id.switch_created:
                isCheckedCreated = switchCompatCreated.isChecked();
                break;
            case R.id.switch_assigned_group:
                isCheckedAssignedGroup = switchCompatAssignedGroup.isChecked();
                break;
            case R.id.switch_assigned_me:
                isCheckedAssignedMe = switchCompatAssignedMe.isChecked();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
