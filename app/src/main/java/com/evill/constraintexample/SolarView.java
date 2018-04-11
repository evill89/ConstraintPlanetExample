package com.evill.constraintexample;

interface SolarView {
    void startPlanetsAnimation();

    void setPlanetInformation(String title, String information);

    void setPlanetIcon(String earth);

    void showInformation();

    void hideInformation();

    void stopPlanetsAnimation();
}
