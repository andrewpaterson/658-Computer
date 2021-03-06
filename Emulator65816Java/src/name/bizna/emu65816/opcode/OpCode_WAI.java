package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setRDYPin(false);

    cpu.addToProgramAddress(1);
    cpu.addToCycles(3);
  }
}

