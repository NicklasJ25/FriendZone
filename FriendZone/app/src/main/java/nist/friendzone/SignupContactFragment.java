package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class SignupContactFragment extends Fragment implements View.OnClickListener
{
    private EditText emailEditText;
    private EditText phoneEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_contact, container, false);

        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        phoneEditText = (EditText) view.findViewById(R.id.phoneEditText);
        Button nextButton = (Button) view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (validateForm())
        {
            Bundle bundle = getArguments();
            bundle.putString("email", emailEditText.getText().toString().toLowerCase());
            bundle.putString("Phone", phoneEditText.getText().toString());

            Fragment fragment = new SignupPasswordFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }
    }

    private boolean validateForm()
    {
        boolean validation = true;
        if (emailEditText.getText().toString().equals(""))
        {
            emailEditText.setError(getString(R.string.emailEditTextError));
            validation = false;
        }
        if (!phoneEditText.getText().toString().equals(""))
        {
            Pattern pattern = Pattern.compile("^([0-9]){8}$");
            if (!pattern.matcher(phoneEditText.getText().toString()).matches())
            {
                phoneEditText.setError(getString(R.string.phoneEditTextError));
                validation = false;
            }
        }
        return validation;
    }
}
