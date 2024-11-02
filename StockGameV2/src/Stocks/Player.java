package Stocks;

public class Player {
    private double fund;
    private double investments;

    public Player(double fund){
        this.fund = fund;
    }

    public double getFund(){
        return fund;
    }
    public double getInvestments(){
        return investments;
    }
    public void setFund(double fund){
        this.fund = fund;
    }
    public void setInvestments(double investments){
        this.investments = investments;
    }
}
