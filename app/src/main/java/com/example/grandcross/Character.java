package com.example.grandcross;

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
    private int attack;
    private int defense;
    private int hp;
    private int Pronz;
    private int sopr;
    private int Reg;
    private int Critch;
    private int Critdmg;
    private int soprCrit;
    private int defCrit;
    private int Vost;
    private int Vamp;


    private Attribute attribute;
    private String userId;
    private List<Ability> abilities;

    // Пустой конструктор для Firebase
    public Character() {
    }

    public Character(int listImage, int detailImage, String name, int lvl, int prob, int attack, int defense, int hp, int pronz, int sopr, int reg, int critch, int critdmg, int soprCrit, int defCrit, int vost, int vamp, Attribute attribute, String userId, List<Ability> abilities) {
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

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPronz() {
        return Pronz;
    }

    public void setPronz(int pronz) {
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

    public int getCritdmg() {
        return Critdmg;
    }

    public void setCritdmg(int critdmg) {
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
}