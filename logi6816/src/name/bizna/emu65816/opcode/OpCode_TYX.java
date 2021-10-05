package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TYX
    extends OpCode
{
  public OpCode_TYX(int mCode, InstructionCycles cycles)
  {
    super("TYX", "Transfer Index Y to Index X", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int value = Binary.getLowByte(cpu.getY());
      cpu.setX(Binary.setLowByte(cpu.getX(), value));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(value);
    }
    else
    {
      cpu.setX(cpu.getY());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

