package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TYA
    extends OpCode
{
  public OpCode_TYA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    if (cpu.accumulatorIs8BitWide() && cpu.indexIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getY());
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.accumulatorIs8BitWide() && cpu.indexIs16BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getY());
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.accumulatorIs16BitWide() && cpu.indexIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getY());
      cpu.setA(value);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setA(cpu.getY());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

