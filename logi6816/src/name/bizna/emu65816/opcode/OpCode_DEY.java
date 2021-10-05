package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_DEY
    extends OpCode
{
  public OpCode_DEY(int mCode, InstructionCycles cycles)
  {
    super("DEY", "Decrement Index Y by One", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int lowerY = Binary.getLowByte(cpu.getY());
      lowerY--;
      lowerY = toByte(lowerY);
      cpu.setY(Binary.setLowByte(cpu.getY(), lowerY));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(lowerY);
    }
    else
    {
      int y = (cpu.getY());
      y--;
      y = toShort(y);
      cpu.setY(y);
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(y);
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}
