package com.evill.constraintexample;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.evill.constraintexample.databinding.ActivitySolarBinding;

import java.util.concurrent.TimeUnit;

public class SolarActivity extends AppCompatActivity implements SolarView {

    private ActivitySolarBinding binding;
    private SolarPresenter presenter;

    private ValueAnimator earthAnimator;
    private ValueAnimator marsAnimator;

    private boolean isInfoShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_solar);


        presenter = new SolarPresenter(this);

        initViews();
        setListeners();


        presenter.start();

    }

    private void initViews() {
        earthAnimator = planetAnimation(binding.earth, TimeUnit.SECONDS.toMillis(2));
        marsAnimator = planetAnimation(binding.mars, TimeUnit.SECONDS.toMillis(5));
    }

    private void setListeners() {
        binding.sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlanetClicked("sun");
            }
        });

        binding.earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlanetClicked("earth");
            }
        });

        binding.mars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPlanetClicked("mars");
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCloseClicked();
            }
        });
    }

    @Override
    public void startPlanetsAnimation() {
        earthAnimator.start();
        marsAnimator.start();
    }

    @Override
    public void stopPlanetsAnimation() {
        earthAnimator.pause();
        marsAnimator.pause();
    }

    private void resumePlanetsAnimation() {
        earthAnimator.resume();
        marsAnimator.resume();
    }

    @Override
    public void setPlanetIcon(String icon) {
        switch (icon) {
            case "earth":
                binding.infoImage.setImageResource(R.drawable.earth);
                break;
            case "mars":
                binding.infoImage.setImageResource(R.drawable.mars);
                break;
            case "sun":
                binding.infoImage.setImageResource(R.drawable.sun);
                break;
        }
    }

    @Override
    public void showInformation() {
        if (isInfoShown) {
            return;
        }

        isInfoShown = true;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_solar_info);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.2f));
        transition.setDuration(700);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                stopPlanetsAnimation();
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                resumePlanetsAnimation();
                binding.closeButton.setVisibility(View.VISIBLE);
                binding.infoImage.setVisibility(View.VISIBLE);
                binding.infoTitle.setVisibility(View.VISIBLE);
                binding.infoText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(binding.constraintRoot, transition);
        constraintSet.applyTo(binding.constraintRoot);
    }

    @Override
    public void hideInformation() {
        if (!isInfoShown) {
            return;
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_solar);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.2f));
        transition.setDuration(700);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                stopPlanetsAnimation();
                binding.closeButton.setVisibility(View.GONE);
                binding.infoImage.setVisibility(View.GONE);
                binding.infoTitle.setVisibility(View.GONE);
                binding.infoText.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                isInfoShown = false;
                resumePlanetsAnimation();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(binding.constraintRoot, transition);
        constraintSet.applyTo(binding.constraintRoot);
    }

    @Override
    public void setPlanetInformation(String title, String information) {
        binding.infoTitle.setText(title);
        binding.infoText.setText(information);
    }

    private ValueAnimator planetAnimation(final ImageView planet, long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 359);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) planet.getLayoutParams();
                layoutParams.circleAngle = val;
                planet.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());

        return valueAnimator;
    }


}
