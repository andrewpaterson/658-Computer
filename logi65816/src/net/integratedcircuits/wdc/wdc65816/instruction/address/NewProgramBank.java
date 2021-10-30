package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class NewProgramBank
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
    return cpu.getNewProgramCounter().getBank();
  }

  @Override
  public String toString()
  {
    return "New_PBR,";
  }
}

