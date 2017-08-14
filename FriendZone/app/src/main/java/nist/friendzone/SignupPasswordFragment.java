package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignupPasswordFragment extends Fragment implements View.OnClickListener
{
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_password, container, false);

        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);
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
            bundle.putString("Password", passwordEditText.getText().toString());

            Fragment fragment = new SignupCreateFragment();
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
        if (passwordEditText.getText().toString().equals(""))
        {
            passwordEditText.setError(getString(R.string.passwordEditTextError));
            validation = false;
        }
        if (confirmPasswordEditText.getText().toString().equals(""))
        {
            confirmPasswordEditText.setError(getString(R.string.confirmPasswordEditTextError));
            validation = false;
        }
        if (validation && !passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
        {
            confirmPasswordEditText.setError(getString(R.string.passwordDoesNotMatch));
            validation = false;
        }
        return validation;
    }
}
