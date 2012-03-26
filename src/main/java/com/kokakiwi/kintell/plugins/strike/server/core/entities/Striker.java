package com.kokakiwi.kintell.plugins.strike.server.core.entities;

import java.util.List;

import com.google.common.collect.Lists;
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
    public final static int         MAXBULLETS     = 60;
    public final static float       VIEWANGLE      = 15.0f * 2;
    
    private final RegisteredProgram program;
    
    private double                  motX           = 0;
    private double                  motY           = 0;
    private double                  motX2          = 0;
    private double                  motY2          = 0;
    private float                   motAngle       = 0;
    
    private int                     life           = MAXLIFE;
    private boolean                 dead           = false;
    private Counter                 bulletsCounter = null;
    
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
    
    @NonAccessible
    public void checkIsDead()
    {
        if (life <= 0)
        {
            dead = true;
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
        life -= amount;
        if (life <= 0)
        {
            life = 0;
        }
    }
    
    @NonAccessible
    public void heal(int amount)
    {
        life += amount;
        if (life > MAXLIFE)
        {
            life = MAXLIFE;
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
    
    public void shiftLeft()
    {
        motX2 = getSpeed()
                * Math.cos(Math.toRadians((location.getAngle() + motAngle + 90) % 360));
        motY2 = getSpeed()
                * Math.sin(Math.toRadians((location.getAngle() + motAngle + 90) % 360));
    }
    
    public void shiftRight()
    {
        motX2 = getSpeed()
                * Math.cos(Math.toRadians((location.getAngle() + motAngle - 90) % 360));
        motY2 = getSpeed()
                * Math.sin(Math.toRadians((location.getAngle() + motAngle - 90) % 360));
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
        
        if (bulletsCounter == null
                || bulletsCounter.increment() == bulletsCounter.max())
        {
            if (bulletsCounter == null)
            {
                bulletsCounter = new Counter(20);
            }
            
            final int x = (int) Math.floor(location.getX());
            final int y = (int) Math.floor(location.getY());
            
            final int x0 = (int) (x + WIDTH / 2 + (WIDTH / 2 + 13)
                    * Math.cos(Math.toRadians(location.getAngle())));
            final int y0 = (int) (y + HEIGHT / 2 + (WIDTH / 2 + 13)
                    * Math.sin(Math.toRadians(location.getAngle())));
            
            final Location bulletLocation = new Location(x0, y0,
                    location.getAngle());
            final Bullet bullet = new Bullet(board, this,
                    board.createEntityId(Bullet.class));
            bullet.setLocation(bulletLocation);
            board.addEntity(bullet);
        }
    }
    
    public double angleTo(Location to)
    {
        final double xx = to.getX() - getLocation().getX();
        final double yy = getLocation().getY() - to.getY();
        
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
        final double xx = to.getX() - getLocation().getX();
        final double yy = to.getY() - getLocation().getY();
        
        final double distance = Math.sqrt(Math.pow(Math.abs(xx), 2)
                + Math.pow(Math.abs(yy), 2));
        
        return distance;
    }
    
    public List<Bullet> viewBullets()
    {
        final List<Bullet> bullets = Lists.newLinkedList();
        
        for (final Entity entity : board.getEntities().values())
        {
            if (entity instanceof Bullet)
            {
                final Bullet bullet = (Bullet) entity;
                
                double angleTo = location.getAngle()
                        - angleTo(bullet.getLocation());
                while (angleTo < -180)
                {
                    angleTo += 360;
                }
                while (angleTo > 180)
                {
                    angleTo -= 360;
                }
                
                if (Math.abs(angleTo) < VIEWANGLE / 2)
                {
                    bullets.add(bullet);
                }
            }
        }
        
        return bullets;
    }
    
    @NonAccessible
    public RegisteredProgram getProgram()
    {
        return program;
    }
    
    @Override
    @NonAccessible
    public void update()
    {
        location.setX(location.getX() + motX);
        location.setX(location.getX() + motX2);
        if (location.getX() < 0)
        {
            location.setX(0);
        }
        if (location.getX() + WIDTH > StrikeBoard.WIDTH)
        {
            location.setX(StrikeBoard.WIDTH - WIDTH);
        }
        location.setY(location.getY() + motY);
        location.setY(location.getY() + motY2);
        if (location.getY() < 0)
        {
            location.setY(0);
        }
        if (location.getY() + HEIGHT > StrikeBoard.HEIGHT)
        {
            location.setY(StrikeBoard.HEIGHT - HEIGHT);
        }
        for (final Striker striker : board.getStrikers().values())
        {
            if (!striker.equals(this))
            {
                final double dx = striker.getLocation().getX()
                        - location.getX();
                final double dy = striker.getLocation().getY()
                        - location.getY();
                
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
                + (bulletsCounter == null ? 0 : bulletsCounter.hashCode());
        result = prime * result + life;
        result = prime * result + Float.floatToIntBits(motAngle);
        long temp;
        temp = Double.doubleToLongBits(motX);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(motY);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (program == null ? 0 : program.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Striker other = (Striker) obj;
        if (other.getId().equals(getId()))
        {
            return true;
        }
        return false;
    }
}
