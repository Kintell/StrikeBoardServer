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
        final List<EnnemyStriker> ennemies = new LinkedList<EnnemyStriker>();
        
        for (final Striker striker : board.getStrikers().values())
        {
            if (!striker.getId().equals(this.striker.getId())
                    && (!striker.isDead() || includeDead))
            {
                final EnnemyStriker ennemyStriker = new EnnemyStriker(striker);
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
        
        for (final EnnemyStriker striker : ennemies)
        {
            final double x0 = striker.getLocation().getX();
            final double y0 = striker.getLocation().getY();
            
            final double x1 = source.getX();
            final double y1 = source.getY();
            
            final double distance = Math.sqrt(Math.pow(Math.abs(x1 - x0), 2)
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
        
        for (final Entity entity : entities)
        {
            final double x0 = entity.getLocation().getX();
            final double y0 = entity.getLocation().getY();
            
            final double x1 = source.getX();
            final double y1 = source.getY();
            
            final double distance = Math.sqrt(Math.pow(Math.abs(x1 - x0), 2)
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
