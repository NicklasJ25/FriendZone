package nist.friendzone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import nist.friendzone.Login.EmailPassword;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{
    private TextView emailTextView;
    private EditText passwordTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        passwordTextView = (EditText) view.findViewById(R.id.passwordTextView);

        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.createUserButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        EmailPassword emailPassword = new EmailPassword(getActivity());
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        switch (v.getId())
        {
            case R.id.loginButton:
                emailPassword.LoginUser(email, password);
                break;
            case R.id.createUserButton:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, new SignUpFragment())
                        .commit();
                break;
        }
    }
}
