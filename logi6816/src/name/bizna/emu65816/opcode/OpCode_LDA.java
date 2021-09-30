package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(int mCode, InstructionCycles busCycles)
  {
    super("LDA", mCode, busCycles.getAddressingMode());
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    int value = cpu.getDataLow();
    cpu.setA(Binary.setLowByte(cpu.getA(), value));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    int value = cpu.getData();
    cpu.setA(value);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
    {
      executeLDA16Bit(cpu);
      cpu.addToCycles(1);
    }
    else
    {
      executeLDA8Bit(cpu);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

