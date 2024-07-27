package net.logicim.domain.integratedcircuit.wdc.wdc65816;

public class BCDResult
{
  public int value;
  public boolean carry;

  public BCDResult(int value, boolean carry)
  {
    this.value = value;
    this.carry = carry;
  }
}

