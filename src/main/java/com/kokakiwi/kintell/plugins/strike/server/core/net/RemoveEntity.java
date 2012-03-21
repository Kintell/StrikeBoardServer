package com.kokakiwi.kintell.plugins.strike.server.core.net;

import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.Encodable;

public class RemoveEntity implements Encodable
{
    private String id;
    private String effect;
    
    public RemoveEntity()
    {
        this(null, "none");
    }
    
    public RemoveEntity(String id, String effect)
    {
        this.id = id;
        this.effect = effect;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getEffect()
    {
        return effect;
    }
    
    public void setEffect(String effect)
    {
        this.effect = effect;
    }
    
    public void encode(DataBuffer buf)
    {
        buf.writeString(id);
        buf.writeString(effect);
    }
    
    public void decode(DataBuffer buf)
    {
        id = buf.readString();
        effect = buf.readString();
    }
    
}
