package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.realm.Realm;
import nist.friendzone.Firebase.EmailPassword;
import nist.friendzone.Realm.User;

public class SignupCreateFragment extends Fragment implements View.OnClickListener
{
    Realm realm = Realm.getDefaultInstance();

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
            String firstname = getArguments().getString("firstname");
            String lastname = getArguments().getString("lastname");
            String birthday = getArguments().getString("BirthDay");
            String profilePicture = getArguments().getString("profilePicture");
            String email = getArguments().getString("email");
            String phone = getArguments().getString("Phone");
            String password = getArguments().getString("Password");

            User user = new User(email, firstname, lastname, birthday, phone, profilePicture);

            EmailPassword emailPassword = new EmailPassword(getActivity());
            emailPassword.CreateUser(user, password);
        }
    }
}
