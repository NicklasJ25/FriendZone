package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignupNameFragment extends Fragment implements View.OnClickListener
{
    private EditText firstnameEditText;
    private EditText lastnameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_name, container, false);

        firstnameEditText = view.findViewById(R.id.firstnameEditText);
        lastnameEditText = view.findViewById(R.id.lastnameEditText);
        Button nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (validateForm())
        {
            Bundle bundle = getArguments();
            bundle.putString("Firstname", firstnameEditText.getText().toString().trim());
            bundle.putString("Lastname", lastnameEditText.getText().toString());

            Fragment fragment = new SignupBirthdayFragment();
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
        if (firstnameEditText.getText().toString().equals(""))
        {
            firstnameEditText.setError(getString(R.string.firstnameEditTextError));
            validation = false;
        }
        if (lastnameEditText.getText().toString().equals(""))
        {
            lastnameEditText.setError(getString(R.string.lastnameEditTextError));
            validation = false;
        }
        return validation;
    }
}
