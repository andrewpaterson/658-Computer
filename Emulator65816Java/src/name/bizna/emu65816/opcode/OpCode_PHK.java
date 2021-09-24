package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHK
    extends OpCode
{
  public OpCode_PHK(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getStack().push8Bit(cpu.getProgramAddress().getBank());
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

