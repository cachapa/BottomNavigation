package net.cachapa.bottomnavigation;

import android.view.View;

class Circle {
    View view;
    int color;
    float progress;

    Circle(View view, int color, float progress) {
        this.view = view;
        this.color = color;
        this.progress = progress;
    }
}
