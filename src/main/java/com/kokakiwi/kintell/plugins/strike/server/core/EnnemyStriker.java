package com.kokakiwi.kintell.plugins.strike.server.core;

import com.kokakiwi.kintell.plugins.strike.server.core.entities.Striker;

public class EnnemyStriker
{
    private final Striker striker;
    
    public EnnemyStriker(Striker striker)
    {
        this.striker = striker;
    }
    
    public Location getLocation()
    {
        return striker.getLocation();
    }
    
    public String getId()
    {
        return striker.getId();
    }
    
    public int getLife()
    {
        return striker.getLife();
    }
    
    public boolean isDead()
    {
        return striker.isDead();
    }
    
    public int getWidth()
    {
        return striker.getWidth();
    }
    
    public int getHeight()
    {
        return striker.getHeight();
    }
    
    public double getSpeed()
    {
        return striker.getSpeed();
    }
    
    public float getTurnSpeed()
    {
        return striker.getTurnSpeed();
    }
    
    public double angleTo(Location to)
    {
        return striker.angleTo(to);
    }
    
    public double distanceTo(Location to)
    {
        return striker.distanceTo(to);
    }
}
