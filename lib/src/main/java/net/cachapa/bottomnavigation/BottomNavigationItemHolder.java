package net.cachapa.bottomnavigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BottomNavigationItemHolder {
    private static final float SELECTED_ALPHA = 1f;
    private static final float DESELECTED_ALPHA = 0.6f;

    private MenuItem item;

    private ViewGroup rootView;
    private ViewGroup container;
    private TextView titleView;
    private ImageView iconView;

    BottomNavigationItemHolder(ViewGroup parent, MenuItem item) {
        this.item = item;

        rootView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        container = (ViewGroup) rootView.findViewById(R.id.container);
        titleView = (TextView) rootView.findViewById(R.id.title);
        iconView = (ImageView) rootView.findViewById(R.id.icon);

        titleView.setText(item.getTitle());
        iconView.setImageDrawable(item.getIcon());
    }

    public MenuItem getIitem() {
        return item;
    }

    void setSelected(boolean selected) {
        titleView.setVisibility(selected ? VISIBLE : GONE);
        iconView.setAlpha(selected ? SELECTED_ALPHA : DESELECTED_ALPHA);
    }

    ViewGroup getRootView() {
        return rootView;
    }

    public ViewGroup getContainer() {
        return container;
    }
}
