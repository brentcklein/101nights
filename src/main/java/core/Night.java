package core;

import static core.Cost.*;
import static core.Partner.*;

public class Night {
    private Integer id;
    private boolean complete;
    private boolean hasECard;
    private boolean involvesFood;
    private boolean involvesProps;
    private boolean involvesTravel;
    private Cost cost;
    private Partner partner;

    public Night(Integer id, boolean complete, boolean hasECard, boolean involvesFood, boolean involvesProps, boolean involvesTravel, Cost cost, Partner partner) {
        this.id = id;
        this.complete = complete;
        this.hasECard = hasECard;
        this.involvesFood = involvesFood;
        this.involvesProps = involvesProps;
        this.involvesTravel = involvesTravel;
        this.cost = cost;
        this.partner = partner;
    }

    public Night(Integer id, boolean complete) {
        this(id,complete,false,false,false,false, FREE, BOTH);
    }

    public Night(Integer id) {
        this(id,false);
    }

    public Night() { this(null); }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
