package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nist.friendzone.Firebase.EmailPassword;

public class SignupCreateFragment extends Fragment implements View.OnClickListener
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_create, container, false);

        Button createAccountButton = (Button) view.findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        String accountType = getArguments().getString("AccountType");
        if (accountType.equals("EmailPassword"))
        {
            String firstname = getArguments().getString("Firstname");
            String lastname = getArguments().getString("Lastname");
            int birthday = getArguments().getInt("Birthday");
            String email = getArguments().getString("Email");
            String phone = getArguments().getString("Phone");
            String password = getArguments().getString("Password");

            EmailPassword emailPassword = new EmailPassword(getActivity());
            emailPassword.CreateUser(email, password);
            emailPassword.UpdateUser("DisplayName", firstname + " " + lastname);
        }
    }
}