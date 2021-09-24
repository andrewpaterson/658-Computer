package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.indexIs8BitWide())
    {
      int lowerX = Binary.lower8BitsOf(cpu.getX());
      lowerX++;
      lowerX = toByte(lowerX);
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), lowerX));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerX);
    }
    else
    {
      cpu.incX();
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

