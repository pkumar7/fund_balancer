package entities.user;

public class FolioAllotment  {

    public Investment investment;
    public Double percentage;

    public FolioAllotment(Investment investment, Double percentage) {
        this.investment = investment;
        this.setPercentage(percentage);
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }


    public void rebalance() {

    }
    
}
