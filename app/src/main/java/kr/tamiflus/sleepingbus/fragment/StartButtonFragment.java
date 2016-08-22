package kr.tamiflus.sleepingbus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.tamiflus.sleepingbus.HomeActivity;
import kr.tamiflus.sleepingbus.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StartButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartButtonFragment extends BaseFullScreenFragment {
    public static StartButtonFragment newInstance() {
        StartButtonFragment fragment = new StartButtonFragment();
        return fragment;
    }

    private View startBtn;

    public StartButtonFragment() {
        // Required empty public constructor
    }

    public View getStartBtn() {
        return startBtn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.theme);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_start_button, container, false);

        startBtn = root.findViewById(R.id.startBussingButton);

        startBtn.setOnClickListener((v) -> {
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().getApplicationContext().startActivity(intent);
        });
        return root;
    }
}
