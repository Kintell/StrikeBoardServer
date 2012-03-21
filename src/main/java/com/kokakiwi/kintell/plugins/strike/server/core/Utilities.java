package com.kokakiwi.kintell.plugins.strike.server.core;

import java.util.List;
import java.util.Random;

public class Utilities
{
    private final Random random = new Random();
    
    public double angleDiff(double a0, double a1)
    {
        double diff = a1 - a0;
        
        while (diff < -180)
        {
            diff += 360;
        }
        while (diff > 180)
        {
            diff -= 360;
        }
        
        return diff;
    }
    
    public Random random()
    {
        return random;
    }
    
    public EnnemyStriker randomEnnemy(List<EnnemyStriker> ennemies)
    {
        int index = Math.abs(random.nextInt()) % ennemies.size();
        
        return ennemies.get(index);
    }
}
