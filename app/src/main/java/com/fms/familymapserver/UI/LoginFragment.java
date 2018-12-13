package com.fms.familymapserver.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fms.familymapserver.Tasks.GetFamilyTreeTask;
import com.fms.familymapserver.R;
import com.fms.familymapserver.DataAccess.ServerProxy;
import com.fms.familymapserver.Tasks.LoginTask;
import com.fms.familymapserver.Tasks.RegisterTask;

import java.io.IOException;
import java.net.URL;

import Request.LoginRequest;
import Request.RegisterRequest;

public class LoginFragment extends Fragment {

    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mEditHost;
    private EditText mEditPort;
    private EditText mEditUsername;
    private EditText mEditPassword;
    private EditText mEditFirstName;
    private EditText mEditLastName;
    private EditText mEditEmail;
    private RadioGroup mGenderGroup;
    private String mHost = "";
    private String mPort = "";
    private String mUsername = "";
    private String mPassword = "";
    private String mFirstName = "";
    private String mLastName = "";
    private String mEmail = "";
    private String mGender = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginButton = v.findViewById(R.id.sign_in_button);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                mGender = (mGenderGroup.getCheckedRadioButtonId() == R.id.male_rb) ? "m" : "f";
                if(!mHost.equals("") && !mPort.equals("") && !mUsername.equals("")
                        && !mPassword.equals(""))
                {
                    new LoginTask(((Context)getActivity()))
                            .execute(mHost, mPort, mUsername, mPassword);
                }
                else
                {
                    Toast.makeText(getActivity(),"Missing parameters", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mRegisterButton = v.findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create async
                mGender = (mGenderGroup.getCheckedRadioButtonId() == R.id.male_rb) ? "m" : "f";
                if(!mHost.equals("") && !mPort.equals("") && !mUsername.equals("")
                        && !mPassword.equals("") && !mFirstName.equals("") && !mLastName.equals("")
                        && !mEmail.equals("") && !mGender.equals(""))
                {
                    new RegisterTask(((Context)getActivity()))
                            .execute(mHost,mPort,mUsername,mPassword,mEmail,
                                    mFirstName,mLastName,mGender);
                }
                else
                {
                    Toast.makeText(getActivity(),"Missing parameters", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mEditHost = v.findViewById(R.id.server_host);
        mEditHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHost = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditPort = v.findViewById(R.id.server_port);
        mEditPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPort = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditUsername = v.findViewById(R.id.username);
        mEditUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUsername = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditPassword = v.findViewById(R.id.password);
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditFirstName = v.findViewById(R.id.first_name);
        mEditFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFirstName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditLastName = v.findViewById(R.id.last_name);
        mEditLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLastName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditEmail = v.findViewById(R.id.email);
        mEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEmail = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mGenderGroup = v.findViewById(R.id.gender_group);

        return v;
    }
}
