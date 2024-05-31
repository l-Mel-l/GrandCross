package com.example.grandcross;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private int currentStar = 1;
    private final int maxStars = 6;
    private TextView[] stars;
    private ImageView[] characterViews;
    private List<Character> allCharacters = new ArrayList<>();
    private Map<String, Character> characterMap = new HashMap<>();
    private Map<Integer, Integer> imageViewResourceMap = new HashMap<>();

    private String userId, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getStringExtra("userId");
        login = getIntent().getStringExtra("login");
        TextView loginText = findViewById(R.id.loginid);
        loginText.setText(login);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.light));
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.not_orange));
        }

        initializeViews();
        initializeCharacters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeViews();
        initializeCharacters();
    }

    private void initializeCharacters() {
        DatabaseReference characterRef = FirebaseDatabase.getInstance().getReference("characters");
        characterRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allCharacters.clear(); // Очистите список перед добавлением новых данных
                characterMap.clear(); // Очистите Map перед добавлением новых данных
                for (DataSnapshot characterSnapshot : snapshot.getChildren()) {
                    String characterId = characterSnapshot.getKey(); // Получите первичный ключ
                    Character character = characterSnapshot.getValue(Character.class);
                    if (character != null) {
                        allCharacters.add(character); // Добавьте персонажа в список
                        characterMap.put(characterId, character); // Сохраните ключ и объект Character
                    }
                }
                updateStars();
                updateCharacterViewsByStar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Ошибка загрузки персонажей", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        stars = new TextView[]{findViewById(R.id.Prop1), findViewById(R.id.Prop2), findViewById(R.id.Prop3), findViewById(R.id.Prop4), findViewById(R.id.Prop5), findViewById(R.id.Prop6)};
        characterViews = new ImageView[]{findViewById(R.id.i1), findViewById(R.id.i2), findViewById(R.id.i3), findViewById(R.id.i4), findViewById(R.id.i5), findViewById(R.id.i6), findViewById(R.id.i7), findViewById(R.id.i8), findViewById(R.id.i9), findViewById(R.id.i10),
                findViewById(R.id.i11), findViewById(R.id.i12), findViewById(R.id.i13), findViewById(R.id.i14), findViewById(R.id.i15), findViewById(R.id.i16), findViewById(R.id.i17), findViewById(R.id.i18), findViewById(R.id.i19), findViewById(R.id.i20)};
    }

    public void setImageForView(ImageView imageView, int drawableId) {
        imageView.setImageResource(drawableId);
        imageViewResourceMap.put(imageView.getId(), drawableId);
    }

    public void openHero(View view) {
        if (imageViewResourceMap.containsKey(view.getId())) {
            int drawableId = imageViewResourceMap.get(view.getId());
            Character selectedCharacter = null;
            String selectedCharacterId = null;

            for (Map.Entry<String, Character> entry : characterMap.entrySet()) {
                if (entry.getValue().getListImage() == drawableId) {
                    selectedCharacter = entry.getValue();
                    selectedCharacterId = entry.getKey(); // Получите первичный ключ персонажа
                    break;
                }
            }

            if (selectedCharacter != null && selectedCharacterId != null) {
                Intent intent = new Intent(MainActivity.this, Hero.class);
                intent.putExtra("character", selectedCharacter).putExtra("login", login);
                intent.putExtra("characterId", selectedCharacterId); // Передаем первичный ключ персонажа
                startActivity(intent);
            }
        }
    }

    private void updateStars() {
        ViewGroup.MarginLayoutParams layoutParams;
        for (int i = 0; i < maxStars; i++) {
            if ((i + 1) == currentStar) {
                stars[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                layoutParams = (ViewGroup.MarginLayoutParams) stars[i].getLayoutParams();
                layoutParams.topMargin = 0;
                stars[i].setLayoutParams(layoutParams);
                stars[i].setTextColor(Color.parseColor("#FF3600"));
            } else {
                stars[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                layoutParams = (ViewGroup.MarginLayoutParams) stars[i].getLayoutParams();
                layoutParams.topMargin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
                stars[i].setLayoutParams(layoutParams);
                stars[i].setTextColor(Color.WHITE);
            }
        }
        updateAttributeTitle();
    }

    public void onNextStarClick(View view) {
        currentStar++;
        if (currentStar > maxStars) {
            currentStar = 1;
        }
        updateStars();
        updateCharacterViewsByStar();
    }

    public void onPreviousStarClick(View view) {
        currentStar--;
        if (currentStar < 1) {
            currentStar = maxStars;
        }
        updateStars();
        updateCharacterViewsByStar();
    }

    private void updateCharacterViewsByStar() {
        Character.Attribute attribute = Character.Attribute.ALL;
        switch (currentStar) {
            case 1:
                attribute = Character.Attribute.ALL;
                break;
            case 2:
                attribute = Character.Attribute.RED;
                break;
            case 3:
                attribute = Character.Attribute.GREEN;
                break;
            case 4:
                attribute = Character.Attribute.BLUE;
                break;
            case 5:
                attribute = Character.Attribute.DARK;
                break;
            case 6:
                attribute = Character.Attribute.LIGHT;
                break;
        }
        filterCharactersByAttribute(attribute);
    }

    private void filterCharactersByAttribute(Character.Attribute attribute) {
        List<Character> charactersToShow;

        if (attribute == Character.Attribute.ALL) {
            // Сохраняем исходный порядок
            charactersToShow = new ArrayList<>(allCharacters);
        } else {
            // Фильтруем персонажей по атрибуту
            charactersToShow = characterMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getAttribute() == attribute)
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }

        for (int i = 0; i < characterViews.length; i++) {
            if (i < charactersToShow.size()) {
                setImageForView(characterViews[i], charactersToShow.get(i).getListImage());
                characterViews[i].setVisibility(View.VISIBLE);
            } else {
                characterViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateAttributeTitle() {
        TextView attributesTitle = findViewById(R.id.SearchId);
        switch (currentStar) {
            case 1:
                attributesTitle.setText("Все Свойства");
                break;
            case 2:
                attributesTitle.setText("Свойство Сила");
                break;
            case 3:
                attributesTitle.setText("Свойство ОЗ");
                break;
            case 4:
                attributesTitle.setText("Свойство Скорость");
                break;
            case 5:
                attributesTitle.setText("Свойство Тьма");
                break;
            case 6:
                attributesTitle.setText("Свойство Свет");
                break;
        }
    }

    public void Exit(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}