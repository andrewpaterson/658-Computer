package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class SetProgramBank
    extends Operation
{
  private int bank;

  public SetProgramBank(int bank)
  {
    this.bank = bank;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setProgramAddressBank(bank);
  }

  @Override
  public String toString()
  {
    return "PBR=" + bank;
  }
}

