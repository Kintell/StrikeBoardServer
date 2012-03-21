package com.kokakiwi.kintell.plugins.strike.server;

import com.kokakiwi.kintell.server.plugins.ServerPlugin;

public class KintellStrikeBoardServerPlugin extends ServerPlugin
{
    private StrikeBoardFactory factory;
    
    @Override
    public void onEnable()
    {
        factory = new StrikeBoardFactory();
        
        getCore().registerBoardFactory(factory);
    }
}
