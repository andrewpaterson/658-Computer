package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TYX
    extends OpCode
{
  public OpCode_TYX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getY());
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), value));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setX(cpu.getY());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

