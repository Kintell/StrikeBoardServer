package com.kokakiwi.kintell.plugins.strike.server.core.net;

import java.util.Map;

import com.google.common.collect.Maps;
import com.kokakiwi.kintell.plugins.strike.server.core.Location;
import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.Encodable;

public class UpdateEntity implements Encodable
{
    private String                    id;
    private Location                  location;
    private final Map<String, String> datas = Maps.newLinkedHashMap();
    
    public UpdateEntity()
    {
        this(null, new Location());
    }
    
    public UpdateEntity(String id, Location location)
    {
        this.id = id;
        this.location = location;
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
    
    public Map<String, String> getDatas()
    {
        return datas;
    }
    
    public void encode(DataBuffer buf)
    {
        buf.writeString(id);
        location.encode(buf);
        
        buf.writeInteger(datas.size());
        for (Map.Entry<String, String> data : datas.entrySet())
        {
            buf.writeString(data.getKey());
            buf.writeString(data.getValue());
        }
    }
    
    public void decode(DataBuffer buf)
    {
        id = buf.readString();
        location.decode(buf);
        
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
            String key = buf.readString();
            String value = buf.readString();
            
            datas.put(key, value);
        }
    }
    
}
