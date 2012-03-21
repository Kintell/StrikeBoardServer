package com.kokakiwi.kintell.plugins.strike.server.core;

import com.kokakiwi.kintell.plugins.strike.server.StrikeBoard;

public abstract class Entity
{
    protected final StrikeBoard board;
    protected final String    id;
    protected final String    type;
    protected Location        location = new Location();
    
    public Entity(StrikeBoard board, String id, String type)
    {
        this.board = board;
        this.id = id;
        this.type = type;
    }
    
    public StrikeBoard getBoard()
    {
        return board;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getType()
    {
        return type;
    }
    
    public Location getLocation()
    {
        return location;
    }
    
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    public void remove()
    {
        board.getRemoved().add(id);
    }
    
    public abstract void update();
}
