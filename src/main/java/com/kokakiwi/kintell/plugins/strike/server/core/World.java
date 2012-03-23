package com.kokakiwi.kintell.plugins.strike.server.core;

import java.util.LinkedList;
import java.util.List;

import com.kokakiwi.kintell.plugins.strike.server.StrikeBoard;
import com.kokakiwi.kintell.plugins.strike.server.core.entities.Striker;
import com.kokakiwi.kintell.server.core.exec.annotations.NonAccessible;

public class World
{
    private final StrikeBoard board;
    private final Striker     striker;
    
    public World(StrikeBoard board, Striker striker)
    {
        this.board = board;
        this.striker = striker;
    }
    
    public int getWidth()
    {
        return StrikeBoard.WIDTH;
    }
    
    public int getHeight()
    {
        return StrikeBoard.HEIGHT;
    }
    
    public List<EnnemyStriker> getEnnemies()
    {
        return getEnnemies(false);
    }
    
    public List<EnnemyStriker> getEnnemies(boolean includeDead)
    {
        List<EnnemyStriker> ennemies = new LinkedList<EnnemyStriker>();
        
        for (Striker striker : board.getStrikers().values())
        {
            if (!striker.getId().equals(this.striker.getId())
                    && (!striker.isDead() || includeDead))
            {
                EnnemyStriker ennemyStriker = new EnnemyStriker(striker);
                ennemies.add(ennemyStriker);
            }
        }
        
        return ennemies;
    }
    
    public EnnemyStriker getNearestEnnemy(Location source,
            List<EnnemyStriker> ennemies)
    {
        EnnemyStriker nearest = null;
        double min = Double.MAX_VALUE;
        
        for (EnnemyStriker striker : ennemies)
        {
            double x0 = striker.getLocation().getX();
            double y0 = striker.getLocation().getY();
            
            double x1 = source.getX();
            double y1 = source.getY();
            
            double distance = Math.sqrt(Math.pow(Math.abs(x1 - x0), 2)
                    + Math.pow(Math.abs(y1 - y0), 2));
            
            if (distance < min)
            {
                min = distance;
                nearest = striker;
            }
        }
        
        return nearest;
    }
    
    public Entity getNearestEntity(Location source, List<Entity> entities)
    {
        Entity nearest = null;
        double min = Double.MAX_VALUE;
        
        for (Entity entity : entities)
        {
            double x0 = entity.getLocation().getX();
            double y0 = entity.getLocation().getY();
            
            double x1 = source.getX();
            double y1 = source.getY();
            
            double distance = Math.sqrt(Math.pow(Math.abs(x1 - x0), 2)
                    + Math.pow(Math.abs(y1 - y0), 2));
            
            if (distance < min)
            {
                min = distance;
                nearest = entity;
            }
        }
        
        return nearest;
    }
    
    @NonAccessible
    public StrikeBoard getBoard()
    {
        return board;
    }
    
    @NonAccessible
    public Striker getStriker()
    {
        return striker;
    }
}
