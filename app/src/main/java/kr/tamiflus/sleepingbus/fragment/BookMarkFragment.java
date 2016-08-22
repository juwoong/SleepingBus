package kr.tamiflus.sleepingbus.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.tamiflus.sleepingbus.HomeActivity;
import kr.tamiflus.sleepingbus.R;



public class BookMarkFragment extends BaseFullScreenFragment {
    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
        return fragment;
    }

    private View passView;

    public BookMarkFragment() {
        // Required empty public constructor
    }

    public View getPassView() {
        return passView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.theme);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_mark, container, false);

        passView = root.findViewById(R.id.boardingImage);


        return root;
    }
}
