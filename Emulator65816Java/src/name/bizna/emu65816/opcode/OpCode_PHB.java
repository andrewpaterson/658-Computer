package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getStack().push8Bit(cpu.getDB());
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

