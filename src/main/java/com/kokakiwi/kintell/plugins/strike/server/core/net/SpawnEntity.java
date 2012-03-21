package com.kokakiwi.kintell.plugins.strike.server.core.net;

import com.kokakiwi.kintell.plugins.strike.server.core.Location;
import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.Encodable;

public class SpawnEntity implements Encodable
{
    private String   type;
    private String   id;
    private Location location;
    
    public SpawnEntity()
    {
        this(null, null, new Location());
    }
    
    public SpawnEntity(String type, String id, Location location)
    {
        this.type = type;
        this.id = id;
        this.location = location;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public Location getLocation()
    {
        return location;
    }
    
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    public void encode(DataBuffer buf)
    {
        buf.writeString(id);
        buf.writeString(type);
        location.encode(buf);
    }
    
    public void decode(DataBuffer buf)
    {
        id = buf.readString();
        type = buf.readString();
        location.decode(buf);
    }
    
}
