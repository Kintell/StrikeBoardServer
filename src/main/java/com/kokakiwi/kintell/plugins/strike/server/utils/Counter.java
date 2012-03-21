package com.kokakiwi.kintell.plugins.strike.server.utils;

import com.kokakiwi.kintell.server.core.exec.annotations.NonAccessible;

public class Counter
{
    private int max;
    private int count = 0;
    
    public Counter(int max)
    {
        this.max = max;
    }
    
    public int max()
    {
        return max;
    }
    
    @NonAccessible
    public void setMax(int max)
    {
        this.max = max;
    }
    
    public int increment()
    {
        count++;
        if (count > max)
        {
            count = 0;
        }
        
        return count;
    }
    
    public int count()
    {
        return count;
    }
}
