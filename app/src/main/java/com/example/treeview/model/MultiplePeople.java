package com.example.treeview.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiplePeople {

    private final List<People> mListPeople;

    public MultiplePeople() {
        mListPeople = new ArrayList<>();
    }

    public MultiplePeople(People... peoples) {
        this();
        mListPeople.addAll(Arrays.asList(peoples));
    }

    public boolean isEmpty(){
        return mListPeople.isEmpty();
    }

    public int size() {
        return mListPeople.size();
    }

    public People get(int index) {
        return mListPeople.get(index);
    }

    public List<People> getListPeople() {
        return mListPeople;
    }

    public People set(int index, People p) {
        return mListPeople.set(index, p);
    }

    public boolean addPeoples(List<People> list) {
        return mListPeople.addAll(list);
    }

    public boolean addPeople(People people) {
        return mListPeople.add(people);
    }
}
