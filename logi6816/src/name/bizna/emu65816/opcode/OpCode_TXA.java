package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TXA
    extends OpCode
{
  public OpCode_TXA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide() && cpu.indexIs8BitWide())
    {
      byte value = Binary.lower8BitsOf(cpu.getX());
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.accumulatorIs8BitWide() && cpu.indexIs16BitWide())
    {
      byte value = Binary.lower8BitsOf(cpu.getX());
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else if (cpu.accumulatorIs16BitWide() && cpu.indexIs8BitWide())
    {
      byte value = Binary.lower8BitsOf(cpu.getX());
      cpu.setA(value);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setA(cpu.getX());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    }

    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

