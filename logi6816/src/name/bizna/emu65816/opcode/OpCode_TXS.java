package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TXS
    extends OpCode
{
  public OpCode_TXS(int mCode, InstructionCycles cycles)
  {
    super("TXS", " Transfer Index X to Stack Pointer Register", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.getCpuStatus().isEmulationMode())
    {
      int newStackPointer = 0x100 | Binary.getLowByte(cpu.getX());;
      cpu.setStackPointer(newStackPointer);
    }
    else
    {
      cpu.setStackPointer(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

