package net.cachapa.bottomnavigationdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.cachapa.bottomnavigationdemo.databinding.PlaceholderFragmentBinding;

public class PlaceholderFragment extends Fragment {
    public static Fragment create(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        
        Fragment fragment = new PlaceholderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PlaceholderFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.placeholder_fragment, container, false);
        
        String title = getArguments().getString("title");
        binding.textView.setText(title);
        
        return binding.getRoot();
    }
    
    public void onReselected() {
        Toast.makeText(getContext(), "Fragment reselected", Toast.LENGTH_SHORT).show();
    }
}
