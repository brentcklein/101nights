package core;

import static core.Cost.*;

public class Night {
    private Integer id;
    private boolean complete;
    private boolean hasECard;
    private boolean involvesFood;
    private boolean involvesProps;
    private boolean involvesTravel;
    private Cost cost;

    public Night(Integer id, boolean complete, boolean hasECard, boolean involvesFood, boolean involvesProps, boolean involvesTravel, Cost cost) {
        this.id = id;
        this.complete = complete;
        this.hasECard = hasECard;
        this.involvesFood = involvesFood;
        this.involvesProps = involvesProps;
        this.involvesTravel = involvesTravel;
        this.cost = cost;
    }

    public Night(Integer id, boolean complete) {
        this(id,complete,false,false,false,false, FREE);
    }

    public Night(Integer id) {
        this(id,false);
    }

    public Integer getId() {
        return id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean hasECard() {
        return hasECard;
    }

    public void setHasECard(boolean hasECard) {
        this.hasECard = hasECard;
    }

    public boolean involvesFood() {
        return involvesFood;
    }

    public void setInvolvesFood(boolean involvesFood) {
        this.involvesFood = involvesFood;
    }

    public boolean involvesProps() {
        return involvesProps;
    }

    public void setInvolvesProps(boolean involvesProps) {
        this.involvesProps = involvesProps;
    }

    public boolean involvesTravel() {
        return involvesTravel;
    }

    public void setInvolvesTravel(boolean involvesTravel) {
        this.involvesTravel = involvesTravel;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }
}
