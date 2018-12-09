package com.mustafa.shakir.happyhome;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class VoiceFrag extends Fragment implements View.OnClickListener {
    Button speak;

    public VoiceFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View someView = inflater.inflate(R.layout.fragment_voice, container, false);
        speak = (Button) someView.findViewById(R.id.voe);
        speak.setOnClickListener(this);
        return  someView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.voe){
            ((FridayMain)getActivity()).startVoiceRecognitionActivity();
        }

    }
}
