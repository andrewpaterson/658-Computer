package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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

