package com.example.grandcross;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://grandcroos-7ee23-default-rtdb.firebaseio.com");

    EditText loginEditText, passwordEditText;
    Button registerBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginEditText = findViewById(R.id.LoginId);
        passwordEditText = findViewById(R.id.PasswordId);
        registerBut = findViewById(R.id.LoginButtonId);

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login, password;
                login = loginEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Введите Логин и Пароль", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean loginExists = false;
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String existingLogin = childSnapshot.child("login").getValue(String.class);
                                if (existingLogin != null && existingLogin.equals(login)) {
                                    loginExists = true;
                                    break;
                                }
                            }

                            if (loginExists) {
                                Toast.makeText(Registration.this, "Данный логин уже используется", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = databaseReference.child("users").push().getKey();
                                if (userId != null) {
                                    databaseReference.child("users").child(userId).child("login").setValue(login);
                                    databaseReference.child("users").child(userId).child("password").setValue(password);
                                    initializeCharactersForUser(userId);
                                    Toast.makeText(Registration.this, "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Registration.this, "Ошибка регистрации, попробуйте снова", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void initializeCharactersForUser(String userId) {
        DatabaseReference characterRef = databaseReference.child("characters");

        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p1, R.drawable.hi1, "«Материальный Клон»`Мелиодас Лоствейн",1,0, 560, 350, 7000,40,70,0,70,170,0,60,110,10,53.9394,31.3131,704.2424, Character.Attribute.RED, userId, AbilityUtils.createAbilities(new Ability(R.drawable.ab_1_1, "«Отдельное Воздействие»``Наносит урон от отрыва, равный 264% от атаки, против цели (один враг).``*Отрыв: 3-кратное увеличение шанса на критический удар."), new Ability(R.drawable.ab_1_2, "«Тройной Аватар»``Наносит урон от заряда, равный 130% от атаки, против цели (все враги). Заражает на 1 ход(ов).``*Заряд: игнорирует оборону. Заражение: ограничивает связанные с восстановлением характеристики."),new Ability(R.drawable.ab_1_3, " «Материальный Клон»``Увеличивает все характеристики героя на 20% на 2 хода. Затем наносит урон от тайной техники, равный 450% от атаки, против цели (все враги) и наносит урон с помощью преимущества по свойствам вне зависимости от свойства.``*Тайная техника: +80% дополнительного урона для каждого умения героя."),new Ability(R.drawable.pas_1_1, "Уступка``Увеличивает рейтинг всех умений героя и увеличивает оборону от критического удара на 30% и до 90%, когда герой получает критический урон во время вражеского хода. Дополнительно увеличивает связанные с атакой характеристики героя на 10% на 3 хода, когда уникальная способность героя увеличивает рейтинги умения.`(Может накапливаться до 5 раз. )"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p2, R.drawable.hi2, "«Королева Филориалов»`Фитория",1,0, 600, 320,6900,80,75,5,105,215,75,85,110,5,49.5252,37.3131,610.1515, Character.Attribute.BLUE, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_2_1, "Пронзающий Шторм``Уменьшает шанс на оборону от критического удара цели (один враг) на 40% на 1 ход. Затем наносит урон от отрыва, равный 200% от атаки.``* Отрыв: 3-кратное увеличение шанса на критический удар."), new Ability(R.drawable.ab_2_2, "Жестокая Буря``Наносит урон от Шторма, равный 100% от атаки, против цели (все враги).``* Шторм: игнорирует 30% от связанных с обороной характеристик.`Увеличение шанса на крит.Урон в 2 раза"),new Ability(R.drawable.ab_2_3, "Явление королевы``Даёт 2 эффекта Режущие когти герою, даёт Защиту королевы всем союзникам на 3 хода, затем наносит урон от шипа, равный 390% от атаки, против цели (все враги).``*Защита королевы:связанные с атакой характеристики +20%, связанные с обороной характеристики. + 20%``*Шип: 2-кратное увеличение крит.Урона."),new Ability(R.drawable.pas_2_1, "Правительница Филориалов``Увеличивает оборону от крит.Удара союзников на 60% на 1 ход в начале битвы. В начале хода союзников применяте власть королевы на союзников неизвестной рассы на 2 хода (не более 1 раза) и увеличивает все характеристики героя на 5% за каждого союзника неизвестной рассы участвующего в битве.`Кроме того, наносимый героем урон увеличивется на 50% при использовании Шторма. Когда союзник неизвестной рассы использует умение для нанесения урона противнику во время хода союзников, герой получает Удар королевы на 2 хода``*Власть королевы: снимает ослобление и увеличивает шанс на крит.Удар на 50%.`Увеличиваетурон, наносимый героем, на +0,3% за каждый 1% оставшихся ОЗ.``*Режущие когти: герой игнорирует 15% от сопротивления цели крит.Удару (Может накапливаться до 3 раз)``*Удар королевы: нанесённый урон +50%, шанс на крит.Удар +50%, степень пронзания +250%"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p3, R.drawable.hi3, "«Вассальное Оружие»`Глас",1,0, 530, 350, 6600,85,60,5,90,190,7,7,110,10,0,0,0, Character.Attribute.GREEN, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Рондо, Нулевая позиция: Обратный Цветок Снежной Луны``Наносит урон от Кражи души равный 190% от атаки, против цели (один враг).`*Кража души: увеличивает наносимый урон на +40% за каждый действующий на героя эффект «Сила души».`2-кратно увеличивает степень пронзания." ), new Ability(R.drawable.ab_3_2, "«Рондо, Пробивная Позиция: Пролом Панциря Черепахи»``Наносит урон от пронзания, равный 200% от атаки, против цели (один враг).`*Пронзание: 3-кратное увеличение степени пронзания."),new Ability(R.drawable.ab_3_3, "«Рондо Пустоты: Лунный Разрыв»``Увеличивает базовые характеристики всех союзников на 30% на 3 хода, наносит урон, равный 400% от атаки, против цели (все враги), а затем дает 1 эффект Сила души герою."),new Ability(R.drawable.pas_3_1, "Воин Вассального Веера``Увеличивает базовые характеристики героя на 7% за каждого участвующего в битве союзника неизвестной расы. Герой получает Силу души, когда союзник неизвестной расы использует умение. И напротив, 1 Сила души снимается, когда герой использует умение. Когда снимается Сила души, степень пронзания героя увеличивается на 30%, максимум до 90%. Когда эффектов Сила души накоплен максимум, эффекты снимаются и шанс на критический удар героя увеличивается на 30%, а степень пронзания возрастает на 150% на 3 хода.``*Сила души: связанные с обороной характеристики +10%. (Может накапливаться до 3 раз. )"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p4, R.drawable.hi4, "«Алмазная Защита»`Королева Элизабет",1,0, 550, 380, 7400,90,60,5,100,190,75,80,110,5,0,0,0, Character.Attribute.LIGHT, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_4_1, "«Изгнание»``Наносит урон от «Наказания», равный 200% ед. от атаки, против цели (один враг).``*Наказание: Увеличение степени проникновения в 3 раза.`+20% к наносимому урону на каждый действующий на себя эффект «Гнев Богини»."), new Ability(R.drawable.ab_4_2, "«Божественная Искра»``Наносит урон от «Ярости», равный 120% от атаки, против цели (все враги).``*Ярость: 1% дополнительного урона за каждый`процент оставшегося собственного 03.`+50% к наносимому урону, когда накоплено максимальное количество эффектов «Гнев Богини»."),new Ability(R.drawable.ab_4_3, "«Стена Света»``Увеличивает связанные с атакой характеристики героя на 10% на 2 хода, затем наносит урон, равный 500% от атаки, против цели (все враги), а потом применяет «Вторжение Света» на 2 хода.``Если герой участвует в битве и имеет 2 или более выживших союзников в конце хода союзников, то союзник с наименьшим количеством оставшихся 03 получает «Защиту Богини» на 1 ход. (Не более 2 ходов)``*Защита Богини: герой оживает с таким же количеством О3, какое было до гибели. (Не оживает, если гибнет от отраженного урона.)"),new Ability(R.drawable.pas_4_1, "Власть Королевы``В начале битвы накладывает на врагов эффект «Вторжение Света» на 2 хода. Увеличивает все характеристики героя на 7% за каждого применимого союзника, участвующего в битве. Кроме того, герой получает «Гнев Богини», когда применимый союзник использует умение атаки. Если герой использует умение атаки против одиночной цели с максимальным накопленным количеством эффектов «Гнев Богини», он отменяет стойки у цели и применяет оглушение на 1 ход. Затем все эффекты «Гнев Богини» снимаются.``*Вторжение Света: связанные с атакойхарактеристики: -10%`Убирает 1 сферу со шкалы суперприема при нанесении урона врагу, у которого 3 сферы и более.``*Применимые союзники: богини, люди, [Семь Смертных Грехов]``*Гнев Богини: связанные с атакой характеристики: +6% (может накапливаться до 4 раз)"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p5, R.drawable.hi5, "«Предвестник Океана»`Тармиэль из Пакта Света",1,0, 530, 420, 7600,50,40,5,70,165,50,50,110,5,0,0,0,Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_5_1, "«Пронзающая Струя»``Наносит урон от наводнения, равный 200% от атаки, против цели (один враг).``*Наводнение: 0.8% дополнительного урона за каждый процент оставшихся собственных 03."), new Ability(R.drawable.ab_5_2, "«Священный Свет»``На 1 ход(ов) увеличивает атаку цели (все союзники) на 15% и, если при атаке герой получает урон, применят эффект, снижающий наносимый урон у всех врагов на 5% на 1 ход.`(Может накапливаться до 3 раз.)"),new Ability(R.drawable.ab_5_3, "Океан``Наносит урон от детонации, равный 630% от атаки, против цели (все враги).``*Детонация: 20% дополнительного урона на каждую сферу шкалы суперприема цели."),new Ability(R.drawable.pas_5_1, "Объятия Океана``Базовые характеристики всех союзников вырастают на 6% каждый раз, когда герой использует Милость для восстановления О3.`(Не более 5 р.)"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p6, R.drawable.hi6, "Грех Гризли - Лень»`Король Фей Кинг",1,0, 560, 350, 7000, 0,0,0,0,0,0,0,0,0,0,0,0,Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p7, R.drawable.hi7, "«Грех Гризли - Лень»`Король Фей Кинг",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p8, R.drawable.hi8, "«Символ Единства»`Король Фей Арлекин",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p9, R.drawable.hi9, "«Посол Короля Демонов»`Палач Зелдрис",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p10, R.drawable.hi10, "«Грех Дракона - Гнев»`Демон Мелиодас",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p11, R.drawable.hi11, "«Избранный Король»`Артур с Экскалибуром",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p12, R.drawable.hi12, "«Алмазная Защита»`Королева Элизабет",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p13, R.drawable.hi13, "«Проклятые Оковы»`Мелиодас Из Частилища",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p14, R.drawable.hi14, "«Огонь Жизни»`Величайший Эсканор",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p15, R.drawable.hi15, "«Земные волны »`Королева Диана",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p16, R.drawable.hi16, "«Искажённый тьмой»`Берсерк Эстаросса",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p17, R.drawable.hi17, "«Правитель»`Тиранический Король Демонов",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p18, R.drawable.hi18, "«Семь Смертных Грехов»`Невероятный Бан",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p19, R.drawable.hi19, "«Материальный Клон»`Мелиодас Лоствейн",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.RED, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));
        characterRef.child(characterRef.push().getKey()).setValue(new Character(R.drawable.p20, R.drawable.hi20, "«Цельнометаллическая»`Одержимая Валенти",1,0, 560, 350, 7000,0,0,0,0,0,0,0,0,0,0,0,0, Character.Attribute.BLUE, userId,AbilityUtils.createAbilities(new Ability(R.drawable.ab_3_1, "Описание способности 1"), new Ability(R.drawable.ab_3_2, "Описание способности 2"),new Ability(R.drawable.ab_3_3, "Описание способности 3"),new Ability(R.drawable.pas_3_1, "Описание способности 4"))));

    }
}