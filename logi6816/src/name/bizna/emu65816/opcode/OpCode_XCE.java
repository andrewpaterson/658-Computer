package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_XCE
    extends OpCode
{
  public OpCode_XCE(int mCode, InstructionCycles cycles)
  {
    super("XCE", "Exchange Carry and Emulation Bits", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 1)
    {
      boolean oldCarry = cpu.getCpuStatus().carryFlag();
      boolean oldEmulation = cpu.getCpuStatus().isEmulationMode();
      cpu.setEmulationMode(oldCarry);
      cpu.getCpuStatus().setCarryFlag(oldEmulation);

      cpu.setX(toByte(cpu.getX()));
      cpu.setY(toByte(cpu.getY()));

      cpu.getCpuStatus().setAccumulatorWidthFlag(cpu.getCpuStatus().isEmulationMode());
      cpu.getCpuStatus().setIndexWidthFlag(cpu.getCpuStatus().isEmulationMode());

      cpu.setStackPointer();

      cpu.noAddress();
    }
    else
    {
      invalidCycle();
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
    int cycle = cpu.getCycle();
    if (cycle == 1)
    {
      cpu.doneInstruction();
    }
    else
    {
      invalidCycle();
    }
  }
}

