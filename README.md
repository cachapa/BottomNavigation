# BottomNavigationView

An implementation of the Material Design [Bottom Navigation Drawer](https://material.io/guidelines/components/bottom-navigation.html) for Android.

![simple](images/bottomnav.gif)

For sake of simplicity, this library only implements a limited subset of the features described in the guidelines. Specifically:
- Only the "shifting icons" with hidden labels mode is available
- Fixed icons mode, intended for 3 or less items, isn't implemented
- Tablet mode isn't implemented since it's hard to do in a single component without making a lot of assumptions
- Exact element padding and sizing may be slightly different from the guideline dimensions

The code is pretty compact since it offloads as much work as possible to the LinearLayout and Android Transitions API.

### Usage

Currently the library isn't published anywhere. You'll have to either import the project manually.

Add `BottomNavigationView` as to the bottom of your layout:

``` xml
<net.cachapa.bottomnavigation.BottomNavigationView
    android:id="@+id/bottom_navigation_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:fitsSystemWindows="true"
    app:bnv_colors="@array/menu_colors"
    app:bnv_menu="@menu/bottom_navigation" />
```

A full demo of the library is included with the project.

### License

    Copyright 2016 Daniel Cachapa.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

### Footnotes

Gifs were generated using the following script: https://gist.github.com/cachapa/aa829bfc717fc4f1d52c568d7ae8521e
