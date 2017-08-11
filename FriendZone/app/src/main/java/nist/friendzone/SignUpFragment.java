package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nist.friendzone.Login.EmailPassword;

public class SignUpFragment extends Fragment implements View.OnClickListener
{
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nameEditText;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        yearSpinner = (Spinner) view.findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);
        daySpinner = (Spinner) view.findViewById(R.id.daySpinner);

        SetDateSpinners();

        view.findViewById(R.id.signUpButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString()))
        {
            EmailPassword emailPassword = new EmailPassword(getActivity());
            emailPassword.CreateUser(emailEditText.getText().toString(), passwordEditText.getText().toString(), nameEditText.getText().toString());
            emailPassword.LoginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }
    }

    private void SetDateSpinners()
    {
        List<String> yearSpinnerArray =  new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i <= 99; i++)
        {
            yearSpinnerArray.add("" + (year - i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, yearSpinnerArray);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        List<String> monthSpinnerArray =  new ArrayList<>();
        for (int i = 1; i <= 12; i++)
        {
            monthSpinnerArray.add("" + i);
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, monthSpinnerArray);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        List<String> daySpinnerArray =  new ArrayList<>();
        for (int i = 1; i <= 31; i++)
        {
            daySpinnerArray.add("" + i);
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, daySpinnerArray);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
    }
}
