package com.example.grandcross;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Hero extends AppCompatActivity {

    private String login;
    private List<Ability> abilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.light));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.not_orange));
        }

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

    public void ExitToMain(View view) {
        finish();
    }
}