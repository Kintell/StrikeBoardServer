package com.kokakiwi.kintell.plugins.strike.server.core.entities;

import com.kokakiwi.kintell.plugins.strike.server.StrikeBoard;
import com.kokakiwi.kintell.plugins.strike.server.core.Entity;
import com.kokakiwi.kintell.plugins.strike.server.core.Location;
import com.kokakiwi.kintell.plugins.strike.server.utils.Counter;
import com.kokakiwi.kintell.server.core.board.Board.RegisteredProgram;
import com.kokakiwi.kintell.server.core.exec.annotations.NonAccessible;

public class Striker extends Entity
{
    public final static int         WIDTH          = 32;
    public final static int         HEIGHT         = 32;
    public final static int         MAXLIFE        = 100;
    
    private final RegisteredProgram program;
    
    private double                  motX           = 0;
    private double                  motY           = 0;
    private float                   motAngle       = 0;
    
    private int                     life           = MAXLIFE;
    private boolean                 dead           = false;
    private Counter                 bulletsCounter = new Counter(20);
    
    public Striker(String id, StrikeBoard board, RegisteredProgram program)
    {
        super(board, id, "tank");
        this.program = program;
    }
    
    public int getWidth()
    {
        return WIDTH;
    }
    
    public int getHeight()
    {
        return HEIGHT;
    }
    
    public double getSpeed()
    {
        return 3.0;
    }
    
    public float getTurnSpeed()
    {
        return 5.0F;
    }
    
    @Override
    public String getId()
    {
        return super.getId();
    }
    
    @Override
    public Location getLocation()
    {
        return super.getLocation();
    }
    
    public int getLife()
    {
        return life;
    }
    
    public boolean isDead()
    {
        return dead;
    }
    
    public void checkIsDead()
    {
        if (life <= 0)
        {
            this.dead = true;
        }
    }
    
    @NonAccessible
    public void setLife(int life)
    {
        this.life = life;
    }
    
    @NonAccessible
    public void damage(int amount)
    {
        this.life -= amount;
        if (this.life <= 0)
        {
            this.life = 0;
        }
    }
    
    @NonAccessible
    public void heal(int amount)
    {
        this.life += amount;
        if (this.life > MAXLIFE)
        {
            this.life = MAXLIFE;
        }
    }
    
    public void forward()
    {
        motX = getSpeed()
                * Math.cos(Math.toRadians((location.getAngle() + motAngle) % 360));
        motY = getSpeed()
                * Math.sin(Math.toRadians((location.getAngle() + motAngle) % 360));
    }
    
    public void backward()
    {
        motX = getSpeed()
                * -Math.cos(Math.toRadians((location.getAngle() + motAngle) % 360));
        motY = getSpeed()
                * -Math.sin(Math.toRadians((location.getAngle() + motAngle) % 360));
    }
    
    public void turnLeft()
    {
        motAngle = getTurnSpeed();
    }
    
    public void turnRight()
    {
        motAngle = -getTurnSpeed();
    }
    
    public Counter getBulletsCounter()
    {
        return bulletsCounter;
    }
    
    public void fire()
    {
        if (bulletsCounter.increment() == bulletsCounter.max())
        {
            int x = (int) Math.floor(location.getX());
            int y = (int) Math.floor(location.getY());
            
            int x0 = (int) (x + (WIDTH / 2) + (WIDTH / 2 + 13)
                    * Math.cos(Math.toRadians(location.getAngle())));
            int y0 = (int) (y + (HEIGHT / 2) + (WIDTH / 2 + 13)
                    * Math.sin(Math.toRadians(location.getAngle())));
            
            Location bulletLocation = new Location(x0, y0, location.getAngle());
            Bullet bullet = new Bullet(board, this,
                    board.createEntityId(Bullet.class));
            bullet.setLocation(bulletLocation);
            board.addEntity(bullet);
        }
    }
    
    public double angleTo(Location to)
    {
        double xx = to.getX() - getLocation().getX();
        double yy = getLocation().getY() - to.getY();
        
        double rads = Math.atan2(yy, xx);
        
        if (rads < 0)
        {
            rads = Math.abs(rads);
        }
        else
        {
            rads = 2 * Math.PI - rads;
        }
        
        return Math.toDegrees(rads);
    }
    
    public double distanceTo(Location to)
    {
        double xx = to.getX() - getLocation().getX();
        double yy = to.getY() - getLocation().getY();
        
        double distance = Math.sqrt(Math.pow(Math.abs(xx), 2)
                + Math.pow(Math.abs(yy), 2));
        
        return distance;
    }
    
    @NonAccessible
    public RegisteredProgram getProgram()
    {
        return program;
    }
    
    @NonAccessible
    public void update()
    {
        location.setX(location.getX() + motX);
        if (location.getX() < 0)
        {
            location.setX(0);
        }
        if (location.getX() + WIDTH > StrikeBoard.WIDTH)
        {
            location.setX(StrikeBoard.WIDTH - WIDTH);
        }
        location.setY(location.getY() + motY);
        if (location.getY() < 0)
        {
            location.setY(0);
        }
        if (location.getY() + HEIGHT > StrikeBoard.HEIGHT)
        {
            location.setY(StrikeBoard.HEIGHT - HEIGHT);
        }
        for (Striker striker : board.getStrikers().values())
        {
            if (!striker.equals(this))
            {
                double dx = striker.getLocation().getX() - location.getX();
                double dy = striker.getLocation().getY() - location.getY();
                
                if (Math.abs(dx) < WIDTH && Math.abs(dy) < HEIGHT)
                {
                    if (Math.abs(dx) > Math.abs(dy))
                    {
                        if (dx > 0)
                        {
                            location.setX(striker.getLocation().getX() - WIDTH);
                        }
                        else
                        {
                            location.setX(striker.getLocation().getX() + WIDTH);
                        }
                    }
                    else
                    {
                        if (dy > 0)
                        {
                            location.setY(striker.getLocation().getY() - HEIGHT);
                        }
                        else
                        {
                            location.setY(striker.getLocation().getY() + HEIGHT);
                        }
                    }
                }
            }
        }
        location.setAngle((location.getAngle() + motAngle) % 360);
        
        motX = 0;
        motY = 0;
        motAngle = 0;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((bulletsCounter == null) ? 0 : bulletsCounter.hashCode());
        result = prime * result + life;
        result = prime * result + Float.floatToIntBits(motAngle);
        long temp;
        temp = Double.doubleToLongBits(motX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(motY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((program == null) ? 0 : program.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Striker other = (Striker) obj;
        if (other.getId().equals(getId()))
        {
            return true;
        }
        return false;
    }
}
