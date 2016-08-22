package kr.tamiflus.sleepingbus.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.tamiflus.sleepingbus.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends BaseFullScreenFragment{
    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    private View busView;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public View getBusView() {
        return busView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.theme);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_welcome, container, false);

        busView = root.findViewById(R.id.busImage);

        return root;
    }
}
