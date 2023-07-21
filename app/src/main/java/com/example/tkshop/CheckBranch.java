package com.example.tkshop;

public class CheckBranch {
    private int numberOne = 1;
    private int numberTwo = 2;

    public CheckBranch(int numberOne, int numberTwo) {
        this.numberOne = numberOne;
        this.numberTwo = numberTwo;
    }
    public int sum(int numberOne, int numberTwo){
        int sum = numberOne + numberTwo;
        return sum;
    }
}
