package com.kokakiwi.kintell.plugins.strike.server.core;

import com.kokakiwi.kintell.server.core.exec.annotations.NonAccessible;
import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.Encodable;

public class Location implements Encodable
{
    private double x;
    private double y;
    
    private float  angle;
    
    public Location()
    {
        this(0, 0, 0);
    }
    
    public Location(double x, double y, float angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    
    public double getX()
    {
        return x;
    }
    
    @NonAccessible
    public void setX(double x)
    {
        this.x = x;
    }
    
    public double getY()
    {
        return y;
    }
    
    @NonAccessible
    public void setY(double y)
    {
        this.y = y;
    }
    
    public float getAngle()
    {
        return angle;
    }
    
    @NonAccessible
    public void setAngle(float angle)
    {
        this.angle = angle;
    }
    
    @NonAccessible
    public void encode(DataBuffer buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeFloat(angle);
    }
    
    @NonAccessible
    public void decode(DataBuffer buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        angle = buf.readFloat();
    }
}
