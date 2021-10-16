package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class SetProgramBank
    extends Operation
{
  private int bank;

  public SetProgramBank(int bank)
  {
    this.bank = bank;
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setProgramAddressBank(bank);
  }

  @Override
  public String toString()
  {
    return "PBR=" + bank;
  }
}

