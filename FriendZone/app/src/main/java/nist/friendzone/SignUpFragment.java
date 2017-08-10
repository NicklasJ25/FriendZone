package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import nist.friendzone.Login.EmailPassword;

public class SignUpFragment extends Fragment implements View.OnClickListener
{
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);

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
}
