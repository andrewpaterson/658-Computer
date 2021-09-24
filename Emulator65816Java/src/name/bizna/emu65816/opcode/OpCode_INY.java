package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_INY
    extends OpCode
{
  public OpCode_INY(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      int lowerY = Binary.lower8BitsOf(cpu.getY());
      lowerY++;
      lowerY = toByte(lowerY);
      cpu.setY(Binary.setLower8BitsOf16BitsValue(cpu.getY(), lowerY));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerY);
    }
    else
    {
      cpu.incY();
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getY());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

