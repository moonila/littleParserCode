package org.moonila.code.parser.engine.lng;

import ai.serenade.treesitter.Node;

public interface LngParser {
    
    long getLngTreeSitter();

    LngStmtEnum getLngStmtEnum(Node currNode, boolean searchSubElmt);

}
