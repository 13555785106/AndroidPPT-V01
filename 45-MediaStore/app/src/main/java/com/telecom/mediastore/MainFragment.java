package com.telecom.mediastore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    GridLayout mGridLayout;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mGridLayout = (GridLayout) v.findViewById(R.id.grid_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_01:
                        getActivity().startActivity(new Intent(getActivity(),ReadActivity.class));
                        break;
                    case R.id.button_02:
                        getActivity().startActivity(new Intent(getActivity(),StoreActivity.class));
                        break;
                }
            }
        };
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View child = mGridLayout.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(clickListener);
            }
        }

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
