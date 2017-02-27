package net.cachapa.bottomnavigationdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.cachapa.bottomnavigation.BottomNavigationView;
import net.cachapa.bottomnavigationdemo.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnMenuItemClickListener {
    public static final String CURRENT_FRAGMENT = "current_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        binding.bottomNavigationView.setOnMenuItemClickListener(this);

        if (savedInstanceState == null) {
            String title = binding.bottomNavigationView.getSelectedItem().getTitle().toString();
            showFragment(PlaceholderFragment.create(title));
        }
    }

    @Override
    public void onMenuItemSelected(MenuItem item) {
        showFragment(PlaceholderFragment.create(item.getTitle().toString()));
    }

    @Override
    public void onMenuItemReselected() {
        PlaceholderFragment fragment = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
        fragment.onReselected();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out)
                .replace(R.id.container, fragment, CURRENT_FRAGMENT)
                .commit();
    }
}
