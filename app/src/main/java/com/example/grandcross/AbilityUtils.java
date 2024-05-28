package com.example.grandcross;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbilityUtils {
    public static List<Ability> createAbilities(Ability... abilities) {
        return new ArrayList<>(Arrays.asList(abilities));
    }
}