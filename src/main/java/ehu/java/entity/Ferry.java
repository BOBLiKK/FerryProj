package ehu.java.entity;

public class Ferry {
    private final int maxWeight;
    private final int maxSpace;
    private int currentWeight = 0;
    private int currentSpace = 0;
    private int numberOfVehiclesOnBoard = 0;
    private FerryState currentState;

    public Ferry(int maxWeight, int maxSpace) {
        this.maxWeight = maxWeight;
        this.maxSpace = maxSpace;
        this.currentState = FerryState.WAITING;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getMaxSpace() {
        return maxSpace;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getCurrentSpace() {
        return currentSpace;
    }

    public FerryState getCurrentState() {
        return currentState;
    }

    public int getNumberOfVehiclesOnBoard() {
        return numberOfVehiclesOnBoard;
    }

    public void setCurrentState(FerryState currentState) {
        this.currentState = currentState;
    }

    public void increaseCurrentWeight(int addWeight) {
        this.currentWeight += addWeight;
    }

    public void increaseCurrentSpace(int addSpace) {
        this.currentSpace += addSpace;
    }

    public void incrementNumberOfVehiclesOnBoard() {
        this.numberOfVehiclesOnBoard++;
    }

    public void clearFerryStorage(){
        this.numberOfVehiclesOnBoard = 0;
        this.currentWeight = 0;
        this.currentSpace = 0;
    }
}