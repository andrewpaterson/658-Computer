package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_INY
    extends OpCode
{
  public OpCode_INY(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int lowerY = Binary.getLowByte(cpu.getY());
      lowerY++;
      lowerY = toByte(lowerY);
      cpu.setY(Binary.setLowByte(cpu.getY(), lowerY));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(lowerY);
    }
    else
    {
      int y = (cpu.getY());
      y++;
      y = toShort(y);
      cpu.setY(y);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(y);
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

