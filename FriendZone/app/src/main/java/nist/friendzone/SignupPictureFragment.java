package nist.friendzone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;

public class SignupPictureFragment extends Fragment implements View.OnClickListener
{
    private ImageView profilePictureImageView;
    private Uri selectedImage;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_picture, container, false);

        profilePictureImageView = (ImageView) view.findViewById(R.id.profilePictureImageView);
        Button browseButton = (Button) view.findViewById(R.id.browseButton);
        Button nextButton = (Button) view.findViewById(R.id.nextButton);

        browseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.browseButton:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.nextButton:
                Bundle bundle = getArguments();
                if (selectedImage != null)
                {
                    bundle.putString("ProfilePicture", selectedImage.toString());
                }

                Fragment fragment = new SignupContactFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, fragment)
                        .commit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            selectedImage = data.getData();
            profilePictureImageView.setImageURI(selectedImage);
        }
    }
}
