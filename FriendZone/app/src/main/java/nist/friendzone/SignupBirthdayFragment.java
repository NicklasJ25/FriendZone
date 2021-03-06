package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class SignupBirthdayFragment extends Fragment implements View.OnClickListener
{
    DatePicker birthdayDatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_birthday, container, false);

        birthdayDatePicker = view.findViewById(R.id.birthdayDatePicker);
        Button nextButton = view.findViewById(R.id.nextButton);

        birthdayDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        int day = birthdayDatePicker.getDayOfMonth();
        int month = birthdayDatePicker.getMonth();
        int year = birthdayDatePicker.getYear();
        Calendar birthday = Calendar.getInstance();
        birthday.set(year, month, day);

        Bundle bundle = getArguments();
        bundle.putLong("Birthday", birthday.getTimeInMillis());

        Fragment fragment = new SignupAddressFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
