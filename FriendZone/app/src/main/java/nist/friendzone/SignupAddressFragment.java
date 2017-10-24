package nist.friendzone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SignupAddressFragment extends Fragment implements View.OnClickListener
{
    private EditText addressEditText;
    private EditText postalcodeEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_address, container, false);

        addressEditText = view.findViewById(R.id.addressEditText);
        postalcodeEditText = view.findViewById(R.id.postalcodeEditText);
        Button nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        Bundle bundle = getArguments();
        bundle.putString("Address", addressEditText.getText().toString().trim());
        bundle.putString("Postalcode", postalcodeEditText.getText().toString());

        Fragment fragment = new SignupPictureFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
