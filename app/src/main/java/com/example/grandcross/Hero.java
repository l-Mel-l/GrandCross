package com.example.grandcross;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Hero extends AppCompatActivity {

    private String login;
    private List<Ability> abilities;
    private ConstraintLayout upgradeLayout;
    private ConstraintLayout probLayout;
    private ImageView lvlupButton, probButton;
    private TextView fromLevelText, toLevelText;
    private ImageView plusButton, minusButton,plusProbButton,minusProbButton;
    private boolean isFromLevelSelected = true; // Флажок для отслеживания выбранного TextView
    private Character.Attribute attribute;
    private String characterId;

    private int awakeningLevel = 0; // Начальный уровень пробуждения
    private int selectedStar = 0; // Выбранная звезда для второго этапа
    private boolean isFirstStage = true; // Флаг для проверки первого этапа
    Character character;
    int prob;

    TextView nameView;
    TextView attackView,attack;
    TextView defenseView, defense;
    TextView hpView, hp;
    TextView lvl, lvltext;
    TextView bkView, bk, txtView,textView2;
    ImageView ab_1, ab_2, ab_3,ab_4;
    GifImageView gifImageView;
    Button upgradeButton;
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
        upgradeButton = findViewById(R.id.upgrade_button);

        fromLevelText.setOnClickListener(v -> isFromLevelSelected = true);
        toLevelText.setOnClickListener(v -> isFromLevelSelected = false);

        plusButton.setOnClickListener(v -> adjustLevel(5));
        minusButton.setOnClickListener(v -> adjustLevel(-5));

        gifImageView = findViewById(R.id.HeroIll);// Убедитесь, что у вас есть ImageView в вашем activity_hero.xml

        nameView = findViewById(R.id.FullName);
        attackView = findViewById(R.id.ATKid);attack = findViewById(R.id.ATK);
        defenseView = findViewById(R.id.DEFid);defense = findViewById(R.id.DEF);
        hpView = findViewById(R.id.OZid);hp = findViewById(R.id.OZ);
        lvl = findViewById(R.id.LVLid);lvltext = findViewById(R.id.LVL);
        bkView = findViewById(R.id.BKid);bk = findViewById(R.id.BK);
        txtView = findViewById(R.id.txt);textView2 = findViewById(R.id.txt2);
        ab_1 = findViewById(R.id.ab_1);ab_2 = findViewById(R.id.ab_2);ab_3 = findViewById(R.id.ab_3);ab_4 = findViewById(R.id.ab_4);

        login = getIntent().getStringExtra("login");

        TextView loginText = findViewById(R.id.loginid);
        loginText.setText(login);
        TextView from = findViewById(R.id.from_level);
        character = (Character) getIntent().getSerializableExtra("character");

        if (character != null) {
            gifImageView.setImageResource(character.getDetailImage());
            lvl.setText(character.getLvl() + "");
            from.setText(lvl.getText());
            nameView.setText(character.getName().replace('`', '\n'));
            attackView.setText(String.valueOf(character.getAttack()));  // Преобразование числового значения в строку
            defenseView.setText(String.valueOf(character.getDefense()));  // Преобразование числового значения в строку
            hpView.setText(String.valueOf(character.getHp()));  // Преобразование числового значения в строку
            characterId = getIntent().getStringExtra("characterId");
            attribute = character.getAttribute();
            getAwakeningLevelFromDatabase();
            calculateAndDisplayBK();

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
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeCharacter(toLevelText, lvl);
            }
        });

        // Обработка нажатия на кнопку lvlup
        lvlupButton = findViewById(R.id.lvlup);
        probButton = findViewById(R.id.prob);
        upgradeLayout = findViewById(R.id.upgrade_layout);
        probLayout = findViewById(R.id.prob_layout);

        lvlupButton.setOnClickListener(v -> toggleUpgradeLayout());
        probButton.setOnClickListener(v -> toggleProbLayout());
        ConstraintLayout mainLayout = findViewById(R.id.main);
        // Обработчики нажатий для пробуждения
        findViewById(R.id.minusProbBut).setOnClickListener(v -> adjustAwakeningLevel(-1));
        findViewById(R.id.plusProbBut).setOnClickListener(v -> adjustAwakeningLevel(1));
        findViewById(R.id.prob_button).setOnClickListener(v -> awakenCharacter());

        // Инициируем начальные данные
        updateAwakeningResources();
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
                    if (probLayout.getVisibility() == View.VISIBLE) {
                        int[] location = new int[2];
                        probLayout.getLocationOnScreen(location);
                        int x = location[0];
                        int y = location[1];
                        int width = probLayout.getWidth();
                        int height = probLayout.getHeight();

                        if (event.getX() < x || event.getX() > (x + width) || event.getY() < y || event.getY() > (y + height)) {
                            hideRequest();
                        }
                    }
                }
                return false;
            }
        });
    }
    private void calculateAndDisplayBK() {
        if (character != null) {
            double bk = 400 + character.getHp() * 0.2 +
                    character.getDefense() * 0.8 +
                    character.getAttack() * 1.0 +
                    character.getSopr() * 4.0 +
                    character.getDefCrit() * 4.0 +
                    character.getSoprCrit() * 4.0 +
                    character.getVost() * 2.5 +
                    character.getReg() * 5.0 +
                    character.getVamp() * 5.0 +
                    character.getCritdmg() * 2.5 +
                    character.getCritch() * 5.0 +
                    character.getPronz() * 5.0;

            // Отобразим рассчитанное значение БК
            bkView.setText(String.valueOf((int) bk));
        }
    }

    public void hideRequest() {
        if(upgradeLayout.getVisibility()==View.VISIBLE){
            hideUpgradeLayout();
        }
        if(probLayout.getVisibility()==View.VISIBLE){
            hideProbLayout();
        }
    }

    private void hideUpgradeLayout() {
        // Анимация для возврата кнопок lvlup и prob
        lvlupButton.animate().translationY(0).setDuration(500).start();
        probButton.animate().translationY(0).setDuration(500).start();
        attack.animate().translationX(0).setDuration(500).start();attackView.animate().translationX(0).setDuration(500).start();
        lvltext.animate().translationX(0).setDuration(500).start(); lvl.animate().translationX(0).setDuration(500).start();
        defense.animate().translationX(0).setDuration(500).start(); defenseView.animate().translationX(0).setDuration(500).start();
        hp.animate().translationX(0).setDuration(500).start();hpView.animate().translationX(0).setDuration(500).start();
        bk.animate().translationX(0).setDuration(500).start(); bkView.animate().translationX(0).setDuration(500).start();
        txtView.animate().translationX(0).setDuration(500).start(); textView2.animate().translationX(0).setDuration(500).start();
        ab_1.animate().translationX(0).setDuration(500).start(); ab_2.animate().translationX(0).setDuration(500).start(); ab_3.animate().translationX(0).setDuration(500).start(); ab_4.animate().translationX(0).setDuration(500).start();
        gifImageView.animate().translationX(0).setDuration(500);

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
    private void hideProbLayout() {
        // Анимация для возврата кнопок lvlup и prob
        lvlupButton.animate().translationY(0).setDuration(500).start();
        probButton.animate().translationY(0).setDuration(500).start();
        attack.animate().translationX(0).setDuration(500).start();attackView.animate().translationX(0).setDuration(500).start();
        lvltext.animate().translationX(0).setDuration(500).start(); lvl.animate().translationX(0).setDuration(500).start();
        defense.animate().translationX(0).setDuration(500).start(); defenseView.animate().translationX(0).setDuration(500).start();
        hp.animate().translationX(0).setDuration(500).start();hpView.animate().translationX(0).setDuration(500).start();
        bk.animate().translationX(0).setDuration(500).start(); bkView.animate().translationX(0).setDuration(500).start();
        txtView.animate().translationX(0).setDuration(500).start(); textView2.animate().translationX(0).setDuration(500).start();
        ab_1.animate().translationX(0).setDuration(500).start(); ab_2.animate().translationX(0).setDuration(500).start(); ab_3.animate().translationX(0).setDuration(500).start(); ab_4.animate().translationX(0).setDuration(500).start();
        gifImageView.animate().translationX(0).setDuration(500);

        // Анимация для скрытия панели вниз
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, probLayout.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);

        // Слушатель завершения анимации
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) probLayout.getLayoutParams();
                params.topMargin = 0; // Установите новое положение верхнего края
                probLayout.setLayoutParams(params);
                probLayout.setVisibility(View.GONE);
                probLayout.clearAnimation(); // Очистите анимацию, чтобы избежать конфликтов
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        probLayout.startAnimation(animate);
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

            attack.animate().translationX(-500).setDuration(500).start();attackView.animate().translationX(-500).setDuration(500).start();
            lvltext.animate().translationX(-500).setDuration(500).start(); lvl.animate().translationX(-500).setDuration(500).start();
            defense.animate().translationX(-500).setDuration(500).start(); defenseView.animate().translationX(-500).setDuration(500).start();
            hp.animate().translationX(-500).setDuration(500).start();hpView.animate().translationX(-500).setDuration(500).start();
            bk.animate().translationX(-500).setDuration(500).start(); bkView.animate().translationX(-500).setDuration(500).start();
            txtView.animate().translationX(-500).setDuration(500).start(); textView2.animate().translationX(-500).setDuration(500).start();
            ab_1.animate().translationX(-500).setDuration(500).start(); ab_2.animate().translationX(-500).setDuration(500).start(); ab_3.animate().translationX(-500).setDuration(500).start(); ab_4.animate().translationX(-500).setDuration(500).start();
            gifImageView.animate().translationX(-300).setDuration(500);
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
    private void toggleProbLayout() {
        if (probLayout.getVisibility() == View.GONE) {
            // Анимация для скрытия кнопок lvlup и prob
            lvlupButton.animate().translationY(lvlupButton.getHeight()).setDuration(500).start();
            probButton.animate().translationY(probButton.getHeight()).setDuration(500).start();

            attack.animate().translationX(-500).setDuration(500).start();
            attackView.animate().translationX(-500).setDuration(500).start();
            lvltext.animate().translationX(-500).setDuration(500).start();
            lvl.animate().translationX(-500).setDuration(500).start();
            defense.animate().translationX(-500).setDuration(500).start();
            defenseView.animate().translationX(-500).setDuration(500).start();
            hp.animate().translationX(-500).setDuration(500).start();
            hpView.animate().translationX(-500).setDuration(500).start();
            bk.animate().translationX(-500).setDuration(500).start();
            bkView.animate().translationX(-500).setDuration(500).start();
            txtView.animate().translationX(-500).setDuration(500).start();
            textView2.animate().translationX(-500).setDuration(500).start();
            ab_1.animate().translationX(-500).setDuration(500).start();
            ab_2.animate().translationX(-500).setDuration(500).start();
            ab_3.animate().translationX(-500).setDuration(500).start();
            ab_4.animate().translationX(-500).setDuration(500).start();
            gifImageView.animate().translationX(-300).setDuration(500);
            // Анимация для выезда снизу
            TranslateAnimation animate = new TranslateAnimation(0, 0, probLayout.getHeight(), -800);
            animate.setDuration(500);
            animate.setFillAfter(true);

            // Слушатель завершения анимации
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    probLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Изменение фактического положения представления
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) probLayout.getLayoutParams();
                    params.topMargin = -800; // Установите новое положение верхнего края
                    probLayout.setLayoutParams(params);
                    probLayout.clearAnimation(); // Очистите анимацию, чтобы избежать конфликтов
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            probLayout.startAnimation(animate);

        } else {
            hideProbLayout();
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

            // Пропускаем ресурсы, количество которых равно 0 или ресурс золота
            if (resourceQuantity == 0 || resourceName.equals("gold")) {
                continue;
            }

            LinearLayout resourceLayout = new LinearLayout(this);
            resourceLayout.setOrientation(LinearLayout.VERTICAL);
            resourceLayout.setPadding(8, 0, 8, 0); // Добавим немного отступов между ресурсами

            ImageView resourceImage = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageSize, imageSize);
            resourceImage.setLayoutParams(imageParams);
            resourceImage.setImageResource(getResourceDrawable(resourceName)); // Получите соответствующий drawable для ресурса
            resourceLayout.addView(resourceImage);

            TextView resourceText = new TextView(this);
            resourceText.setText(String.valueOf(resourceQuantity));
            resourceText.setGravity(Gravity.CENTER);
            resourceLayout.addView(resourceText);

            resourcesContainer.addView(resourceLayout);
        }

        int goldAmount = resources.get("gold");
        String formattedGoldAmount = NumberFormat.getNumberInstance(Locale.US).format(goldAmount);

        upgradeButton.setText(String.format("%s", formattedGoldAmount));
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

    private int getResourceDrawable(String resourceName) {
        switch (resourceName) {
            case "ssr":
                return R.drawable.ssr_pod;
            case "grimoire3":
                switch (attribute) {
                    case RED:
                        return R.drawable.red_3;
                    case BLUE:
                        return R.drawable.blue_3;
                    case GREEN:
                        return R.drawable.green_3;
                    case LIGHT:
                        return R.drawable.light_3;
                    case DARK:
                        return R.drawable.dark_3;
                    default:
                        return R.drawable.green_3;
                }
            case "grimoire4":
                switch (attribute) {
                    case RED:
                        return R.drawable.red_4;
                    case BLUE:
                        return R.drawable.blue_4;
                    case GREEN:
                        return R.drawable.green_4;
                    case LIGHT:
                        return R.drawable.light_4;
                    case DARK:
                        return R.drawable.dark_4;
                    default:
                        return R.drawable.green_4;
                }
            case "grimoire5":
                switch (attribute) {
                    case RED:
                        return R.drawable.red_5;
                    case BLUE:
                        return R.drawable.blue_5;
                    case GREEN:
                        return R.drawable.green_5;
                    case LIGHT:
                        return R.drawable.light_5;
                    case DARK:
                        return R.drawable.dark_5;
                    default:
                        return R.drawable.green_5;
                }
            case "grimoire6":
                switch (attribute) {
                    case RED:
                        return R.drawable.red_6;
                    case BLUE:
                        return R.drawable.blue_6;
                    case GREEN:
                        return R.drawable.green_6;
                    case LIGHT:
                        return R.drawable.light_6;
                    case DARK:
                        return R.drawable.dark_6;
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

    private void upgradeCharacter(TextView toLevelTextView, TextView currentLevelTextView) {
        int newLevel = Integer.parseInt(toLevelTextView.getText().toString());
        updateCharacterLevelInDatabase(newLevel);
        currentLevelTextView.setText(String.valueOf(newLevel));
    }

    private void updateCharacterLevelInDatabase(int newLevel) {
        DatabaseReference characterRef = FirebaseDatabase.getInstance().getReference("characters").child(characterId);

        characterRef.child("lvl").setValue(newLevel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Hero.this, "Уровень персонажа обновлен", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Hero.this, "Ошибка обновления уровня персонажа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void adjustAwakeningLevel(int delta) {
        if (isFirstStage) {
            int newLevel = awakeningLevel + delta;

            if (newLevel >= 0 && newLevel <= 1) { // Ограничение уровня пробуждения на первом этапе
                awakeningLevel = newLevel;
                updateAwakeningResources();
            }
        } else {
            int newSelectedStar = selectedStar + delta;

            if (newSelectedStar >= 0 && newSelectedStar <= 6) { // Ограничение уровня пробуждения на втором этапе
                selectedStar = newSelectedStar;
                updateAwakeningResources();
            }
        }
    }

    private void updateAwakeningResources() {
        FlexboxLayout starContainer = findViewById(R.id.star_container);
        starContainer.removeAllViews();

        for (int i = 0; i < 6; i++) {
            ImageView star = new ImageView(this);
            int starResource;

            if (isFirstStage) {
                starResource = (awakeningLevel == 1) ? R.drawable.star_ic : R.drawable.not_star;
            } else {
                if (i < selectedStar) {
                    starResource = R.drawable.ac_star_ic;
                } else {
                    starResource = R.drawable.star_ic;
                }
            }

            star.setImageResource(starResource);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
            params.setMargins(5, 0, 5, 0);
            star.setLayoutParams(params);
            starContainer.addView(star);
        }

        // Обновление ресурсов
        Map<String, Integer> resources = calculateAwakeningResources(awakeningLevel, awakeningLevel + selectedStar);
        FlexboxLayout resourcesContainer = findViewById(R.id.resources_prob_container);
        resourcesContainer.removeAllViews();

        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resourceName = entry.getKey();
            int resourceQuantity = entry.getValue();

            if(resourceQuantity != 0) {
                LinearLayout resourceLayout = new LinearLayout(this);
                resourceLayout.setOrientation(LinearLayout.VERTICAL);
                resourceLayout.setPadding(8, 0, 8, 0);

                ImageView resourceImage = new ImageView(this);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(170, 170);
                resourceImage.setLayoutParams(imageParams);
                resourceImage.setImageResource(getAwakeningResourceDrawable(resourceName));
                resourceLayout.addView(resourceImage);

                TextView resourceText = new TextView(this);
                resourceText.setText(String.valueOf(resourceQuantity));
                resourceText.setGravity(Gravity.CENTER);
                resourceLayout.addView(resourceText);

                resourcesContainer.addView(resourceLayout);
            }
        }
    }

    private Map<String, Integer> calculateAwakeningResources(int fromLevel, int toLevel) {
        Map<String, Integer> resources = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        int probCoin = 0;
        int supProbCoin = 0;

        for (int level = prob; level < toLevel; ) {
            if (isFirstStage) {
                if (level == 0) {
                    probCoin += 1;
                    level = 1;
                }
            } else {
                if (level < 1) {
                    supProbCoin += 3;
                    level = 1;
                } else if (level < 2) {
                    supProbCoin += 3;
                    level = 2;
                } else if (level < 3) {
                    supProbCoin += 6;
                    level = 3;
                } else if (level < 4) {
                    supProbCoin += 9;
                    level = 4;
                } else if (level < 5) {
                    supProbCoin += 12;
                    level = 5;
                } else if (level < 6) {
                    supProbCoin += 15;
                    level = 6;
                } else if (level < 7) {
                    supProbCoin += 18;
                    level = 7;
                } else {
                    break;
                }
            }
        }

        if (isFirstStage) {
            resources.put("prob_coin", probCoin);
        } else {
            resources.put("sup_prob_coin", supProbCoin);
        }

        return resources;
    }

    private void awakenCharacter() {
        if (isFirstStage) {
            if (awakeningLevel == 1) {
                isFirstStage = false;
                selectedStar = 0;
            }
            // Сохраняем уровень пробуждения в базу данных
            saveAwakeningLevelToDatabase(awakeningLevel);
        } else {
            // Сохраняем уровень пробуждения в базу данных
            saveAwakeningLevelToDatabase(awakeningLevel + selectedStar);
        }

        // Обновление отображения звёздочек
        updateAwakeningResources();
    }

    private void saveAwakeningLevelToDatabase(int level) {
        DatabaseReference characterRef = FirebaseDatabase.getInstance().getReference("characters").child(characterId);

        characterRef.child("prob").setValue(level)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Hero.this, "Пробуждение персонажа обновлено", Toast.LENGTH_SHORT).show();
                        getAwakeningLevelFromDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Hero.this, "Ошибка обновления пробуждения персонажа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int getAwakeningResourceDrawable(String resourceName) {
        switch (resourceName) {
            case "prob_coin":
                return R.drawable.prob_coin;
            case "sup_prob_coin":
                return R.drawable.sup_prob_coin;
            default:
                return R.drawable.prob_coin;
        }
    }
    private void getAwakeningLevelFromDatabase() {
        DatabaseReference characterRef = FirebaseDatabase.getInstance().getReference("characters").child(characterId);

        characterRef.child("prob").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    prob = snapshot.getValue(Integer.class);
                    updateAwakeningLevelFromDatabase(prob);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Hero.this, "Ошибка получения уровня пробуждения: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAwakeningLevelFromDatabase(int prob) {
        if (prob == 0) {
            isFirstStage = true;
            awakeningLevel = 0;
            selectedStar = 0;
        } else if (prob == 1) {
            isFirstStage = false;
            awakeningLevel = 1;
            selectedStar = 0;
        } else if (prob > 1) {
            isFirstStage = false;
            awakeningLevel = 1;
            selectedStar = prob - 1;
        } else {
            isFirstStage = true;
            awakeningLevel = 0;
            selectedStar = 0;
        }

        // Обновление отображения звёздочек
        updateAwakeningResources();
    }
    public void ExitToMain(View view) {
        finish();
    }
}