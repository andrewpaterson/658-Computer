package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class DataBank
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return 0;
  }

  @Override
  public int getBank(W65C816 cpu)
  {
    return cpu.getDataBank();
  }

  @Override
  public String toString()
  {
    return "DBR,";
  }
}

