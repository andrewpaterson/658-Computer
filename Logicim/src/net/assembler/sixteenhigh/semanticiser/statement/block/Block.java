package net.assembler.sixteenhigh.semanticiser.statement.block;

import net.assembler.sixteenhigh.semanticiser.statement.Statement;

import java.util.List;

public abstract class Block
    extends Statement
{
  public List<Statement> statements;
}

