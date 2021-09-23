package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHX
    extends OpCode
{
  public OpCode_PHX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      cpu.getStack().push8Bit(Binary.lower8BitsOf(cpu.getX()));
      cpu.addToProgramAddressAndCycles(1, 3);
    }
    else
    {
      cpu.getStack().push16Bit(cpu.getX());
      cpu.addToProgramAddressAndCycles(1, 4);
    }
  }
}

