package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
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
      int x = (cpu.getX());
      x++;
      x = toShort(x);
      cpu.setX(x);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(x);
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

