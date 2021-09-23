package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

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
      byte lowerX = Binary.lower8BitsOf(cpu.getX());
      lowerX++;
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
