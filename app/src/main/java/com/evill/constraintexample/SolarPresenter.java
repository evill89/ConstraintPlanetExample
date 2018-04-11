package com.evill.constraintexample;

public class SolarPresenter {
    
    private SolarView view;

    public SolarPresenter(SolarView view) {
        this.view = view;
    }

    public void start() {
        view.startPlanetsAnimation();
    }

    public void onPlanetClicked(String planet) {
        view.setPlanetIcon(planet);
        switch (planet) {
            case "earth":
                view.setPlanetInformation("Earth", "Information about earth");
                break;
            case "mars":
                view.setPlanetInformation("Mars", "Information about mars");
                break;
            case "sun":
                view.setPlanetInformation("Sun", "Information about sun");
                break;
        }
        view.showInformation();
    }

    public void onCloseClicked() {
        view.hideInformation();
    }
}
