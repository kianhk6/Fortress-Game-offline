package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private Boolean isUserTurn;
    private int FortressHealth;
    private int numOfTanks;
    private Boolean isUserTheWinner;
    private Boolean isGameFinished;
    char[][] UIGrid = new char[10][10];
    char[][] cheatGrid = new char[10][10];
    Grid logicGrid;
    Random rand = new Random();
    char[] TankLetter = new char[10];
    List<Tank> tankArray = new ArrayList<>();
    public GameLogic(int numOfTanks, Grid grid){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                UIGrid[i][j] = '~';
                cheatGrid[i][j] = '.';
            }
        }
        this.numOfTanks = numOfTanks;
        logicGrid = grid;
        TankLetter[0] = 'A';
        TankLetter[1] = 'B';
        TankLetter[2] = 'C';
        TankLetter[3] = 'D';
        TankLetter[4] = 'E';
        TankLetter[5] = 'F';
        TankLetter[6] = 'G';
        TankLetter[7] = 'H';
        TankLetter[8] = 'I';
        TankLetter[9] = 'J';
    }

    private void updateGameboard(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(logicGrid.isCellTank(i,j)){
                    cheatGrid[i][j] = (char)(logicGrid.getCell(i, j).getWhichTank() +'0');
                }
            }
        }
    }
    private char getTankChar(Cell cell){
        cell.getWhichTank();
        return 'c';
//        return TankLetter[cell.getWhichTank()];
    }
    //Game Logic
    private ArrayList<Cell> getChildren(int x, int y){
        ArrayList<Cell> children = new ArrayList<>();
        if(x < 9 && !logicGrid.isCellTank(x+1, y)){
            children.add(logicGrid.getCell(x+1,y));
        }
        if( y < 9 && !logicGrid.isCellTank(x,y+1)){
            children.add(logicGrid.getCell(x, y+1));
        }
        if( x > 0 && !logicGrid.isCellTank(x-1, y)){
            children.add(logicGrid.getCell(x-1, y));
        }
        if(y > 0  &&  !logicGrid.isCellTank(x, y-1)){
            children.add(logicGrid.getCell(x, y-1));
        }
        return children;

    }
    public void generateTanks(){

        for(int i = 0; i < numOfTanks; i++){

            System.out.println(i);
            Tank tank = new Tank();
            int x = rand.nextInt() & Integer.MAX_VALUE %10;
            int y = rand.nextInt() & Integer.MAX_VALUE %10;
            ArrayList<Cell> Children = getChildren(x, y);

            //generate random cell till the starting point has the least one child or neighbour
            while (Children.size() == 0) {
                x = rand.nextInt()%10;
                y = rand.nextInt()%10;
                Children = getChildren(x, y);
            }

            //make it a tank
            logicGrid.setTank(x,y);
            tank.addCell(logicGrid.getCell(x, y));
            logicGrid.getCell(x, y).setWhichTank(i);
            Cell current = addFrom(logicGrid.getCell(x, y), tank, i);

            System.out.println(x + "," + y + " " +logicGrid.getCell(x, y).getIsTank());

            while(tank.tankCells.size() < 5){
                //adding children from this place
                ArrayList<Cell> nextChildChildren = getChildren(current.getX(), current.getY());
                if(nextChildChildren.size() != 0){
                    //current = current->next
                    current = addFrom(current, tank, i);
                }
                else{
                    current = current.getParent();
                    addFrom(current.getParent(), tank, i);
                }

            }
            tankArray.add(tank);

        }
        updateGameboard();


    }

//it over writes
    private Cell addFrom(Cell origin, Tank tank, int tankId) {
        //select which child
        ArrayList<Cell> Children = getChildren(origin.getX(), origin.getY());
        int randomChildIndex = rand.nextInt(Children.size());

        //make the cell to tank
        Cell child = Children.get(randomChildIndex);
        //while loop doesnt work
        while(child.getIsTank()){
            randomChildIndex = rand.nextInt(Children.size());
            child = Children.get(randomChildIndex);
            System.out.println("trying: " + child.getX() + "," +child.getY() + " " + child.getIsTank());

        }

        logicGrid.setTank(child.getX(), child.getY());
        child.setWhichTank(tankId);
        tank.addCell(child);
        child.setParent(origin);
        System.out.println(child.getX() + "," +child.getY() + " " + child.getIsTank());


        return child;


    }

    public Boolean getUserTurn() {
        return isUserTurn;
    }

    public void setUserTurn(Boolean userTurn) {
        isUserTurn = userTurn;
    }

    public int getFortressHealth() {
        return FortressHealth;
    }

    public void setFortressHealth(int fortressHealth) {
        FortressHealth = fortressHealth;
    }

    public int getNumOfTanks() {
        return numOfTanks;
    }

    public void setNumOfTanks(int numOfTanks) {
        this.numOfTanks = numOfTanks;
    }

    public Boolean getUserTheWinner() {
        return isUserTheWinner;
    }

    public void setUserTheWinner(Boolean userTheWinner) {
        isUserTheWinner = userTheWinner;
    }

    public Boolean getGameFinished() {
        return isGameFinished;
    }

    public void setGameFinished(Boolean gameFinished) {
        isGameFinished = gameFinished;
    }

    public char[][] getUIGrid() {
        return UIGrid;
    }

    public void setUIGrid(char[][] UIGrid) {
        this.UIGrid = UIGrid;
    }

    public char[][] getCheatGrid() {
        return cheatGrid;
    }

    public void setCheatGrid(char[][] cheatGrid) {
        this.cheatGrid = cheatGrid;
    }
}
