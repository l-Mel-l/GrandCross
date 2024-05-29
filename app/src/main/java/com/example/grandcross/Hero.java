package com.example.grandcross;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Hero extends AppCompatActivity {

    private String login;
    private List<Ability> abilities;
    private ConstraintLayout upgradeLayout;
    private ImageView lvlupButton, probButton;
    private TextView fromLevelText, toLevelText;
    private ImageView plusButton, minusButton;
    private boolean isFromLevelSelected = true; // Флажок для отслеживания выбранного TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.light));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.not_orange));
        }

        fromLevelText = findViewById(R.id.from_level);
        toLevelText = findViewById(R.id.to_level);
        plusButton = findViewById(R.id.plusBut);
        minusButton = findViewById(R.id.minusBut);

        fromLevelText.setOnClickListener(v -> isFromLevelSelected = true);
        toLevelText.setOnClickListener(v -> isFromLevelSelected = false);

        plusButton.setOnClickListener(v -> adjustLevel(10));
        minusButton.setOnClickListener(v -> adjustLevel(-10));

        GifImageView gifImageView = findViewById(R.id.HeroIll); // Убедитесь, что у вас есть ImageView в вашем activity_hero.xml
        TextView nameView = findViewById(R.id.FullName);
        TextView attackView = findViewById(R.id.ATKid);
        TextView defenseView = findViewById(R.id.DEFid);
        TextView hpView = findViewById(R.id.OZid);
        login = getIntent().getStringExtra("login");
        TextView loginText = findViewById(R.id.loginid);
        loginText.setText(login);

        Character character = (Character) getIntent().getSerializableExtra("character");

        if (character != null) {
            gifImageView.setImageResource(character.getDetailImage());
            nameView.setText(character.getName().replace('`', '\n'));
            attackView.setText(String.valueOf(character.getAttack()));  // Преобразование числового значения в строку
            defenseView.setText(String.valueOf(character.getDefense()));  // Преобразование числового значения в строку
            hpView.setText(String.valueOf(character.getHp()));  // Преобразование числового значения в строку

            abilities = character.getAbilities();
            if (abilities != null && abilities.size() >= 2) {
                ImageView ability1 = findViewById(R.id.ab_1);
                ImageView ability2 = findViewById(R.id.ab_2);
                ImageView ability3 = findViewById(R.id.ab_3);
                ImageView ability4 = findViewById(R.id.ab_4);

                ability1.setImageResource(abilities.get(0).getIcon());
                ability2.setImageResource(abilities.get(1).getIcon());
                ability3.setImageResource(abilities.get(2).getIcon());
                ability4.setImageResource(abilities.get(3).getIcon());

                ability1.setOnClickListener(view -> showAbilityDialog(0));
                ability2.setOnClickListener(view -> showAbilityDialog(1));
                ability3.setOnClickListener(view -> showAbilityDialog(2));
                ability4.setOnClickListener(view -> showAbilityDialog(3));
            }
        } else {
            // Обработайте случай, когда данные о персонаже отсутствуют
        }

        // Обработка нажатия на кнопку lvlup
        lvlupButton = findViewById(R.id.lvlup);
        probButton = findViewById(R.id.prob);
        upgradeLayout = findViewById(R.id.upgrade_layout);

        lvlupButton.setOnClickListener(v -> toggleUpgradeLayout());
        ConstraintLayout mainLayout = findViewById(R.id.main);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (upgradeLayout.getVisibility() == View.VISIBLE) {
                        int[] location = new int[2];
                        upgradeLayout.getLocationOnScreen(location);
                        int x = location[0];
                        int y = location[1];
                        int width = upgradeLayout.getWidth();
                        int height = upgradeLayout.getHeight();

                        if (event.getX() < x || event.getX() > (x + width) || event.getY() < y || event.getY() > (y + height)) {
                            hideRequest();
                        }
                    }
                }
                return false;
            }
        });
    }

    public void hideRequest() {
        if(upgradeLayout.getVisibility()==View.VISIBLE){
            hideUpgradeLayout();
        }
    }

    private void hideUpgradeLayout() {
        // Анимация для возврата кнопок lvlup и prob
        lvlupButton.animate().translationY(0).setDuration(500).start();
        probButton.animate().translationY(0).setDuration(500).start();

        // Анимация для скрытия панели вниз
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, upgradeLayout.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);

        // Слушатель завершения анимации
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) upgradeLayout.getLayoutParams();
                params.topMargin = 0; // Установите новое положение верхнего края
                upgradeLayout.setLayoutParams(params);
                upgradeLayout.setVisibility(View.GONE);
                upgradeLayout.clearAnimation(); // Очистите анимацию, чтобы избежать конфликтов
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        upgradeLayout.startAnimation(animate);
    }

    private void showAbilityDialog(int index) {
        if (abilities != null && abilities.size() > index) {
            Ability ability = abilities.get(index);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ability, null);
            builder.setView(dialogView);

            ImageView abilityIcon = dialogView.findViewById(R.id.ability_icon);
            TextView abilityDescription = dialogView.findViewById(R.id.ability_description);

            abilityIcon.setImageResource(ability.getIcon());
            abilityDescription.setText(ability.getDescription().replace('`', '\n'));

            AlertDialog dialog = builder.create();
            dialog.show();

            dialogView.setOnClickListener(v -> dialog.dismiss());
            // Устанавливаем обработчик нажатий для TextView
            abilityDescription.setOnClickListener(v -> dialog.dismiss());
            // Устанавливаем обработчик нажатий для ImageView
            abilityIcon.setOnClickListener(v -> dialog.dismiss());
        }
    }

    private void toggleUpgradeLayout() {
        if (upgradeLayout.getVisibility() == View.GONE) {
            // Анимация для скрытия кнопок lvlup и prob
            lvlupButton.animate().translationY(lvlupButton.getHeight()).setDuration(500).start();
            probButton.animate().translationY(probButton.getHeight()).setDuration(500).start();

            // Анимация для выезда снизу
            TranslateAnimation animate = new TranslateAnimation(0, 0, upgradeLayout.getHeight(), -500);
            animate.setDuration(500);
            animate.setFillAfter(true);

            // Слушатель завершения анимации
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    upgradeLayout.setVisibility(View.VISIBLE);}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Изменение фактического положения представления
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) upgradeLayout.getLayoutParams();
                    params.topMargin = -500; // Установите новое положение верхнего края
                    upgradeLayout.setLayoutParams(params);
                    upgradeLayout.clearAnimation(); // Очистите анимацию, чтобы избежать конфликтов
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            upgradeLayout.startAnimation(animate);

        } else {
            hideUpgradeLayout();
        }
    }

    private void adjustLevel(int delta) {
        TextView selectedTextView = isFromLevelSelected ? fromLevelText : toLevelText;
        int currentValue = Integer.parseInt(selectedTextView.getText().toString());
        int newValue = currentValue + delta;

        if (newValue >= 0 && newValue <= 100) {
            selectedTextView.setText(String.valueOf(newValue));
        }
    }

    public void ExitToMain(View view) {
        finish();
    }
}