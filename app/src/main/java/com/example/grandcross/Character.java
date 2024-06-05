package com.example.grandcross;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Character implements Serializable {
    public enum Attribute {
        ALL, RED, GREEN, BLUE, DARK, LIGHT
    }

    private int listImage;
    private int detailImage;
    private String name;
    private int lvl;
    private int prob;
    private double attack;
    private double defense;
    private double hp;
    private double Pronz;
    private int sopr;
    private int Reg;
    private int Critch;
    private double Critdmg;
    private int soprCrit;
    private int defCrit;
    private int Vost;
    private int Vamp;
    private double AtkBuf;
    private double DefBuf;
    private double HpBuf;


    private Attribute attribute;
    private String userId;
    private List<Ability> abilities;

    // Пустой конструктор для Firebase
    public Character() {
    }

    public Character(int listImage, int detailImage, String name, int lvl, int prob, double attack, double defense, double hp, double pronz, int sopr, int reg, int critch, double critdmg, int soprCrit, int defCrit, int vost, int vamp, double atkBuf, double defBuf, double hpBuf, Attribute attribute, String userId, List<Ability> abilities) {
        this.listImage = listImage;
        this.detailImage = detailImage;
        this.name = name;
        this.lvl = lvl;
        this.prob = prob;
        this.attack = attack;
        this.defense = defense;
        this.hp = hp;
        Pronz = pronz;
        this.sopr = sopr;
        Reg = reg;
        Critch = critch;
        Critdmg = critdmg;
        this.soprCrit = soprCrit;
        this.defCrit = defCrit;
        Vost = vost;
        Vamp = vamp;
        AtkBuf = atkBuf;
        DefBuf = defBuf;
        HpBuf = hpBuf;
        this.attribute = attribute;
        this.userId = userId;
        this.abilities = abilities;
    }

    public int getListImage() {
        return listImage;
    }

    public void setListImage(int listImage) {
        this.listImage = listImage;
    }

    public int getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(int detailImage) {
        this.detailImage = detailImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getProb() {
        return prob;
    }

    public void setProb(int prob) {
        this.prob = prob;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getPronz() {
        return Pronz;
    }

    public void setPronz(double pronz) {
        Pronz = pronz;
    }

    public int getSopr() {
        return sopr;
    }

    public void setSopr(int sopr) {
        this.sopr = sopr;
    }

    public int getReg() {
        return Reg;
    }

    public void setReg(int reg) {
        Reg = reg;
    }

    public int getCritch() {
        return Critch;
    }

    public void setCritch(int critch) {
        Critch = critch;
    }

    public double getCritdmg() {
        return Critdmg;
    }

    public void setCritdmg(double critdmg) {
        Critdmg = critdmg;
    }

    public int getSoprCrit() {
        return soprCrit;
    }

    public void setSoprCrit(int soprCrit) {
        this.soprCrit = soprCrit;
    }

    public int getDefCrit() {
        return defCrit;
    }

    public void setDefCrit(int defCrit) {
        this.defCrit = defCrit;
    }

    public int getVost() {
        return Vost;
    }

    public void setVost(int vost) {
        Vost = vost;
    }

    public int getVamp() {
        return Vamp;
    }

    public void setVamp(int vamp) {
        Vamp = vamp;
    }

    public double getAtkBuf() {
        return AtkBuf;
    }

    public void setAtkBuf(double atkBuf) {
        AtkBuf = atkBuf;
    }

    public double getDefBuf() {
        return DefBuf;
    }

    public void setDefBuf(double defBuf) {
        DefBuf = defBuf;
    }

    public double getHpBuf() {
        return HpBuf;
    }

    public void setHpBuf(double hpBuf) {
        HpBuf = hpBuf;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }
    public void calculateAndSaveEnhancedStats(int newLevel, DatabaseReference characterRef) {
        // Получаем текущий уровень
        int currentLevel = this.lvl;

        // Разница между новым уровнем и текущим
        int levelDifference = newLevel - currentLevel;

        // Рассчитываем новые значения характеристик

        this.attack = (this.attack + AtkBuf * levelDifference);
        this.defense = (this.defense + DefBuf * levelDifference);
        this.hp = (this.hp + HpBuf * levelDifference);

        // Обновляем значения в Firebase
        characterRef.child("attack").setValue(this.attack);
        characterRef.child("defense").setValue(this.defense);
        characterRef.child("hp").setValue(this.hp);
    }
}