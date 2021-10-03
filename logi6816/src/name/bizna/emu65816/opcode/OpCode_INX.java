package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(int mCode, InstructionCycles cycles)
  {
    super("INX", "Increment Index X by One", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int lowerX = Binary.getLowByte(cpu.getX());
      lowerX++;
      lowerX = toByte(lowerX);
      cpu.setX(Binary.setLowByte(cpu.getX(), lowerX));
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
}

