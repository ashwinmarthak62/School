package com.example.veraki.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.veraki.school.R;


/**
 *
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {
    Button mobile,telephone,email,website,locate,address;

    public ContactUsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mobile= (Button)rootView.findViewById(R.id.buttonCall);
        mobile.setOnClickListener(this);

        telephone = (Button)rootView.findViewById(R.id.buttonTel);
        telephone.setOnClickListener(this);

        email= (Button)rootView.findViewById(R.id.buttonEmail);
        email.setOnClickListener(this);

        website = (Button)rootView.findViewById(R.id.buttonWebsite);
        website.setOnClickListener(this);

        locate = (Button)rootView.findViewById(R.id.buttonLocation);
        locate.setOnClickListener(this);

        address = (Button)rootView.findViewById(R.id.buttonAddress);
        address.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCall:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0700281350"));
                startActivity(callIntent);
                break;
            case R.id.buttonTel:
                Intent tele = new Intent(Intent.ACTION_CALL);
                tele.setData(Uri.parse("tel:0205226753"));
                startActivity(tele);
                break;
            case R.id.buttonEmail:
                Intent inten = new Intent(Intent.ACTION_SEND);
                inten.setType("plain/text");
                inten.putExtra(Intent.EXTRA_EMAIL, new String[] { "sales@solken.co.ke" });
                inten.putExtra(Intent.EXTRA_SUBJECT, "School app messages");
                inten.putExtra(Intent.EXTRA_TEXT, "Dear sir/madam");
                startActivity(Intent.createChooser(inten, ""));
                break;
            case R.id.buttonWebsite:
                Uri uri = Uri.parse("http://http://www.schoolapp.co.ke/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.buttonLocation:
                Toast.makeText(getActivity(), "1st floor Plessy House, Nairobi", Toast.LENGTH_LONG).show();
                break;
            case R.id.buttonAddress:
                  Toast.makeText(getActivity(), "Solken solutions ltd, P.o box 202-00621", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

}
