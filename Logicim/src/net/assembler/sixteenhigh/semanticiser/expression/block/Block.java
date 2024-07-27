package net.assembler.sixteenhigh.semanticiser.expression.block;

import net.assembler.sixteenhigh.semanticiser.triple.Triple;

import java.util.ArrayList;
import java.util.List;

public abstract class Block
{
  public List<Triple> triples;

  public Block()
  {
    triples = new ArrayList<>();
  }

  public void pushTriples(List<Triple> triples)
  {
    this.triples.addAll(triples);
  }

  public List<Triple> getTriples()
  {
    return triples;
  }
}

