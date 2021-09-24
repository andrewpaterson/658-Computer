package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if ((cpu.accumulatorIs8BitWide() && cpu.indexIs8BitWide()) ||
        (cpu.accumulatorIs16BitWide() && cpu.indexIs8BitWide()))
    {
      int lower8BitsOfA = Binary.lower8BitsOf(cpu.getA());
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), lower8BitsOfA));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
    }
    else
    {
      cpu.setX(cpu.getA());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

