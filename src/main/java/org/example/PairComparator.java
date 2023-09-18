package org.example;


import javafx.util.Pair;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair<String, Double>> {
    @Override
    public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
        // Compare pairs based on the Double values in descending order
        // To sort in descending order, we reverse the order of comparison
        return Double.compare(p2.getValue(), p1.getValue());
    }
}
