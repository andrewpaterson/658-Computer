package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.getStack().push8Bit(Binary.lower8BitsOf(cpu.getA()));
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.getStack().push16Bit(cpu.getA());
      cpu.addToProgramAddressAndCycles(1, 3);
    }
  }
}

