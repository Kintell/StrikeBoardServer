package com.kokakiwi.kintell.plugins.strike.server.core.entities;

import com.kokakiwi.kintell.plugins.strike.server.StrikeBoard;
import com.kokakiwi.kintell.plugins.strike.server.core.Entity;

public class Bullet extends Entity
{
    public final static int    WIDTH  = 3;
    public final static int    HEIGHT = 3;
    public final static double SPEED  = 10.0;
    public final static int    DAMAGE = Striker.MAXLIFE / 10;
    
    private final Striker      owner;
    
    public Bullet(StrikeBoard board, Striker owner, String id)
    {
        super(board, id, "bullet");
        this.owner = owner;
    }
    
    public boolean isOwn(Striker tank)
    {
        return tank.equals(owner);
    }
    
    @Override
    public void update()
    {
        if (location.getX() < 0 || location.getY() < 0
                || location.getX() > StrikeBoard.WIDTH
                || location.getY() > StrikeBoard.HEIGHT)
        {
            remove();
        }
        
        for (Striker striker : board.getStrikers().values())
        {
            if (!striker.equals(owner))
            {
                double dx = (striker.getLocation().getX() + (Striker.WIDTH / 2))
                        - location.getX();
                double dy = (striker.getLocation().getY() + (Striker.HEIGHT / 2))
                        - location.getY();
                
                double distance = Math.abs(Math.sqrt(Math.pow(dx, 2)
                        + Math.pow(dy, 2)));
                
                if (distance < (Striker.WIDTH / 2))
                {
                    striker.damage(DAMAGE);
                    remove();
                }
            }
        }
        
        double motX = SPEED
                * Math.cos(Math.toRadians((location.getAngle()) % 360));
        double motY = SPEED
                * Math.sin(Math.toRadians((location.getAngle()) % 360));
        
        location.setX(location.getX() + motX);
        location.setY(location.getY() + motY);
    }
}
