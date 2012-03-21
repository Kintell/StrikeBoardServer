package com.kokakiwi.kintell.plugins.strike.server;

import com.kokakiwi.kintell.server.core.KintellServerCore;
import com.kokakiwi.kintell.server.core.board.BoardFactory;

public class StrikeBoardFactory implements BoardFactory<StrikeBoard>
{
    
    public String getId()
    {
        return "strike";
    }
    
    public String getName()
    {
        return "Striker killmatch";
    }
    
    public StrikeBoard createBoard(KintellServerCore core)
    {
        StrikeBoard board = new StrikeBoard(core);
        
        return board;
    }
    
}
