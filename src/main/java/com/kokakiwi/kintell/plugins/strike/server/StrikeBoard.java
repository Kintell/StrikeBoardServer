package com.kokakiwi.kintell.plugins.strike.server;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kokakiwi.kintell.plugins.strike.server.core.Entity;
import com.kokakiwi.kintell.plugins.strike.server.core.Location;
import com.kokakiwi.kintell.plugins.strike.server.core.Utilities;
import com.kokakiwi.kintell.plugins.strike.server.core.World;
import com.kokakiwi.kintell.plugins.strike.server.core.entities.Striker;
import com.kokakiwi.kintell.plugins.strike.server.core.net.RemoveEntity;
import com.kokakiwi.kintell.plugins.strike.server.core.net.SpawnEntity;
import com.kokakiwi.kintell.plugins.strike.server.core.net.UpdateEntity;
import com.kokakiwi.kintell.plugins.strike.server.core.net.Win;
import com.kokakiwi.kintell.server.core.KintellServerCore;
import com.kokakiwi.kintell.server.core.board.Board;

public class StrikeBoard extends Board
{
    public final static int            WIDTH    = 768;
    public final static int            HEIGHT   = WIDTH * 3 / 4;
    
    private final Map<String, Striker> strikers = Maps.newLinkedHashMap();
    private final Map<String, Entity>  entities = Maps.newLinkedHashMap();
    private final List<String>         removed  = Lists.newLinkedList();
    
    private int                        count    = 0;
    
    public StrikeBoard(KintellServerCore core)
    {
        super(core);
    }
    
    @Override
    public void configureProgram(RegisteredProgram program)
    {
        Striker striker = new Striker(program.getId(), this, program);
        World world = new World(this, striker);
        
        striker.setLocation(createLocation());
        
        strikers.put(program.getId(), striker);
        
        program.getExecutor().set("striker", striker);
        program.getExecutor().set("world", world);
        program.getExecutor().set("utils", new Utilities());
        
        sendData("updateEntity",
                new UpdateEntity(striker.getId(), striker.getLocation()));
    }
    
    public Location createLocation()
    {
        Location location = new Location();
        
        Random random = new Random();
        do
        {
            location.setX(random.nextDouble() * WIDTH);
            location.setY(random.nextDouble() * HEIGHT);
        } while (!checkLocation(location));
        
        return location;
    }
    
    public boolean checkLocation(Location loc)
    {
        boolean result = true;
        
        if (loc.getX() < Striker.WIDTH || loc.getY() < Striker.HEIGHT
                || Math.abs(loc.getX() - WIDTH) < Striker.WIDTH
                || Math.abs(loc.getY() - HEIGHT) < Striker.HEIGHT)
        {
            result = false;
        }
        else
        {
            for (Striker striker : strikers.values())
            {
                double diffX = Math.abs(striker.getLocation().getX()
                        - loc.getX());
                double diffY = Math.abs(striker.getLocation().getY()
                        - loc.getY());
                
                if (diffX < Striker.WIDTH || diffY < Striker.HEIGHT)
                {
                    result = false;
                }
            }
        }
        
        return result;
    }
    
    @Override
    public void init()
    {
        count = getStrikers().size();
        
        while (hasNext())
        {
            initProgram();
        }
    }
    
    @Override
    public void tick()
    {
        for (Entity entity : entities.values())
        {
            entity.update();
            sendData("updateEntity",
                    new UpdateEntity(entity.getId(), entity.getLocation()));
        }
        
        for (String id : removed)
        {
            removeEntity(id, "none");
        }
        removed.clear();
        
        while (hasNext())
        {
            RegisteredProgram program = next();
            Striker striker = strikers.get(program.getId());
            
            if (!striker.isDead())
            {
                striker.checkIsDead();
                
                if (!striker.isDead())
                {
                    tickProgram();
                    striker.update();
                }
                else
                {
                    count--;
                    checkWin();
                }
                
                UpdateEntity packet = new UpdateEntity(striker.getId(),
                        striker.getLocation());
                packet.getDatas()
                        .put("life", String.valueOf(striker.getLife()));
                
                sendData("updateEntity", packet);
            }
        }
    }
    
    public void checkWin()
    {
        if (count == 1)
        {
            Striker winner = getWinner();
            sendData("win", new Win(winner.getId()));
            
            setRunning(false);
        }
    }
    
    public Striker getWinner()
    {
        Striker winner = null;
        
        for (Striker striker : strikers.values())
        {
            if (!striker.isDead())
            {
                winner = striker;
            }
        }
        
        return winner;
    }
    
    public Map<String, Striker> getStrikers()
    {
        return strikers;
    }
    
    public Map<String, Entity> getEntities()
    {
        return entities;
    }
    
    public List<String> getRemoved()
    {
        return removed;
    }
    
    public String createEntityId(Class<? extends Entity> clazz)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName());
        sb.append('-');
        
        Random random = new Random();
        int entityId = random.nextInt();
        
        while (entities.containsKey(sb.toString() + String.valueOf(entityId)))
        {
            entityId = random.nextInt();
        }
        
        sb.append(entityId);
        
        return sb.toString();
    }
    
    public void addEntity(Entity entity)
    {
        entities.put(entity.getId(), entity);
        SpawnEntity packet = new SpawnEntity(entity.getType(), entity.getId(),
                entity.getLocation());
        sendData("spawnEntity", packet);
    }
    
    public void removeEntity(String id, String effect)
    {
        entities.remove(id);
        RemoveEntity packet = new RemoveEntity(id, effect);
        sendData("removeEntity", packet);
    }
}
