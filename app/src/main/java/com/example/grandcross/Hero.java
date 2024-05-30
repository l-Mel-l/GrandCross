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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

    TextView nameView;
    TextView attackView,attack;
    TextView defenseView, defense;
    TextView hpView, hp;
    TextView lvl, lvltext;
    TextView bkView, bk;

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

        plusButton.setOnClickListener(v -> adjustLevel(5));
        minusButton.setOnClickListener(v -> adjustLevel(-5));

        GifImageView gifImageView = findViewById(R.id.HeroIll);// Убедитесь, что у вас есть ImageView в вашем activity_hero.xml

        nameView = findViewById(R.id.FullName);
        attackView = findViewById(R.id.ATKid);attack = findViewById(R.id.ATK);
        defenseView = findViewById(R.id.DEFid);defense = findViewById(R.id.DEF);
        hpView = findViewById(R.id.OZid);hp = findViewById(R.id.OZ);
        lvl = findViewById(R.id.LVLid);lvltext = findViewById(R.id.LVL);
        bkView = findViewById(R.id.BKid);bk = findViewById(R.id.BK);

        login = getIntent().getStringExtra("login");
        TextView loginText = findViewById(R.id.loginid);
        loginText.setText(login);
        TextView from = findViewById(R.id.from_level);
        from.setText(lvl.getText());
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

            attack.animate().translationX(-400).setDuration(500).start();attackView.animate().translationX(-400).setDuration(500).start();
            lvltext.animate().translationX(-400).setDuration(500).start(); lvl.animate().translationX(-400).setDuration(500).start();
            defense.animate().translationX(-400).setDuration(500).start(); defenseView.animate().translationX(-400).setDuration(500).start();
            hp.animate().translationX(-400).setDuration(500).start();hpView.animate().translationX(-400).setDuration(500).start();
            bk.animate().translationX(-400).setDuration(500).start(); bkView.animate().translationX(-400).setDuration(500).start();

            // Анимация для выезда снизу
            TranslateAnimation animate = new TranslateAnimation(0, 0, upgradeLayout.getHeight(), -800);
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
                    params.topMargin = -800; // Установите новое положение верхнего края
                    upgradeLayout.setLayoutParams(params);
                    upgradeLayout.clearAnimation(); // Очистите анимацию, чтобы избежать конфликтов
                    updateResources();
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
        updateResources();
    }

    private void updateResources() {
        int fromLevel = Integer.parseInt(((TextView) findViewById(R.id.from_level)).getText().toString());
        int toLevel = Integer.parseInt(((TextView) findViewById(R.id.to_level)).getText().toString());
        Character.Attribute attribute = getCharacterAttribute(); // Получите атрибут персонажа

        Map<String, Integer> resources = calculateResources(fromLevel, toLevel, attribute);

        FlexboxLayout resourcesContainer = findViewById(R.id.resources_container);
        resourcesContainer.removeAllViews();

        int visibleResourceCount = 0;
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            if (entry.getValue() > 0) {
                visibleResourceCount++;
            }
        }

        int imageSize;
        if (visibleResourceCount <= 5) {
            imageSize = (int) getResources().getDimension(R.dimen.resource_image_size_large); // 70dp
        } else if (visibleResourceCount <= 10) {
            imageSize = (int) getResources().getDimension(R.dimen.resource_image_size_medium); // 60dp
        } else {
            imageSize = (int) getResources().getDimension(R.dimen.resource_image_size_small); // 50dp
        }

        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resourceName = entry.getKey();
            int resourceQuantity = entry.getValue();

            // Пропускаем ресурсы, количество которых равно 0
            if (resourceQuantity == 0||resourceName == "gold")  {
                continue;
            }

            LinearLayout resourceLayout = new LinearLayout(this);
            resourceLayout.setOrientation(LinearLayout.VERTICAL);
            resourceLayout.setPadding(8, 0, 8, 0); // Добавим немного отступов между ресурсами

            ImageView resourceImage = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            resourceImage.setLayoutParams(imageParams);
            resourceImage.setImageResource(getResourceDrawable(resourceName, attribute)); // Получите соответствующий drawable для ресурса
            resourceLayout.addView(resourceImage);

            TextView resourceText = new TextView(this);
            resourceText.setText(String.valueOf(resourceQuantity));
            resourceText.setGravity(Gravity.CENTER);
            resourceLayout.addView(resourceText);

            resourcesContainer.addView(resourceLayout);
        }

        TextView goldAmount = findViewById(R.id.gold_amount);
        goldAmount.setText(String.format("%,d золота", resources.get("gold")));
    }

    private Map<String, Integer> calculateResources(int fromLevel, int toLevel, Character.Attribute attribute) {
        Map<String, Integer> resources = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        int ssrCount = 0;
        int grimoire3Count = 0;
        int grimoire4Count = 0;
        int grimoire5Count = 0;
        int grimoire6Count = 0;
        int urCount = 0;
        int hornsCount = 0;
        int wingsCount = 0;
        int earsCount = 0;
        int heartsCount = 0;
        int soulsCount = 0;
        int goldAmount = 0;

        for (int level = fromLevel; level < toLevel;) {
            if (level < 60) {
                ssrCount += 3;
                grimoire3Count += 3;
                grimoire4Count += 2;
                grimoire5Count += 1;
                goldAmount += 200000;
                level = 60;
            } else if (level < 65) {
                ssrCount += 3;
                grimoire6Count += 1;
                hornsCount += 20;
                goldAmount += 250000;
                level = 65;
            } else if (level < 70) {
                ssrCount += 3;
                grimoire6Count += 1;
                wingsCount += 20;
                hornsCount += 10;
                goldAmount += 300000;
                level = 70;
            } else if (level < 75) {
                ssrCount += 3;
                grimoire6Count += 1;
                earsCount += 20;
                hornsCount += 30;
                goldAmount += 350000;
                level = 75;
            } else if (level < 80) {
                ssrCount += 3;
                grimoire6Count += 1;
                earsCount += 30;
                wingsCount += 30;
                goldAmount += 400000;
                level = 80;
            } else if (level < 85) {
                ssrCount += 3;
                grimoire6Count += 1;
                heartsCount += 30;
                wingsCount += 30;
                goldAmount += 450000;
                level = 85;
            } else if (level < 90) {
                ssrCount += 3;
                grimoire6Count += 1;
                heartsCount += 40;
                earsCount += 30;
                goldAmount += 500000;
                level = 90;
            } else if (level < 95) {
                urCount += 1;
                grimoire6Count += 1;
                soulsCount += 50;
                heartsCount += 30;
                goldAmount += 750000;
                level = 95;
            } else {
                urCount += 1;
                grimoire6Count += 1;
                soulsCount += 75;
                heartsCount += 45;
                goldAmount += 1000000;
                level = 100;
            }
        }

        resources.put("ur", urCount);
        resources.put("ssr", ssrCount);
        resources.put("grimoire6", grimoire6Count);
        resources.put("grimoire5", grimoire5Count);
        resources.put("grimoire4", grimoire4Count);
        resources.put("grimoire3", grimoire3Count);
        resources.put("souls", soulsCount);
        resources.put("hearts", heartsCount);
        resources.put("ears", earsCount);
        resources.put("wings", wingsCount);
        resources.put("horns", hornsCount);
        resources.put("gold", goldAmount);

        return resources;
    }

    private int getResourceDrawable(String resourceName, Character.Attribute attribute) {
        switch (resourceName) {
            case "ssr":
                return R.drawable.ssr_pod;
            case "grimoire3":
                return R.drawable.green_3;
            case "grimoire4":
                return R.drawable.green_4;
            case "grimoire5":
                return R.drawable.green_5;
            case "grimoire6":
                switch (attribute) {
                    case RED:
                        return R.drawable.green_6;
                    case BLUE:
                        return R.drawable.green_6;
                    case GREEN:
                        return R.drawable.green_6;
                    case LIGHT:
                        return R.drawable.green_6;
                    case DARK:
                        return R.drawable.green_6;
                    default:
                        return R.drawable.green_6;
                }
            case "ur":
                return R.drawable.ur_pod;
            case "horns":
                return R.drawable.horn;
            case "wings":
                return R.drawable.wing;
            case "ears":
                return R.drawable.ear;
            case "hearts":
                return R.drawable.heart;
            case "souls":
                return R.drawable.soul;
            default:
                return R.drawable.horn;
        }
    }

    private Character.Attribute getCharacterAttribute() {
        // Реализуйте получение атрибута персонажа из вашей базы данных или текущего состояния
        return Character.Attribute.RED; // Пример, замените на фактическое значение
    }

    public void ExitToMain(View view) {
        finish();
    }
}