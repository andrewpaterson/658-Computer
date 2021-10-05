package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLY
    extends OpCode
{
  public OpCode_PLY(int mCode, InstructionCycles cycles)
  {
    super("PLY", "Pull Index Y from Stack", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      int value = cpu.pull8Bit();
      cpu.setY(Binary.setLowByte(cpu.getY(), value));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(value);
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setY(cpu.pull16Bit());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getY());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }
}

