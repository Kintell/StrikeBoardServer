package com.kokakiwi.kintell.plugins.strike.server.core.net;

import com.kokakiwi.kintell.spec.utils.data.DataBuffer;
import com.kokakiwi.kintell.spec.utils.data.Encodable;

public class Win implements Encodable
{
    private String id;
    
    public Win(String id)
    {
        this.id = id;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public void encode(DataBuffer buf)
    {
        buf.writeString(id);
    }
    
    public void decode(DataBuffer buf)
    {
        id = buf.readString();
    }
    
}
