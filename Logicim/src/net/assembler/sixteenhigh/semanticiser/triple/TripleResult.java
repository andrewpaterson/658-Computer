package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.LogResult;

public class TripleResult
{
  public Triple triple;
  public LogResult logResult;

  public TripleResult(Triple triple, LogResult logResult)
  {
    this.triple = triple;
    this.logResult = logResult;
  }
}


