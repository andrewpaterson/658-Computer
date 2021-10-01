package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class SetProgramBank
    extends Operation
{
  private int bank;

  public SetProgramBank(int bank)
  {
    this.bank = bank;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setProgramAddressBank(bank);
  }
}

